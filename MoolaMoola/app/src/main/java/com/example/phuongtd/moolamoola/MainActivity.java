package com.example.phuongtd.moolamoola;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.phuongtd.moolamoola.dialog.PasswordDialog;
import com.example.phuongtd.moolamoola.fileExplore.FileExploreActivity;
import com.nononsenseapps.filepicker.FilePickerActivity;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

public class MainActivity extends AppCompatActivity implements DirectoryChooserFragment.OnFragmentInteractionListener {
    final int FILE_CODE = 26;
    public static final int MY_FILE_CODE = 7;
    private DirectoryChooserFragment mDialog;
    DirectoryChooserConfig config;
    Button btSelectRarFile;
    Button btSelectTargetFolder;
    Button btExtract;
    EditText tvFileSelect;
    EditText tvFolderTarget;
    boolean hasChooseFolder = false;
    String targetFolder = "/sdcard";
    String uriFile = null;
    ProgressBar progressBar;
    String password = "";
    NumberProgressBar numberProgressBar;
    int numOfFile = 1;
    int currentExtractFile = 0;
    private final String FILE_HAVE_PASS = "File have password";

    File f;
    Archive a;
    FileHeader fh;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Window window = getWindow();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.status));
        }
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
        tvFileSelect = (EditText) findViewById(R.id.tvFileSelect);
        tvFolderTarget = (EditText) findViewById(R.id.tvFolderTarget);
        btExtract = (Button) findViewById(R.id.btExtract);
        numberProgressBar = (NumberProgressBar) findViewById(R.id.numberBar);
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
                Intent i = new Intent(MainActivity.this, FileExploreActivity.class);
                startActivityForResult(i, MY_FILE_CODE);
            }
        });

        btExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uriFile.endsWith(".rar")) {
                    Toast.makeText(MainActivity.this, "Please choose .rar file", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!hasChooseFolder) {
                    Toast.makeText(MainActivity.this, "Please choose folder to save file", Toast.LENGTH_LONG).show();
                    return;
                }

                new MyAsync().execute();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_FILE_CODE) {
            if (data!=null && data.hasExtra("FILE")) {
                String uri = data.getStringExtra("FILE");
                tvFileSelect.setText(uri);
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

    public class MyAsync extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            //File dir = Environment.getExternalStorageDirectory();
            progressBar.setVisibility(View.VISIBLE);
            numberProgressBar.setVisibility(View.VISIBLE);
            f = new File(uriFile);
            a = null;
            try {
                a = new Archive(f, password, false); // extract mode
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentExtractFile = 0;
            numOfFile = a.getFileHeaders().size();
            numberProgressBar.setMax(numOfFile);
            numberProgressBar.setProgress(currentExtractFile);
        }

        @Override
        protected String doInBackground(Void... params) {
            HashSet<String> hashSet = new HashSet<>();
            if (a != null) {
                a.getMainHeader().print();
                FileHeader fh = a.nextFileHeader();
                String fileName = fh.getFileNameString().replace('\\', '/');
                String tempFile = targetFolder + "/" + "temp";

                try {
                    FileOutputStream os = new FileOutputStream(tempFile);
                    a.extractFile(fh, os);
                    os.close();
                } catch (RarException e) {
                    if (e.getMessage().equalsIgnoreCase("crcError")) {
                        return FILE_HAVE_PASS;
                    }
                } catch (Exception e) {
                    return "Cant extract this file, " + e.getMessage();
                } finally {
                    File fileTemp = new File(tempFile);
                    if (fileTemp.exists()) {
                        fileTemp.delete();
                    }
                }

                // caculator sum block

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
                    currentExtractFile++;
                    Log.d("phuongtd", "Packname before: " + fh.getFileNameString());
                    Log.d("phuongtd", "Packname after: " + fh.getFileNameString().replace('\\', '/'));
                    try {
                        if (folderName == null || (folderName != null && !fh.getFileNameString().equalsIgnoreCase(folderName))) {
                            fileName = fh.getFileNameString().replace('\\', '/');
                            String[] listFolder = fileName.split("/");
                            String longName = listFolder[0];
                            //hashSet.add(longName);
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
                        return "Cant extract this file, " + e.getMessage();
                    }
                    fh = a.nextFileHeader();
                    publishProgress(currentExtractFile);
                }
                return "";
            } else {
                return "Cant extract this file ";
            }
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid.equalsIgnoreCase("")) {
                Toast.makeText(MainActivity.this, "Extract successfully", Toast.LENGTH_SHORT).show();
            } else if (aVoid.equalsIgnoreCase(FILE_HAVE_PASS)) {
                PasswordDialog passwordDialog = new PasswordDialog(MainActivity.this, new PasswordDialog.ActionImpl() {
                    @Override
                    public void execute(String pass) {
                        password = pass;
                        new MyAsync().execute();
                    }
                });
                passwordDialog.show();
            } else {
                Toast.makeText(MainActivity.this, aVoid, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            numberProgressBar.setProgress(values[0]);
        }
    }
}
