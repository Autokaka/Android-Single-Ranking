package cn.dshitpie.filemanager.utils;

import java.io.File;
import java.util.Comparator;

public class SortByInitialComparator implements Comparator {
    private static final String TAG = "testapp";
    @Override
    public int compare(Object o1, Object o2) {
        File left = (File) o1, right = (File) o2;
        String leftName = left.getName(), rightName = right.getName();
        if ('.' == leftName.charAt(0)) return -1;
        if ('.' == rightName.charAt(0)) return 1;
        if (leftName.toLowerCase().charAt(0) == rightName.charAt(0)) return -1;
        if (leftName.charAt(0) == rightName.toLowerCase().charAt(0)) return 1;
        return (leftName.toLowerCase().charAt(0) - rightName.toLowerCase().charAt(0));
    }
}
