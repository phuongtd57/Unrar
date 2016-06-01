package com.example.phuongtd.moolamoola;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.nononsenseapps.filepicker.FilePickerActivity;
import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements DirectoryChooserFragment.OnFragmentInteractionListener
{
    final int FILE_CODE = 26;
    private DirectoryChooserFragment mDialog;
    DirectoryChooserConfig config;
    Button btSelectRarFile;
    Button btSelectTargetFolder;
    TextView tvFileSelect;
    TextView tvFolderTarget;

    String targetFolder = "/sdcard";
    String urlFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initChooseFolder();

    }

    private void initChooseFolder()
    {
        config = DirectoryChooserConfig.builder()
                 .newDirectoryName("New Folder")
                 .allowNewDirectoryNameModification(true)
                 .initialDirectory(targetFolder)
                 .build();
        mDialog = DirectoryChooserFragment.newInstance(config);
    }

    void initView(){
        btSelectRarFile = (Button) findViewById(R.id.btSelectRarFile);
        btSelectTargetFolder = (Button) findViewById(R.id.btSelectTargetFolder);
        tvFileSelect = (TextView) findViewById(R.id.tvFileSelect);
        tvFolderTarget = (TextView) findViewById(R.id.tvFolderTarget);

        btSelectTargetFolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.show(getFragmentManager(), null);
            }
        });

        btSelectRarFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);

                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, FILE_CODE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            tvFileSelect.setText(uri.getPath());
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path: paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                            tvFileSelect.setText(uri.getPath());
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                tvFileSelect.setText(uri.getPath());
            }
        }
    }

    @Override
    public void onSelectDirectory(String path)
    {
        mDialog.dismiss();
        targetFolder = path;
        initChooseFolder();
        tvFolderTarget.setText(targetFolder);
    }

    @Override
    public void onCancelChooser()
    {
        mDialog.dismiss();
    }
}
