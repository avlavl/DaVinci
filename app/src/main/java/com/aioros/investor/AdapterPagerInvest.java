package com.aioros.investor;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
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
import java.util.List;

/**
 * Created by aizhang on 2017/6/17.
 */

public class AdapterPagerInvest extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String mTabTitles[] = new String[]{"申万证券", "养老产业"};
    private String mTabCodes[] = new String[]{"399707", "399812"};
    private List<BeanInvest> mInvestBeanList = null;


    public AdapterPagerInvest(Context context, List<BeanInvest> beanList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mInvestBeanList = beanList;
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.pager_invest, null);

        TextView textViewInvestQuota = (TextView) view.findViewById(R.id.textViewInvestQuota);
        textViewInvestQuota.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mQuota + mInvestBeanList.get(2 * position + 1).mQuota));
        TextView textViewInvestRealPoint = (TextView) view.findViewById(R.id.textViewInvestRealPoint);
        textViewInvestRealPoint.setText("" + mInvestBeanList.get(2 * position).mRealPoint);
        TextView textViewInvestBasePoint = (TextView) view.findViewById(R.id.textViewInvestBasePoint);
        textViewInvestBasePoint.setText(String.format("%d", (int) mInvestBeanList.get(2 * position).mBasePoint));
        TextView textViewInvestTotalProperty = (TextView) view.findViewById(R.id.textViewInvestTotalProperty);
        textViewInvestTotalProperty.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mProperty + mInvestBeanList.get(2 * position + 1).mProperty));

        TextView textViewInvestTimesShort = (TextView) view.findViewById(R.id.textViewInvestTimesShort);
        textViewInvestTimesShort.setText(String.format("定投%d次", mInvestBeanList.get(2 * position).mTimes));
        TextView textViewInvestWinPointShort = (TextView) view.findViewById(R.id.textViewInvestCostShort);
        textViewInvestWinPointShort.setText(String.format("本%.2f", mInvestBeanList.get(2 * position).mCurrentCost));
        TextView textViewInvestProperty = (TextView) view.findViewById(R.id.textViewInvestProperty);
        textViewInvestProperty.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mProperty));
        TextView textViewInvestYield = (TextView) view.findViewById(R.id.textViewInvestYield);
        textViewInvestYield.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position).mYield));
        TextView textViewInvestKeyPoint = (TextView) view.findViewById(R.id.textViewInvestKeyPoint);
        textViewInvestKeyPoint.setText(String.format("%.2f", mInvestBeanList.get(2 * position).mKeyPoint));
        TextView textViewInvestKeyRatio = (TextView) view.findViewById(R.id.textViewInvestKeyRatio);
        textViewInvestKeyRatio.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position).mKeyRatio));
        TextView textViewInvestTimesLong = (TextView) view.findViewById(R.id.textViewInvestTimesLong);
        textViewInvestTimesLong.setText(String.format("定投%d次", mInvestBeanList.get(2 * position + 1).mTimes));
        TextView textViewInvestWinPointLong = (TextView) view.findViewById(R.id.textViewInvestCostLong);
        textViewInvestWinPointLong.setText(String.format("本%.2f", mInvestBeanList.get(2 * position + 1).mCurrentCost));
        TextView textViewInvestProperty1 = (TextView) view.findViewById(R.id.textViewInvestProperty1);
        textViewInvestProperty1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mProperty));
        TextView textViewInvestYield1 = (TextView) view.findViewById(R.id.textViewInvestYield1);
        textViewInvestYield1.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position + 1).mYield));
        TextView textViewInvestKeyPoint1 = (TextView) view.findViewById(R.id.textViewInvestKeyPoint1);
        textViewInvestKeyPoint1.setText(String.format("%.2f", mInvestBeanList.get(2 * position + 1).mKeyPoint));
        TextView textViewInvestKeyRatio1 = (TextView) view.findViewById(R.id.textViewInvestKeyRatio1);
        textViewInvestKeyRatio1.setText(String.format("%.2f%%", mInvestBeanList.get(2 * position + 1).mKeyRatio));

        Button button = (Button) view.findViewById(R.id.buttonPagerInvest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOnClick(v, position);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public View getTabView(int position) {
        View view = mInflater.inflate(R.layout.item_tabs, null);
        TextView textView = (TextView) view.findViewById(R.id.textview_tabs);
        textView.setText(mTabTitles[position]);
        return view;
    }

    private void buttonOnClick(View v, int position) {
        Thread dft = new UpdateDataThread(position);
        dft.start();
    }

    class UpdateDataThread extends Thread {
        private int index;

        public UpdateDataThread(int idx) {
            index = idx;
        }

        @Override
        public void run() {
            String storageDir = Environment.getExternalStorageDirectory().toString();
            String filePath = storageDir + "/investor/data/W" + mTabTitles[index] + ".txt";
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    String urlStr = "http://hq.sinajs.cn/list=sz" + mTabCodes[index];
                    HttpUtility httpUtility = new HttpUtility();
                    String httpStr = httpUtility.getData(urlStr);
                    if (httpStr.equals("")) {
                        Looper.prepare();
                        Toast.makeText(mContext, "网络无连接！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else if (httpStr.contains("pv_none_match")) {
                        Looper.prepare();
                        Toast.makeText(mContext, "找不到对应的股票！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        String[] strs = httpStr.substring(httpStr.indexOf("\"") + 1, httpStr.lastIndexOf("\"")).split(",");
                        String dataStr = String.format("%s\t%.2f\t%.2f\t%.2f\t%.2f", strs[30].replace("-", "/"),
                                Double.parseDouble(strs[1]), Double.parseDouble(strs[4]), Double.parseDouble(strs[5]), Double.parseDouble(strs[3]));
                        PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                        pw.println(dataStr);
                        pw.close();
                        Looper.prepare();
                        Toast.makeText(mContext, "更新成功！", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                Looper.prepare();
                Toast.makeText(mContext, "请先建立基础文件", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    }
}
