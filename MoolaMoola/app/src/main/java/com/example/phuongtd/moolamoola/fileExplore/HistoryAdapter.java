package com.example.phuongtd.moolamoola.fileExplore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.HistoryItem;
import com.example.phuongtd.moolamoola.file.HistoryItemView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by qs109 on 6/10/2016.
 */
public class HistoryAdapter extends BaseAdapter {
    Context context;
    List<HistoryItemView> extractHistories;

    public HistoryAdapter(Context context, List<HistoryItemView> extractHistoryList) {
        this.context = context;
        this.extractHistories = extractHistoryList;
    }

    @Override
    public int getCount() {
        return extractHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return extractHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        }
        TextView tvPath = (TextView) convertView.findViewById(R.id.tvFilePath);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        HistoryItemView extractHistory = extractHistories.get(position);
        tvPath.setText(extractHistory.getFilePath());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm - dd MMM");
        Date date = new Date(extractHistory.getTime());
        tvTime.setText(simpleDateFormat.format(date));
        return convertView;
    }
}
