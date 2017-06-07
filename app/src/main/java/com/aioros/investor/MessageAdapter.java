package com.aioros.investor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private List<MessageBean> mMsgBeanList = null;
    private Context mContext;
    private LayoutInflater mInflater;

    public MessageAdapter(List<MessageBean> msgBeanList, Context context) {
        mMsgBeanList = msgBeanList;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMsgBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMsgBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.message_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.img_msg_item);
        imageView.setImageResource(mMsgBeanList.get(position).getPhotoDrawableId());

        TextView nameMsg = (TextView) view.findViewById(R.id.name_msg_item);
        nameMsg.setText(mMsgBeanList.get(position).getMessageName());

        TextView contentMsg = (TextView) view.findViewById(R.id.content_msg_item);
        contentMsg.setText(mMsgBeanList.get(position).getMessageContent());

        TextView timeMsg = (TextView) view.findViewById(R.id.time_msg_item);
        timeMsg.setText(mMsgBeanList.get(position).getMessageTime());

        return view;
    }
}
