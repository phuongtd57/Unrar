package com.example.phuongtd.moolamoola.file;

/**
 * Created by qs109 on 6/16/2016.
 */
public class HistoryItemView {
    private String id;
    private String filePath;
    private String folderSave;
    private String name;
    private String status;
    private Long time;

    public HistoryItemView(HistoryItem item) {
        this.id = item.getId();
        this.filePath = item.getFilePath();
        this.folderSave = item.getFolderSave();
        this.name = item.getName();
        this.status = item.getStatus();
        this.time = item.getTime();
    }

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

    public String getFolderSave() {
        return folderSave;
    }

    public void setFolderSave(String folderSave) {
        this.folderSave = folderSave;
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
}
