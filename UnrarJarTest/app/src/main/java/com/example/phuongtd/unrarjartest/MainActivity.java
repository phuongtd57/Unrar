package com.example.phuongtd.unrarjartest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File dir = Environment.getExternalStorageDirectory();
        File f = new File(dir, "/Download/text.rar");
        Archive a = null;
        try {
            a = new Archive(f, "", false); // extract mode
            if(a.isPass()){
                Log.d( "phuongtd" , "Pass word");
                a = new Archive(f, "123", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (a != null) {
            a.getMainHeader().print();
            FileHeader fh = a.nextFileHeader();
            while (fh != null) {
                try {
                    File out = new File(dir , "/Download/"
                            + fh.getFileNameString().trim());
                    System.out.println(out.getAbsolutePath());
                    FileOutputStream os = new FileOutputStream(out);
                    a.extractFile(fh, os);
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fh = a.nextFileHeader();
            }
        }
    }
}
