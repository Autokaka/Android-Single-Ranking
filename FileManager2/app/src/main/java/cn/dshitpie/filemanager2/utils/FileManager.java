package cn.dshitpie.filemanager2.utils;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.model.Item;

public class FileManager {

    public static File getSdCard0Directory() {
        return Environment.getExternalStorageDirectory();
    }

    public static boolean isSdCard0Dir(File file) {
        if (file.equals(getSdCard0Directory())) return true;
        else return false;
    }

    /**
     * 传入一个File, 返回这个File所在File列表的位置序号
     * */
    public static int locate(File file) {
        if (FileManager.isSdCard0Dir(file)) return -1;
        File list[] = sortByType(file.getParentFile().listFiles());
        int index = -1;
        for (int i = 0; i < list.length; i++) if (list[i].equals(file)) index = i;
        return index;
    }

    public static Item convertToItem(File file) {
        int imgResId;
        if (file.isFile()) imgResId = R.drawable.type_file;
        else imgResId = R.drawable.type_dir;
        return (new Item(imgResId, file.getName(), file));
    }

    public static List<Item> convertToItemList(File files[]) {
        if (null == files || 0 == files.length) return null;
        files = sortByType(files);
        int imgResId;
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) imgResId = R.drawable.type_file;
            else imgResId = R.drawable.type_dir;
            itemList.add(new Item(imgResId, files[i].getName(), files[i]));
        }
        return itemList;
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

    public static String getCntInfo(File file) {
        int fileCnt = 0, dirCnt = 0;
        File childFile[] = file.listFiles();
        for (int i = 0; i < childFile.length; i++) {
            if (childFile[i].isFile()) fileCnt++;
            else if (childFile[i].isDirectory()) dirCnt++;
        }
        String cntInfo = "";
        cntInfo += "文件夹数: " + dirCnt + " 文件数: " + fileCnt;
        return cntInfo;
    }

    public static int newFileIn(File file, String name) {
        if (!file.isDirectory()) return CodeConsultant.OPERATE_FAIL;
        File newFileFile = new File(file.getAbsolutePath() + "/" + name);
        if (newFileFile.exists()) return CodeConsultant.FILE_ALREADY_EXISTS;
        try {
            newFileFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newFileFile.exists()) return CodeConsultant.OPERATE_SUCCESS;
        return CodeConsultant.OPERATE_FAIL;
    }

    public static int newDirIn(File file, String name) {
        if (!file.isDirectory()) return CodeConsultant.OPERATE_FAIL;
        File newDirectoryFile = new File(file.getAbsolutePath() + "/" + name);
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
        else if (newName.equals(TagConsultant.NEW_FILE_OR_DIR)) return CodeConsultant.OPERATE_FAIL;
        else {
            String parentFilePath = file.getParent();
            File newFile = new File(parentFilePath + "/" + newName);
            if (file.renameTo(newFile)) return CodeConsultant.OPERATE_SUCCESS;
            else return CodeConsultant.OPERATE_FAIL;
        }
    }

    /**
     * 把下面两个逻辑改了, 就能实现复制粘贴了
     * */
    private static boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean copyFolder(String oldPath, String newPath) {
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                if (!newFile.mkdirs()) {
                    Log.e("--Method--", "copyFolder: cannot create directory.");
                    return false;
                }
            }
            File oldFile = new File(oldPath);
            String[] files = oldFile.list();
            File temp;
            for (String file : files) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file);
                } else {
                    temp = new File(oldPath + File.separator + file);
                }

                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (!temp.exists()) {
                    Log.e("--Method--", "copyFolder:  oldFile not exist.");
                    return false;
                } else if (!temp.isFile()) {
                    Log.e("--Method--", "copyFolder:  oldFile not file.");
                    return false;
                } else if (!temp.canRead()) {
                    Log.e("--Method--", "copyFolder:  oldFile cannot read.");
                    return false;
                } else {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
