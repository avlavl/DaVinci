package com.aioros.investor;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.aioros.investor.TimeUtility.isWeekUpdateTime;

/**
 * Created by aizhang on 2017/6/7.
 */
public class FragmentInvest extends BaseFragment {
    private static final String TAG = "FragmentInvest";
    private MainActivity mMainActivity;
    private AdapterPagerInvest mAdapterPager;
    private List<BeanInvest> mBeanInvestList = new ArrayList<BeanInvest>();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView mTextViewDate;
    private Button mButtonUpdate;
    private FileUtility fileUtility = new FileUtility();
    public Handler mHandler;
    private String latestDate;
    public String[][] mMarketDatas;
    private String mTabTitles[] = new String[]{"申万证券", "养老产业", "中证传媒"};
    private String mTabCodes[] = new String[]{"z399707", "z399812", "z399971"};
    private int[] indexArray = new int[]{5, 6, 7};
    private int[] weeksArray = new int[3];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        mBeanInvestList.add(new BeanInvest(1000, 7, 1.5, 100, 0.2));    // 1/100 + 1/25 = 1/20
        mBeanInvestList.add(new BeanInvest(1000, 7, 1.5, 25, 2));
        mBeanInvestList.add(new BeanInvest(1200, 11, 1.5, 120, 0.3));    // 1/120 + 1/40 = 1/30
        mBeanInvestList.add(new BeanInvest(1200, 11, 1.5, 40, 2));
        mBeanInvestList.add(new BeanInvest(2200, 2, 1.5, 24, 0.2));    // 1/24 + 1/12 = 1/8
        mBeanInvestList.add(new BeanInvest(2200, 2, 1.5, 12, 1));

        for (int i = 0; i < indexArray.length; i++) {
            fileUtility.importDataFile("investor/data/W" + mTabTitles[i] + ".txt");
            weeksArray[i] = fileUtility.rows;
            if (weeksArray[i] > 0) {
                for (int j = 0; j < 2; j++) {
                    mBeanInvestList.get(2 * i + j).mRealPoint = Double.parseDouble(mMarketDatas[indexArray[i]][1]);
                    StrategyInvest strategyInvest = new StrategyInvest(fileUtility);
                    strategyInvest.sysInvestEva(mBeanInvestList.get(2 * i + j));
                    double basePoint = strategyInvest.basePoints[strategyInvest.items - 1] + mBeanInvestList.get(2 * i + j).mSlope;
                    mBeanInvestList.get(2 * i + j).mBasePoint = basePoint;
                    double diffRate = mBeanInvestList.get(2 * i + j).mRealPoint / basePoint;
                    double investAmount = (basePoint / mBeanInvestList.get(2 * i + j).mDivisor) / Math.pow(diffRate, mBeanInvestList.get(2 * i + j).mDiffCoef);
                    mBeanInvestList.get(2 * i + j).mQuota = (diffRate <= 1) ? investAmount : 0;
                    mBeanInvestList.get(2 * i + j).mTimes = strategyInvest.getInvestTimes();
                    mBeanInvestList.get(2 * i + j).mCurrentCost = strategyInvest.getCurrentCost();
                    mBeanInvestList.get(2 * i + j).mTotalNumber = strategyInvest.totalNumber;
                    mBeanInvestList.get(2 * i + j).mProperty = strategyInvest.totalNumber * mBeanInvestList.get(2 * i + j).mRealPoint;
                    mBeanInvestList.get(2 * i + j).mIncome = strategyInvest.totalNumber * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost);
                    mBeanInvestList.get(2 * i + j).mYield = 100 * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost) / mBeanInvestList.get(2 * i + j).mCurrentCost;
                    mBeanInvestList.get(2 * i + j).mKeyPoint = strategyInvest.getKeyPoint();
                }
            }
        }

        // 在主线程中声明一个消息处理对象Handler
        mMainActivity.mInvestHandler = new Handler() {
            // 重载消息处理方法，用于接收和处理WorkerThread发送的消息
            @Override
            public void handleMessage(Message msg) {
                mMarketDatas = (String[][]) msg.obj;
                for (int i = 0; i < indexArray.length; i++) {
                    if (weeksArray[i] > 0) {
                        for (int j = 0; j < 2; j++) {
                            mBeanInvestList.get(2 * i + j).mRealPoint = Double.parseDouble(mMarketDatas[indexArray[i]][1]);
                            double basePoint = mBeanInvestList.get(2 * i + j).mStartPoint + weeksArray[i] * mBeanInvestList.get(2 * i + j).mSlope;
                            mBeanInvestList.get(2 * i + j).mBasePoint = basePoint;
                            double diffRate = mBeanInvestList.get(2 * i + j).mRealPoint / basePoint;
                            double investAmount = (basePoint / mBeanInvestList.get(2 * i + j).mDivisor) / Math.pow(diffRate, mBeanInvestList.get(2 * i + j).mDiffCoef);
                            mBeanInvestList.get(2 * i + j).mQuota = (diffRate <= 1) ? investAmount : 0;
                            mBeanInvestList.get(2 * i + j).mProperty = mBeanInvestList.get(2 * i + j).mTotalNumber * mBeanInvestList.get(2 * i + j).mRealPoint;
                            mBeanInvestList.get(2 * i + j).mIncome = mBeanInvestList.get(2 * i + j).mTotalNumber * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost);
                            mBeanInvestList.get(2 * i + j).mYield = 100 * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost) / mBeanInvestList.get(2 * i + j).mCurrentCost;
                        }
                    }
                }
                mAdapterPager.notifyDataSetChanged();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView---->");
        View view = inflater.inflate(R.layout.fragment_invest, container, false);
        mFragmentManager = getActivity().getFragmentManager();
        mAdapterPager = new AdapterPagerInvest(mMainActivity, mBeanInvestList);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerInvest);
        mViewPager.setAdapter(mAdapterPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayoutInvest);
        mTabLayout.setupWithViewPager(mViewPager);

        fileUtility.importDataFile("investor/data/W申万证券.txt");
        String fileDate = fileUtility.dateList.get(fileUtility.rows - 1);
        mTextViewDate = (TextView) view.findViewById(R.id.textViewInvestDate);
        mTextViewDate.setText(fileDate);

        mButtonUpdate = (Button) view.findViewById(R.id.buttonInvestUpdate);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOnClick(v);
            }
        });
        int durations = TimeUtility.daysBetween(fileDate, TimeUtility.getCurrentDate());
        mButtonUpdate.setVisibility(((durations > 6) && isWeekUpdateTime()) ? View.VISIBLE : View.INVISIBLE);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                Toast.makeText(mMainActivity, message, Toast.LENGTH_LONG).show();
                if (message.equals("更新成功！")) {
                    mTextViewDate.setText(latestDate);
                    mButtonUpdate.setVisibility(View.INVISIBLE);
                }
            }
        };

        mAdapterPager.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.currFragTag = Constant.FRAGMENT_FLAG_INVEST;
    }

    private void buttonOnClick(View v) {
        Thread udt = new UpdateDataThread();
        udt.start();
    }

    class UpdateDataThread extends Thread {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            String storageDir = Environment.getExternalStorageDirectory().toString();
            for (int i = 0; i < mTabTitles.length; i++) {
                String filePath = storageDir + "/investor/data/W" + mTabTitles[i] + ".txt";
                File file = new File(filePath);
                if (file.exists()) {
                    try {
                        String urlStr = "http://hq.sinajs.cn/list=s" + mTabCodes[i];
                        HttpUtility httpUtility = new HttpUtility();
                        String httpStr = httpUtility.getData(urlStr);
                        if (httpStr.equals("")) {
                            msg.obj = "网络无连接！";
                            mHandler.sendMessage(msg);
                            return;
                        } else if (httpStr.contains("\"\"")) {
                            msg.obj = "找不到对应的股票！";
                            mHandler.sendMessage(msg);
                            return;
                        } else {
                            String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split(",");
                            latestDate = strs[30].replace("-", "/");
                            String dataStr = String.format("%s\t%.2f\t%.2f\t%.2f\t%.2f\r", latestDate,
                                    Double.parseDouble(strs[1]), Double.parseDouble(strs[4]), Double.parseDouble(strs[5]), Double.parseDouble(strs[3]));
                            PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                            pw.println(dataStr);
                            pw.close();
                        }
                    } catch (IOException | NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            msg.obj = "更新成功！";
            mHandler.sendMessage(msg);
        }
    }
}
