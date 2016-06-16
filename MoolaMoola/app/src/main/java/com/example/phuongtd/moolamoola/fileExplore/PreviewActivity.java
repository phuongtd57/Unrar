package com.example.phuongtd.moolamoola.fileExplore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phuongtd.moolamoola.R;
import com.example.phuongtd.moolamoola.file.FileObject;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * Created by qs109 on 6/9/2016.
 */
public class PreviewActivity extends AppCompatActivity {
    ListView listView;
    TextView btCancel;
    TextView tvLocalFolder;
    TextView tvCancel;
    ProgressBar progressBar;
    String filePath;
    List<FileHeader> fileHeaders = new ArrayList<>();
    List<FileObject> fileObjects = new ArrayList<>();
    List<net.lingala.zip4j.model.FileHeader> fileHeadersZips = new ArrayList<>();
    FilePreviewAdapter filePreviewAdapter;
    Set<String> hashSet;
    FileObject root;
    FileObject master;
    FileObject current;
    Archive archive;
    FileHeader fileHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);
        getSupportActionBar().hide();
        initView();
        filePath = getIntent().getExtras().getString("file");
        if (filePath.endsWith(".rar")) {
            previewRar();
        } else {
            previewZip();
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void previewZip() {
        new MyAsyncZip().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileObject fileObject = fileObjects.get(position);
                if (!fileObject.isFile()) {
                    if (fileObject.getChilds() != null) {
                        fileObjects.clear();
                        fileObjects.addAll(fileObject.getChilds());
                        filePreviewAdapter.notifyDataSetChanged();
                    } else {
                        fileObject.setChilds(new ArrayList<FileObject>());
                        for (net.lingala.zip4j.model.FileHeader header : fileHeadersZips) {
                            String path = header.getFileName().replace('\\', '/');
                            if (path.startsWith(fileObject.getPath()) && !path.equals(fileObject.getPath())) {
                                String temp = path.substring(fileObject.getPath().length());
                                if (!temp.contains("/") || temp.endsWith("/")) {
                                    FileObject c = new FileObject();
                                    if (hashSet.contains(path.substring(0, path.length() - 1))) {
                                        c.setIsFile(false);
                                    } else {
                                        c.setIsFile(true);
                                    }
                                    c.setParent(fileObject);
                                    c.setPath(path);
                                    String tempName = path.substring(fileObject.getPath().length());
                                    if (tempName.contains("/")) {
                                        c.setName(tempName.substring(0, tempName.length() - 1));
                                    } else {
                                        c.setName(tempName);
                                    }
                                    fileObject.getChilds().add(c);
                                }
                            }
                        }
                        current = fileObject;
                        fileObjects.clear();
                        fileObjects.addAll(fileObject.getChilds());
                        filePreviewAdapter.notifyDataSetChanged();
                        tvLocalFolder.setText(fileObject.getPath());
                    }
                }
            }

        });
    }


    private void previewRar() {
        new MyAsyncInit().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileObject fileObject = fileObjects.get(position);
                if (!fileObject.isFile()) {
                    if (fileObject.getChilds() != null) {
                        fileObjects.clear();
                        fileObjects.addAll(fileObject.getChilds());
                        filePreviewAdapter.notifyDataSetChanged();
                    } else {
                        fileObject.setChilds(new ArrayList<FileObject>());
                        for (FileHeader header : fileHeaders) {
                            String path = header.getFileNameString().replace('\\', '/');
                            if (path.startsWith(fileObject.getPath()) && !path.equals(fileObject.getPath())) {
                                if (!path.substring(fileObject.getPath().length() + 1).contains("/")) {
                                    FileObject c = new FileObject();
                                    if (hashSet.contains(path)) {
                                        c.setIsFile(false);
                                    } else {
                                        c.setIsFile(true);
                                    }
                                    c.setParent(fileObject);
                                    c.setPath(path);
                                    c.setName(path.substring(fileObject.getPath().length() + 1));
                                    fileObject.getChilds().add(c);
                                }

                            }
                        }
                        current = fileObject;
                        fileObjects.clear();
                        fileObjects.addAll(fileObject.getChilds());
                        filePreviewAdapter.notifyDataSetChanged();
                        tvLocalFolder.setText(fileObject.getPath());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (current.getPath().equalsIgnoreCase(master.getPath())) {
            super.onBackPressed();
        } else {
            FileObject parent = current.getParent();
            fileObjects.clear();
            fileObjects.addAll(parent.getChilds());
            filePreviewAdapter.notifyDataSetChanged();
            current = current.getParent();
            tvLocalFolder.setText(current.getPath());
        }
    }

    private void initView() {
        tvLocalFolder = (TextView) findViewById(R.id.tvLocalFolder);
        listView = (ListView) findViewById(R.id.listView);
        btCancel = (TextView) findViewById(R.id.tvCancel);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    public class MyAsyncZip extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fileObjects.clear();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                File file = new File(filePath);
                ZipFile zipFile = new ZipFile(filePath);
                List fileHeaderList = zipFile.getFileHeaders();

                root = new FileObject();
                master = new FileObject();
                master.setChilds(new ArrayList<FileObject>());
                master.getChilds().add(root);
                master.setPath(file.getName());
                root.setParent(master);

                fileHeadersZips.clear();
                hashSet = new HashSet<>();

                for (int i = 0; i < fileHeaderList.size(); i++) {
                    net.lingala.zip4j.model.FileHeader fileHeaderZip = (net.lingala.zip4j.model.FileHeader) fileHeaderList.get(i);
                    fileHeadersZips.add(fileHeaderZip);
                    String fileName = fileHeaderZip.getFileName().replace('\\', '/');
                    if (fileName.endsWith("/")) {
                        String[] listFolder = fileName.split("/");
                        String longName = listFolder[0];
                        hashSet.add(longName);
                        for (int j = 1; j < listFolder.length; j++) {
                            String s = listFolder[j];
                            longName = longName + "/" + s;
                            hashSet.add(longName);
                        }
                    }
                }

                if (fileHeadersZips.size() == 1) {
                    root.setIsFile(true);
                    root.setName(fileHeadersZips.get(0).getFileName());
                    root.setPath(fileHeadersZips.get(0).getFileName());
                    fileObjects.addAll(master.getChilds());
                } else {
                    root.setIsFile(false);
                    for (net.lingala.zip4j.model.FileHeader fh : fileHeadersZips) {
                        String fullName = fh.getFileName().replace('\\', '/');
                        if (!fullName.substring(0, fullName.length() - 1).contains("/")) {
                            root.setName(fullName.substring(0, fullName.length() - 1));
                            root.setPath(fullName);
                            break;
                        }
                    }
                    fileObjects.addAll(master.getChilds());
                }
                current = master;
            } catch (ZipException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            filePreviewAdapter = new FilePreviewAdapter(PreviewActivity.this, fileObjects);
            tvLocalFolder.setText(master.getPath());
            listView.setAdapter(filePreviewAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

    public class MyAsyncInit extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fileObjects.clear();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            File file = new File(filePath);
            try {
                archive = new Archive(file, "", false);
                fileHeader = archive.nextFileHeader();
                root = new FileObject();
                master = new FileObject();
                master.setChilds(new ArrayList<FileObject>());
                master.getChilds().add(root);
                master.setPath(file.getName());
                root.setParent(master);
                fileHeaders.clear();
                hashSet = new HashSet<>();
                while (fileHeader != null) {
                    fileHeaders.add(fileHeader);
                    String fileName = fileHeader.getFileNameString().replace('\\', '/');
                    String[] listFolder = fileName.split("/");
                    String longName = listFolder[0];
                    hashSet.add(longName);
                    for (int i = 1; i < listFolder.length - 1; i++) {
                        String s = listFolder[i];
                        longName = longName + "/" + s;
                        hashSet.add(longName);
                    }
                    fileHeader = archive.nextFileHeader();
                }
                if (fileHeaders.size() == 1) {
                    root.setIsFile(true);
                    root.setName(fileHeaders.get(0).getFileNameString());
                    root.setPath(fileHeaders.get(0).getFileNameString());
                    fileObjects.addAll(master.getChilds());
                } else {
                    root.setIsFile(false);
                    for (FileHeader fh : fileHeaders) {
                        if (!fh.getFileNameString().replace('\\', '/').contains("/")) {
                            root.setName(fh.getFileNameString());
                            root.setPath(fh.getFileNameString().replace('\\', '/'));
                            break;
                        }
                    }
                    fileObjects.addAll(master.getChilds());
                }
                current = master;
            } catch (RarException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            filePreviewAdapter = new FilePreviewAdapter(PreviewActivity.this, fileObjects);
            tvLocalFolder.setText(master.getPath());
            listView.setAdapter(filePreviewAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

}
