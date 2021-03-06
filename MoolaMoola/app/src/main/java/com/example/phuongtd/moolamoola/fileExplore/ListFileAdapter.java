package com.example.phuongtd.moolamoola.fileExplore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.FileObject;

import java.util.List;

/**
 * Created by qs109 on 6/7/2016.
 */
public class ListFileAdapter extends BaseAdapter {

    Context context;
    List<FileObject> fileObjects;

    public ListFileAdapter(Context context, List<FileObject> fileObjectList) {
        this.context = context;
        this.fileObjects = fileObjectList;
    }

    public static class ViewHolder {
        ImageView ivIcon;
        TextView tvFolderName;
        TextView tvFileSize;
    }

    @Override
    public int getCount() {
        return fileObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return fileObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.file_item, parent, false);
            vh = new ViewHolder();
            vh.ivIcon = (ImageView) convertView.findViewById(R.id.imgFolderIcon);
            vh.tvFolderName = (TextView) convertView.findViewById(R.id.tvFolderName);
            vh.tvFileSize = (TextView) convertView.findViewById(R.id.tvSize);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        FileObject fileObject = fileObjects.get(position);

        if (fileObject.isBackFolder()) {
            vh.tvFileSize.setVisibility(View.GONE);
            vh.tvFolderName.setText("...");
            vh.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.upload));
        } else if (fileObject.isFile()) {
            vh.tvFileSize.setVisibility(View.VISIBLE);
            vh.tvFileSize.setText(fileObject.getVolume());
            vh.tvFolderName.setText(fileObject.getName());
            vh.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.rar));
        } else {
            vh.tvFileSize.setVisibility(View.GONE);
            vh.tvFolderName.setText(fileObject.getName());
            vh.ivIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.folder_icon));
        }
        return convertView;
    }
}
