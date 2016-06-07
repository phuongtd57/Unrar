package com.example.phuongtd.moolamoola.file;

/**
 * Created by qs109 on 6/7/2016.
 */
public class FileObject
{
    boolean isBackFolder;
    private String path;
    private FileObject parent;
    private boolean isFile;
    private String extension;
    private String volume;
    private String name;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public FileObject getParent()
    {
        return parent;
    }

    public void setParent(FileObject parent)
    {
        this.parent = parent;
    }

    public void setIsFile(boolean isFile)
    {
        this.isFile = isFile;
    }

    public String getExtension()
    {
        return extension;
    }

    public void setExtension(String extension)
    {
        this.extension = extension;
    }

    public String getVolume()
    {
        return volume;
    }

    public void setVolume(String volume)
    {
        this.volume = volume;
    }

    public boolean isBackFolder() {
        return isBackFolder;
    }

    public void setIsBackFolder(boolean isBackFolder) {
        this.isBackFolder = isBackFolder;
    }

    public boolean isFile() {
        return isFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
