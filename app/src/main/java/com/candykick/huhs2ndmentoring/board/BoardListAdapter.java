package com.candykick.huhs2ndmentoring.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.candykick.huhs2ndmentoring.R;

import java.util.ArrayList;

/**
 * Created by candykick on 2019. 9. 4..
 */

public class BoardListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<BoardData> listData = new ArrayList<>();

    public BoardListAdapter(Context context, ArrayList<BoardData> listData) {
        super();
        this.context = context;
        this.listData = listData;
    }

    //아래는 BaseAdapter 상속받은 필수요소.
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if(view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rawboard, parent, false);

            holder.tvTitle = view.findViewById(R.id.tvBoardTitle);
            holder.tvContents = view.findViewById(R.id.tvBoardContents);

            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        BoardData data = listData.get(position);

        if(data.title.length() > 17) {
            holder.tvTitle.setText(data.title.substring(0, 17)+"...");
        } else {
            holder.tvTitle.setText(data.title);
        }

        String contents = data.contents.replace("\n", " ");
        if(contents.length() > 23) {
            holder.tvContents.setText(contents.substring(0, 23)+"...");
        } else {
            holder.tvContents.setText(contents);
        }

        return view;
    }


    public class ViewHolder {
        public TextView tvTitle;
        public TextView tvContents;
    }
}
