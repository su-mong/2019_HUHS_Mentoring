package com.candykick.huhs2ndmentoring.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.candykick.huhs2ndmentoring.R;

import java.util.ArrayList;

/**
 * Created by candykick on 2019. 8. 28..
 */

public class MapListAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<MapData> listData = new ArrayList<>();

    public MapListAdapter(Context context, ArrayList<MapData> listData) {
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
        if (view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.raw_listview_map, parent, false);

            holder.tvName = view.findViewById(R.id.tvRawMapName);
            holder.tvAddress = view.findViewById(R.id.tvRawMapAddress);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        MapData data = listData.get(position);

        holder.tvName.setText(data.place_name);
        holder.tvAddress.setText(data.address_name);

        return view;
    }


    public class ViewHolder {
        public TextView tvName;
        public TextView tvAddress;
    }
}
