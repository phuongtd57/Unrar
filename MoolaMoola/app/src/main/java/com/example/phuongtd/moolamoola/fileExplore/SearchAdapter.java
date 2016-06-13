package com.example.phuongtd.moolamoola.fileExplore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.FileObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by qs109 on 6/13/2016.
 */
public class SearchAdapter extends BaseAdapter {
    private List<FileObject> fileObjectList;
    Context context;

    SearchAdapter(List<FileObject> fileObjects, Context context) {
        this.fileObjectList = fileObjects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fileObjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileObjectList.get(position);
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
        TextView tvName = (TextView) convertView.findViewById(R.id.tvFilePath);
        TextView tvFileSize = (TextView) convertView.findViewById(R.id.tvTime);
        FileObject fileObject = fileObjectList.get(position);
        tvName.setText(fileObject.getPath());
        tvFileSize.setText(fileObject.getVolume());


        return convertView;
    }
}
