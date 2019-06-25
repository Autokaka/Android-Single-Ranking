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

    public int newFileIn(File parentDirectoryFile, String newFileName) {
        //1: 文件已经存在, 0: 创建成功, -1: 创建失败
        if (!parentDirectoryFile.isDirectory()) return -1;
        File newFileFile = new File(parentDirectoryFile.getAbsolutePath() + "/" + newFileName);
        if (newFileFile.exists()) return 1;
        try {
            newFileFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newFileFile.exists()) return 0;
        return -1;
    }

    public int newDirectoryIn(File parentDirectoryFile, String newDirectoryName) {
        //1: 文件夹已经存在, 0: 创建成功, -1: 创建失败
        if (!parentDirectoryFile.isDirectory()) return -1;
        File newDirectoryFile = new File(parentDirectoryFile.getAbsolutePath() + "/" + newDirectoryName);
        if (newDirectoryFile.exists()) return 1;
        newDirectoryFile.mkdirs();
        if (newDirectoryFile.exists()) return 0;
        return -1;
    }
}
