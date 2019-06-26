package cn.dshitpie.filemanager.view;

import java.io.File;

public class RecyclerViewItem {
    private String itemName;
    private int imageId;
    private File itemFile;

    public RecyclerViewItem(int imageId, String itemName, File itemFile) {
        setImageId(imageId);
        setItemName(itemName);
        setItemFile(itemFile);
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
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
