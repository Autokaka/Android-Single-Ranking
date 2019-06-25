package cn.dshitpie.filemanager.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class FileManager {
    private static final String TAG = "testapp";

    public File getSdCard0Directory() {
        return Environment.getExternalStorageDirectory();
    }

    public File[] sortByType(File fileList[]) {
        //类型分离
        ArrayList<File> directory = new ArrayList<>(), file = new ArrayList<>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) directory.add(fileList[i]);
            else file.add(fileList[i]);
        }
        //单个类型排序
        SortByInitialComparator sortByInitialComparator = new SortByInitialComparator();
        Collections.sort(directory, sortByInitialComparator);
        Collections.sort(file, sortByInitialComparator);
        directory.addAll(file);
        for (int i = 0; i < fileList.length; i++) fileList[i] = directory.get(i);
        return fileList;
    }

    public boolean newFileIn(File parentFile) {
        if (!parentFile.isDirectory()) return false;
        return false;
    }

    public boolean newDirectoryIn(File parentFile) {
        if (!parentFile.isDirectory()) return false;
        File newDirectoryFile = new File(parentFile.getAbsolutePath() + "/");
        return false;
    }
}
