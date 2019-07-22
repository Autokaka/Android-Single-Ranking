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
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
        romInfo += "储存: " + Formatter.formatFileSize(context, file.getUsableSpace()) + "/" + Formatter.formatFileSize(context, file.getTotalSpace());
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
     * 这是一个很简单的复制粘贴函数, 它没有"覆盖"处理机制, 后期需要考虑dstFile内如果已经存在同名文件的更加复杂的情况
     * */
    public static synchronized int copy(File srcFile, File dstFile) {
        if (!dstFile.exists() && !dstFile.mkdirs()) return CodeConsultant.OPERATE_FAIL;
        try {
            if (srcFile.isFile()) return copyFile(srcFile, new File(dstFile, srcFile.getName()));
            else {
                File[] srcChildFiles = srcFile.listFiles();
                int result;
                for (File cursor : srcChildFiles) {
                    File dstChild = new File(dstFile, cursor.getName());
                    if (cursor.isFile()) result = copyFile(cursor, dstChild);
                    else result = copy(cursor, dstChild);
                    if (CodeConsultant.OPERATE_SUCCESS != result) return result;
                }
                return CodeConsultant.OPERATE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testapp", e.getMessage());
            return CodeConsultant.OPERATE_FAIL;
        }
    }

    private static synchronized int copyFile(File srcFile, File dstFile) {
        try {
            if (!srcFile.exists()) return CodeConsultant.FILE_NOT_EXISTS;
            else if (!srcFile.canRead()) return CodeConsultant.FILE_NOT_READABLE;
            else {
                if (!dstFile.exists() && !dstFile.createNewFile()) return CodeConsultant.OPERATE_FAIL;
                FileInputStream fileInputStream = new FileInputStream(srcFile);
                FileOutputStream fileOutputStream = new FileOutputStream(dstFile);
                FileChannel inChannel = fileInputStream.getChannel();
                FileChannel outChannel = fileOutputStream.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                inChannel.close();
                outChannel.close();
                fileInputStream.close();
                fileOutputStream.close();
                return CodeConsultant.OPERATE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CodeConsultant.OPERATE_FAIL;
        }
    }
}
