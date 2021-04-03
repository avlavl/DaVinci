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

import static com.aioros.investor.Constant.*;
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
    public static int mDataSource = 0;
    private int[] indexArray = new int[]{INDEX_SWZQ, INDEX_YLCY, INDEX_YYYB, INDEX_ZZWB};
    private int[] weeksArray = new int[NUMBER_INVEST];
    private double[] closeArray = new double[NUMBER_INVEST];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate------");
        mMainActivity = (MainActivity) getActivity();
        mMarketDatas = mMainActivity.mMarketDatas;
        mBeanInvestList.add(new BeanInvest(1000, 6.9, 0.8, 1.5, 100, 0.2));    // 1/100 + 1/25 = 1/20
        mBeanInvestList.add(new BeanInvest(1000, 6.9, 0.8, 1.5, 25, 2));
        mBeanInvestList.add(new BeanInvest(1250, 10.3, 1, 2, 108, 0.5));    // 1/108 + 1/36 = 1/27
        mBeanInvestList.add(new BeanInvest(1250, 10.3, 1, 2, 36, 2));
        mBeanInvestList.add(new BeanInvest(7800, 16, 1, 3, 80, 0.3));    // 1/80 + 1/80 = 1/40
        mBeanInvestList.add(new BeanInvest(7800, 16, 1, 3, 80, 1.5));
        mBeanInvestList.add(new BeanInvest(2300, 6.3, 1, 2, 48, 0.3));    // 1/48 + 1/24 = 1/16
        mBeanInvestList.add(new BeanInvest(2300, 6.3, 1, 2, 24, 2));

        for (int i = 0; i < indexArray.length; i++) {
            fileUtility.importDataFile1("investor/data/W" + INVEST_NAMES[i] + ".txt");
            weeksArray[i] = fileUtility.rows1;
            closeArray[i] = fileUtility.closeList1.get(fileUtility.rows1 - 1);
            if (weeksArray[i] > 0) {
                for (int j = 0; j < 2; j++) {
                    mBeanInvestList.get(2 * i + j).mRealPoint = Double.parseDouble(mMarketDatas[indexArray[i]][2]);
                    StrategyInvest strategyInvest = new StrategyInvest(fileUtility);
                    strategyInvest.sysInvestEva(mBeanInvestList.get(2 * i + j));
                    mBeanInvestList.get(2 * i + j).mRecordDataList = strategyInvest.recordDataList;
                    double basePoint = strategyInvest.basePoints[strategyInvest.items - 1] + mBeanInvestList.get(2 * i + j).mSlope;
                    mBeanInvestList.get(2 * i + j).mBasePoint = basePoint;
                    mBeanInvestList.get(2 * i + j).mDispersion = mBeanInvestList.get(2 * i + j).mRealPoint / basePoint;
                    double investAmount = (basePoint / mBeanInvestList.get(2 * i + j).mDivisor) / Math.pow(mBeanInvestList.get(2 * i + j).mDispersion, mBeanInvestList.get(2 * i + j).mDiffCoef);
                    mBeanInvestList.get(2 * i + j).mQuota = (mBeanInvestList.get(2 * i + j).mDispersion <= mBeanInvestList.get(2 * i + j).mInvestLevel) ? investAmount : 0;
                    mBeanInvestList.get(2 * i + j).mTimes = strategyInvest.getInvestTimes();
                    mBeanInvestList.get(2 * i + j).mCurrentCost = (strategyInvest.totalNumber != 0) ? strategyInvest.getCurrentCost() : 0;
                    mBeanInvestList.get(2 * i + j).mTotalNumber = strategyInvest.totalNumber;
                    mBeanInvestList.get(2 * i + j).mProperty = strategyInvest.totalNumber * mBeanInvestList.get(2 * i + j).mRealPoint;
                    mBeanInvestList.get(2 * i + j).mIncome = strategyInvest.totalNumber * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost);
                    mBeanInvestList.get(2 * i + j).mWeekIncome = strategyInvest.totalNumber * (mBeanInvestList.get(2 * i + j).mRealPoint - closeArray[i]);
                    mBeanInvestList.get(2 * i + j).mYield = (mBeanInvestList.get(2 * i + j).mCurrentCost != 0) ? 100 * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost) / mBeanInvestList.get(2 * i + j).mCurrentCost : 0;
                    mBeanInvestList.get(2 * i + j).mKeyPoint = strategyInvest.getKeyPoint();
                    mBeanInvestList.get(2 * i + j).mWeeks = strategyInvest.getInvestWeeks();
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
                            mBeanInvestList.get(2 * i + j).mRealPoint = Double.parseDouble(mMarketDatas[indexArray[i]][2]);
                            double basePoint = mBeanInvestList.get(2 * i + j).mStartPoint + weeksArray[i] * mBeanInvestList.get(2 * i + j).mSlope;
                            mBeanInvestList.get(2 * i + j).mBasePoint = basePoint;
                            mBeanInvestList.get(2 * i + j).mDispersion = mBeanInvestList.get(2 * i + j).mRealPoint / basePoint;
                            double investAmount = (basePoint / mBeanInvestList.get(2 * i + j).mDivisor) / Math.pow(mBeanInvestList.get(2 * i + j).mDispersion, mBeanInvestList.get(2 * i + j).mDiffCoef);
                            mBeanInvestList.get(2 * i + j).mQuota = (mBeanInvestList.get(2 * i + j).mDispersion <= mBeanInvestList.get(2 * i + j).mInvestLevel) ? investAmount : 0;
                            mBeanInvestList.get(2 * i + j).mProperty = mBeanInvestList.get(2 * i + j).mTotalNumber * mBeanInvestList.get(2 * i + j).mRealPoint;
                            mBeanInvestList.get(2 * i + j).mIncome = mBeanInvestList.get(2 * i + j).mTotalNumber * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost);
                            mBeanInvestList.get(2 * i + j).mWeekIncome = mBeanInvestList.get(2 * i + j).mTotalNumber * (mBeanInvestList.get(2 * i + j).mRealPoint - closeArray[i]);
                            mBeanInvestList.get(2 * i + j).mYield = (mBeanInvestList.get(2 * i + j).mCurrentCost != 0) ? 100 * (mBeanInvestList.get(2 * i + j).mRealPoint - mBeanInvestList.get(2 * i + j).mCurrentCost) / mBeanInvestList.get(2 * i + j).mCurrentCost : 0;
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

        fileUtility.importDataFile1("investor/data/W申万证券.txt");
        String fileDate = fileUtility.dateList1.get(fileUtility.rows1 - 1);
        mTextViewDate = (TextView) view.findViewById(R.id.textViewTradeRecordBpDate);
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
        MainActivity.currFragTag = FRAGMENT_FLAG_INVEST;
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
            for (int i = 0; i < INVEST_NAMES.length; i++) {
                String filePath = storageDir + "/investor/data/W" + INVEST_NAMES[i] + ".txt";
                File file = new File(filePath);
                if (file.exists()) {
                    String httpStr = "";
                    String dataStr = "";
                    do {
                        String urlStr = ((mDataSource == 0) ? "http://qt.gtimg.cn/r=0.8409869808238q=s_s" : "http://hq.sinajs.cn/list=s") + INVEST_CODES[i];
                        httpStr = HttpUtility.getData(urlStr);
                        if (httpStr.equals("")) {
                            msg.obj = "Lose connection 2 !";
                            mHandler.sendMessage(msg);
                            return;
                        } else if (httpStr.contains("pv_none_match")) {
                            mDataSource = 1;
                        } else if (httpStr.contains("\"\"")) {
                            mDataSource = 0;
                        }
                    } while (httpStr.contains("pv_none_match") || httpStr.contains("\"\""));

                    if (mDataSource == 0) {
                        String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split("~");
                        latestDate = TimeUtility.getCurrentDate();
                        dataStr = String.format("%s\t%.2f\t%.2f\t%.2f\t%.2f\r", latestDate,
                                Double.parseDouble(strs[3]), Double.parseDouble(strs[3]), Double.parseDouble(strs[3]), Double.parseDouble(strs[3]));
                    } else {
                        String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split(",");
                        latestDate = strs[30].replace("-", "/");
                        dataStr = String.format("%s\t%.2f\t%.2f\t%.2f\t%.2f\r", latestDate,
                                Double.parseDouble(strs[1]), Double.parseDouble(strs[4]), Double.parseDouble(strs[5]), Double.parseDouble(strs[3]));
                    }

                    try {
                        PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                        pw.println(dataStr);
                        pw.close();
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
