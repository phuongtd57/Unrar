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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DirectoryChooserFragment.OnFragmentInteractionListener {
    final int FILE_CODE = 26;
    private DirectoryChooserFragment mDialog;
    DirectoryChooserConfig config;
    Button btSelectRarFile;
    Button btSelectTargetFolder;
    Button btExtract;
    TextView tvFileSelect;
    TextView tvFolderTarget;
    boolean hasChooseFolder = false;
    String targetFolder = "/sdcard";
    Uri uriFile = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initChooseFolder();

    }

    private void initChooseFolder() {
        config = DirectoryChooserConfig.builder()
                .newDirectoryName("New Folder")
                .allowNewDirectoryNameModification(true)
                .initialDirectory(targetFolder)
                .build();
        mDialog = DirectoryChooserFragment.newInstance(config);
    }

    void initView() {
        btSelectRarFile = (Button) findViewById(R.id.btSelectRarFile);
        btSelectTargetFolder = (Button) findViewById(R.id.btSelectTargetFolder);
        tvFileSelect = (TextView) findViewById(R.id.tvFileSelect);
        tvFolderTarget = (TextView) findViewById(R.id.tvFolderTarget);
        btExtract = (Button) findViewById(R.id.btExtract);
        btSelectTargetFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show(getFragmentManager(), null);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btSelectRarFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, FILE_CODE);
            }
        });

        btExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uriFile.getPath().endsWith(".rar")) {
                    Toast.makeText(MainActivity.this, "Please choose .rar file", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!hasChooseFolder) {
                    Toast.makeText(MainActivity.this, "Please choose folder to save file", Toast.LENGTH_LONG).show();
                    return;
                }

                extractFile();
            }
        });
    }

    void extractFile() {
        //File dir = Environment.getExternalStorageDirectory();
        progressBar.setVisibility(View.VISIBLE);
        File f = new File(uriFile.getPath());
        Archive a = null;
        try {
            a = new Archive(f, "", false); // extract mode
            if (a.isPass()) {
                Log.d("phuongtd", "Pass word");
                a = new Archive(f, "123", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        HashSet<String> hashSet = new HashSet<>();

        if (a != null) {
            a.getMainHeader().print();
            FileHeader fh = a.nextFileHeader();
            String fileName = fh.getFileNameString().replace('\\', '/');
            String folderName = null;
            if (fileName.contains("/")) {
                folderName = fileName.split("/")[0];
                String folderString = targetFolder + "/" + folderName;
                File folder = new File(folderString);
                if (!folder.exists()) {
                    folder.mkdir();
                }
            }

            while (fh != null) {
                Log.d("phuongtd", "Packname before: " + fh.getFileNameString());
                Log.d("phuongtd", "Packname after: " + fh.getFileNameString().replace('\\' , '/'));
                try {
                    if (folderName == null || (folderName != null && !fh.getFileNameString().equalsIgnoreCase(folderName))) {
                        fileName = fh.getFileNameString().replace('\\', '/');
                        String[] listFolder = fileName.split("/");
                        String longName = listFolder[0];
                        hashSet.add(longName);
                        for (int i = 1; i < listFolder.length - 1; i++) {
                            String s = listFolder[i];
                            longName = longName + "/" + s;
                            hashSet.add(longName);
                            File folder = new File(targetFolder + "/" + longName);
                            if (!folder.exists()) {
                                folder.mkdir();
                            }
                        }
                        if (!hashSet.contains(fileName)) {
                            File out = new File("", targetFolder + "/" + fileName);
                            System.out.println(out.getAbsolutePath());
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fh = a.nextFileHeader();
            }
        }
        progressBar.setVisibility(View.GONE);
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
                            uriFile = uri;
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                            tvFileSelect.setText(uri.getPath());
                            uriFile = uri;
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                tvFileSelect.setText(uri.getPath());
                uriFile = uri;
            }
        }
    }

    @Override
    public void onSelectDirectory(String path) {
        mDialog.dismiss();
        targetFolder = path;
        initChooseFolder();
        tvFolderTarget.setText(targetFolder);
        hasChooseFolder = true;
    }

    @Override
    public void onCancelChooser() {
        mDialog.dismiss();
    }
}
