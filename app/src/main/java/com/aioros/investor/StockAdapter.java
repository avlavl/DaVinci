package com.aioros.investor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StockAdapter extends BaseAdapter {
    private List<StockBean> stockBeanList = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public StockAdapter(List<StockBean> msgBeanList, Context context) {
        stockBeanList = msgBeanList;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stockBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return stockBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.stock_item, null);

        ImageView image = (ImageView) view.findViewById(R.id.image_stock_item);
        image.setImageResource(stockBeanList.get(position).getPhotoId());

        TextView name = (TextView) view.findViewById(R.id.name_stock_item);
        name.setText(stockBeanList.get(position).getStockName());

        TextView code = (TextView) view.findViewById(R.id.code_stock_item);
        code.setText(stockBeanList.get(position).getStockCode());

        TextView value = (TextView) view.findViewById(R.id.value_stock_item);
        value.setText(stockBeanList.get(position).getStockValue());

        TextView ratio = (TextView) view.findViewById(R.id.ratio_stock_item);
        ratio.setText(stockBeanList.get(position).getStockRatio());

        TextView scope = (TextView) view.findViewById(R.id.scope_stock_item);
        scope.setText(stockBeanList.get(position).getStockScope());

        return view;
    }
}
