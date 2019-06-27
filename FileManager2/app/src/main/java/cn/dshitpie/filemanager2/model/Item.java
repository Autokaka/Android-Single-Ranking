package cn.dshitpie.filemanager2.model;

import java.io.File;

public class Item {
    private int imgResId;
    private String itemName;
    private File itemFile;

    public Item(int imgResId, String itemName, File itemFile) {
        setImgResId(imgResId);
        setItemName(itemName);
        setItemFile(itemFile);
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemFile(File itemFile) {
        this.itemFile = itemFile;
    }

    public File getItemFile() {
        return itemFile;
    }
}
