package com.example.phuongtd.moolamoola.file;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by qs109 on 6/10/2016.
 */
public class HistoryItem extends RealmObject {
    @PrimaryKey
    private String id;
    private String filePath;
    private String folderSave;
    private String name;
    private String status;
    private Long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getFolderSave() {
        return folderSave;
    }

    public void setFolderSave(String folderSave) {
        this.folderSave = folderSave;
    }
}
