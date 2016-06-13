package com.example.phuongtd.moolamoola.fileExplore;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuongtd.moolamoola.MainActivity;
import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.FileObject;
import com.example.phuongtd.moolamoola.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by qs109 on 6/13/2016.
 */
public class SearchActivity extends AppCompatActivity {
    ListView listView;
    ProgressBar progressBar;
    TextView tvCancel;
    Queue<File> files = new PriorityQueue();
    SearchAdapter searchAdapter;
    List<FileObject> fileObjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        getSupportActionBar().hide();
        initView();
        searchAdapter = new SearchAdapter(fileObjectList, SearchActivity.this);
        listView.setAdapter(searchAdapter);
        new MyAsync().execute();
      /*  File root = Environment.getExternalStorageDirectory();
        File[] files = root.listFiles(new AudioFileFilter());*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("FILE", fileObjectList.get(position).getPath());
                setResult(MainActivity.MY_FILE_CODE, intent);
                finish();
            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvCancel = (TextView) findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public class MyAsync extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File root = new File(Environment.getExternalStorageDirectory().getPath());
            files.add(root);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!files.isEmpty()) {
                File file = files.poll();
                if (file.isFile() && file.getPath().endsWith(".rar")) {
                    publishProgress(file.getPath());
                } else if (!file.isFile() && file.listFiles().length > 0) {
                    for (File file1 : file.listFiles()) {
                        files.add(file1);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            File file = new File(values[0]);
            FileObject fileObject = new FileObject();
            fileObject.setPath(file.getPath());
            fileObject.setVolume(FileUtils.getFileLength(file));
            fileObjectList.add(fileObject);
            searchAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            if (fileObjectList.size() == 0) {
                Toast.makeText(SearchActivity.this, "Cant find any rar file", Toast.LENGTH_LONG).show();
            }
        }
    }
}
