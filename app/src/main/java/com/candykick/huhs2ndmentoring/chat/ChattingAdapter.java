package com.candykick.huhs2ndmentoring.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.candykick.huhs2ndmentoring.R;

import java.util.ArrayList;

/**
 * Created by candykick on 2019. 8. 31..
 */

public class ChattingAdapter extends BaseAdapter {

    Context context;
    String myName;
    ArrayList<ChattingData> arrayList = new ArrayList<>();

    public ChattingAdapter(Context context, String myName) {
        this.myName = myName;
        this.context = context;
    }

    //아래는 BaseAdapter 상속받은 필수요소.
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view; ViewHolder holder;
        ChattingData data = arrayList.get(position);

        view = convertView;

        if(view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rawchatting, parent, false);

            holder.llChatReceiver = view.findViewById(R.id.llChatReceiver);
            holder.llChatMine = view.findViewById(R.id.llChatMine);
            holder.tvName = view.findViewById(R.id.tvNameChattingRaw);
            holder.tvMessage = view.findViewById(R.id.tvChatChattingRaw);
            holder.tvMessageMine = view.findViewById(R.id.tvChatChatMineRaw);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        if(myName == data.name) { //내가 작성한 메세지인 경우
            holder.llChatMine.setVisibility(View.VISIBLE);
            holder.llChatReceiver.setVisibility(View.GONE);
            holder.tvMessageMine.setText(data.message);
        } else { //상대방이 작성한 메세지인 경우
            holder.llChatMine.setVisibility(View.GONE);
            holder.llChatReceiver.setVisibility(View.VISIBLE);
            holder.tvName.setText(data.name);
            holder.tvMessage.setText(data.message);
        }

        return view;
    }

    public void addData(ChattingData data) {
        arrayList.add(data);
        notifyDataSetChanged();
    }
    public void removeData(ChattingData data) {
        arrayList.remove(data);
    }

    public class ViewHolder {
        public LinearLayout llChatReceiver;
        public LinearLayout llChatMine;
        public TextView tvName;
        public TextView tvMessage;
        public TextView tvMessageMine;
    }
}
