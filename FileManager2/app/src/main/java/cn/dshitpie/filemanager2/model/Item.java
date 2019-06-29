package cn.dshitpie.filemanager2.model;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import java.io.File;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.utils.AppManager;

public class Item {
    private int imgResId;
    private String itemName;
    private File itemFile;
    private int textColor;
    private Typeface typeface;

    public Item(int imgResId, String itemName, File itemFile) {
        setImgResId(imgResId);
        setItemName(itemName);
        setItemFile(itemFile);
        setTextColor(R.color.colorAccent);
        setTypeface(Typeface.DEFAULT);
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

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = ContextCompat.getColor(AppManager.getContext(), textColor);
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
}
