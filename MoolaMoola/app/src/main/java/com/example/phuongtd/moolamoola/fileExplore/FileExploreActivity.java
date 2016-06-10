package com.example.phuongtd.moolamoola.fileExplore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phuongtd.moolamoola.MainActivity;
import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.FileObject;
import com.example.phuongtd.moolamoola.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by qs109 on 6/7/2016.
 */
public class FileExploreActivity extends AppCompatActivity {
    ListView listView;
    TextView tvCancel;
    TextView tvCurrentFolder;
    EditText tvSearch;
    List<FileObject> fileObjectList = new ArrayList<>();
    List<FileObject> fileObjectListBackUp = new ArrayList<>();

    ListFileAdapter fileAdapter;
    FileObject rootFileObject;
    FileObject back;
    FileObject folderSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_picker_activity);
        getSupportActionBar().hide();
        initView();

        File rootFile = Environment.getExternalStorageDirectory();
        rootFileObject = new FileObject();
        rootFileObject.setPath(rootFile.getPath());
        folderSelect = rootFileObject;
        tvCurrentFolder.setText(rootFileObject.getPath());
        back = new FileObject();
        back.setIsBackFolder(true);

        fileObjectList.add(back);
        fileObjectList.addAll(FileUtils.getChildFiles(rootFileObject));
        fileObjectListBackUp.clear();
        fileObjectListBackUp.addAll(fileObjectList);

        fileAdapter = new ListFileAdapter(FileExploreActivity.this, fileObjectList);
        listView.setAdapter(fileAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileObject selectFolder = fileObjectList.get(position);
                if (selectFolder.isBackFolder()) {
                    back();
                } else if (selectFolder.isFile()) {
                    Intent intent = new Intent();
                    intent.putExtra("FILE", selectFolder.getPath());
                    setResult(MainActivity.MY_FILE_CODE, intent);
                    finish();
                } else {
                    folderSelect = selectFolder;
                    tvCurrentFolder.setText(selectFolder.getPath());
                    fileObjectList.clear();
                    fileObjectList.add(back);
                    fileObjectList.addAll(FileUtils.getChildFiles(selectFolder));
                    fileObjectListBackUp.clear();
                    fileObjectListBackUp.addAll(fileObjectList);
                    fileAdapter.notifyDataSetChanged();
                    tvSearch.setText("");
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fileObjectList.clear();
                if (s.toString() == null || s.toString().length() == 0) {
                    fileObjectList.addAll(fileObjectListBackUp);
                } else {
                    for (FileObject fileObject : fileObjectListBackUp) {
                        if (fileObject.isBackFolder()) {
                            fileObjectList.add(fileObject);
                        } else if (fileObject.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            fileObjectList.add(fileObject);
                        }
                    }
                }
                fileAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        tvSearch = (EditText) findViewById(R.id.tvSearch);
        listView = (ListView) findViewById(R.id.listView);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvCurrentFolder = (TextView) findViewById(R.id.tvCurrentFolder);
    }

    public void back() {
        if (folderSelect.getPath().equals("/")) {
            super.onBackPressed();
        } else {
            backAndInitView();
        }
    }

    private void backAndInitView() {
        FileObject parent = folderSelect.getParent();
        if (parent == null) {
            File parentFile = new File(folderSelect.getPath()).getParentFile();
            parent = new FileObject();
            parent.setPath(parentFile.getPath());
        }
        fileObjectList.clear();
        fileObjectList.add(back);
        fileObjectList.addAll(FileUtils.getChildFiles(parent));
        fileObjectListBackUp.clear();
        fileObjectListBackUp.addAll(fileObjectList);
        fileAdapter.notifyDataSetChanged();
        folderSelect = parent;
        tvCurrentFolder.setText(folderSelect.getPath());
        tvSearch.setText("");
    }

    @Override
    public void onBackPressed() {
        back();
    }

}
