package com.example.phuongtd.moolamoola.file;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by qs109 on 6/7/2016.
 */
public class FileUtils {
    public static List<FileObject> getChildFiles(FileObject parent) {
        List<FileObject> child = new ArrayList<>();
        File parentFile = new File(parent.getPath());
        if (!parentFile.exists()) {
            return null;
        }

        File[] files = parentFile.listFiles();
        for (File file : files) {
            FileObject childObject = new FileObject();
            childObject.setParent(parent);
            childObject.setIsFile(file.isFile());
            childObject.setPath(file.getPath());
            childObject.setIsBackFolder(false);
            childObject.setName(file.getName());
            if (file.isFile()) {
                childObject.setVolume(FileUtils.getFileLength(file));
            }
            if (!file.isFile()) {
                if (file.listFiles()!= null && file.listFiles().length > 0) {
                    child.add(childObject);
                }
            } else if (childObject.getName().endsWith(".rar")) {
                child.add(childObject);
            }
        }
        Collections.sort(child, new Comparator<FileObject>() {
            @Override
            public int compare(FileObject o1, FileObject o2) {
                if (!o1.isFile() && !o2.isFile()) {
                    String name1 = o1.getName().toLowerCase();
                    String name2 = o2.getName().toLowerCase();
                    return name1.compareTo(name2);
                } else if (o1.isFile() && o2.isFile()) {
                    String name1 = o1.getName().toLowerCase();
                    String name2 = o2.getName().toLowerCase();
                    return name1.compareTo(name2);
                } else if (!o1.isFile() && o2.isFile()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return child;

    }

    public static String getFileLength(File file) {
        if (file.exists()) {
            double bytes = file.length();
            double kilobytes = (bytes / 1024);
            double megabytes = (kilobytes / 1024);
            double gigabytes = (megabytes / 1024);

            if (bytes < 1024) {
                return bytes + " bytes";
            } else if (kilobytes < 1024) {
                return new BigDecimal(kilobytes).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " kb";
            } else if (megabytes < 1024) {
                return new BigDecimal(megabytes).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " mb";
            } else {
                return new BigDecimal(gigabytes).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " gb";
            }
        }

        return "";

    }
}