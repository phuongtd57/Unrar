package com.example.phuongtd.moolamoola.fileExplore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.HistoryItem;

import java.util.List;

import io.realm.Realm;

/**
 * Created by qs109 on 6/10/2016.
 */
public class ExtractHistory extends AppCompatActivity {
    ListView listHistory;
    TextView tvCancel;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.history_activity);

        initView();

        realm = Realm.getInstance(ExtractHistory.this);
        List<HistoryItem> historyItemList = realm.where(HistoryItem.class).findAll();
        HistoryAdapter historyAdapter = new HistoryAdapter(ExtractHistory.this, historyItemList);

        listHistory.setAdapter(historyAdapter);

    }

    private void initView() {
        listHistory = (ListView) findViewById(R.id.listView);
        tvCancel = (TextView) findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
