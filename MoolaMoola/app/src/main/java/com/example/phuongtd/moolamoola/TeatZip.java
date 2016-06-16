package com.example.phuongtd.moolamoola;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.util.List;

/**
 * Created by qs109 on 6/15/2016.
 */
public class TeatZip {
    public static void main(String[] args) throws Exception {
        ZipFile zipFile = new ZipFile("C:/Users/qs109/Desktop/gggg.zip");

        // Get the list of file headers from the zip file
        List fileHeaderList = zipFile.getFileHeaders();

        // Loop through the file headers
        for (int i = 0; i < fileHeaderList.size(); i++) {
            FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
            String fileName = fileHeader.getFileName();
            System.out.println(fileName);
            zipFile.extractFile(fileHeader, "C:/Users/qs109/Desktop");


        }
    }
}
