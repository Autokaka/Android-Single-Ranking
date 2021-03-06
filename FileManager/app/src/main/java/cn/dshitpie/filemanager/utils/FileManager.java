package cn.dshitpie.filemanager.utils;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class FileManager {

    public static File getSdCard0Directory() {
        return Environment.getExternalStorageDirectory();
    }

    public static File[] sortByType(File fileList[]) {
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

    public static String getRomInfo(Context context, File file) {
        String romInfo = "";
        romInfo += "储存: " + Formatter.formatFileSize(context, file.getTotalSpace()) + "/" + Formatter.formatFileSize(context, file.getUsableSpace());
        return romInfo;
    }

    public static String getFileCountInfo(File file) {
        int fileCnt = 0, dirCnt = 0;
        File childFile[] = file.listFiles();
        for (int i = 0; i < childFile.length; i++) {
            if (childFile[i].isFile()) fileCnt++;
            else if (childFile[i].isDirectory()) dirCnt++;
        }
        String cntInfo = "";
        cntInfo += "文件数: " + fileCnt + " 文件夹数: " + dirCnt;
        return cntInfo;
    }

    public static int newFileIn(File parentDirFile, String newFileName) {
        if (!parentDirFile.isDirectory()) return CodeConsultant.OPERATE_FAIL;
        File newFileFile = new File(parentDirFile.getAbsolutePath() + "/" + newFileName);
        if (newFileFile.exists()) return CodeConsultant.FILE_ALREADY_EXISTS;
        try {
            newFileFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newFileFile.exists()) return CodeConsultant.OPERATE_SUCCESS;
        return CodeConsultant.OPERATE_FAIL;
    }

    public static int newDirIn(File parentDirFile, String newDirectoryName) {
        if (!parentDirFile.isDirectory()) return CodeConsultant.OPERATE_FAIL;
        File newDirectoryFile = new File(parentDirFile.getAbsolutePath() + "/" + newDirectoryName);
        if (newDirectoryFile.exists()) return CodeConsultant.FILE_ALREADY_EXISTS;
        newDirectoryFile.mkdirs();
        if (newDirectoryFile.exists()) return CodeConsultant.OPERATE_SUCCESS;
        return CodeConsultant.OPERATE_FAIL;
    }

    public static int delete(File file) {
        if (!file.exists()) return CodeConsultant.FILE_NOT_EXISTS;
        else if (file.isFile()) {
            deleteFileSafely(file);
            return CodeConsultant.OPERATE_SUCCESS;
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            int delChildResult = CodeConsultant.OPERATE_SUCCESS;
            if (!(null == childFiles || 0 == childFiles.length)) {
                for (int i = 0; i < childFiles.length; i++) {
                    delChildResult = delete(childFiles[i]);
                }
            }
            if (CodeConsultant.OPERATE_SUCCESS != delChildResult) return delChildResult;
        }
        return deleteFileSafely(file);
    }

    private static int deleteFileSafely(File file) {
        if (null == file) return CodeConsultant.OPERATE_FAIL;
        String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
        File tmp = new File(tmpPath);
        file.renameTo(tmp);
        if (tmp.delete()) return CodeConsultant.OPERATE_SUCCESS;
        else return CodeConsultant.OPERATE_FAIL;
    }

    public static int rename(File file, String newName) {
        if (!file.exists()) return CodeConsultant.FILE_NOT_EXISTS;
        else if (newName.equals(file.getName())) return CodeConsultant.OPERATE_FAIL;
        else if (newName.equals(TagConsultant.FILE_DEFAULT_NAME)) return CodeConsultant.OPERATE_FAIL;
        else {
            String parentFilePath = file.getParent();
            File newFile = new File(parentFilePath + "/" + newName);
            if (file.renameTo(newFile)) return CodeConsultant.OPERATE_SUCCESS;
            else return CodeConsultant.OPERATE_FAIL;
        }
    }
}
