package com.example.phuongtd.moolamoola.fileExplore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuongtd.moolamoola.MainActivity;
import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.HistoryItem;

import java.io.File;
import java.util.List;

import io.realm.Realm;

/**
 * Created by qs109 on 6/10/2016.
 */
public class HistoryActivity extends AppCompatActivity {
    ListView listHistory;
    TextView tvCancel;
    Realm realm;
    List<HistoryItem> historyItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.history_activity);

        initView();

        realm = Realm.getInstance(HistoryActivity.this);
        historyItemList = realm.where(HistoryItem.class).findAll();
        HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this, historyItemList);

        listHistory.setAdapter(historyAdapter);

        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem historyItem = historyItemList.get(position);
                File file = new File(historyItem.getFilePath());
                if (!file.exists()) {
                    Toast.makeText(HistoryActivity.this, "File not found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("FILE", historyItem.getFilePath());
                    setResult(MainActivity.MY_FILE_CODE, intent);
                    finish();
                }
            }
        });
    }

    private void initView() {
        listHistory = (ListView) findViewById(R.id.listView);
        tvCancel = (TextView) findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
