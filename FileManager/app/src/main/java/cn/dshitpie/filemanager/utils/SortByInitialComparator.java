package cn.dshitpie.filemanager.utils;

import android.util.Log;

import java.io.File;
import java.util.Comparator;

public class SortByInitialComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        File left = (File) o1, right = (File) o2;
        String leftName = left.getName(), rightName = right.getName();
        if (!isLetter(leftName.charAt(0)) && isLetter(rightName.charAt(0))) return -1;
        else if (!isLetter(rightName.charAt(0)) && isLetter(leftName.charAt(0))) return 1;
        else if (leftName.toLowerCase().charAt(0) == rightName.charAt(0)) return -1;
        else if (leftName.charAt(0) == rightName.toLowerCase().charAt(0)) return 1;
        else if (leftName.charAt(0) == rightName.charAt(0)) {
            String subLeftName = leftName.substring(1), subRightName = rightName.substring(1);
//            Log.d(TAG, "左侧: " + subLeftName + " 右侧: " + subRightName);
            if ("".equals(subLeftName) && !"".equals(subRightName)) return -1;
            else if ("".equals(subRightName) && !"".equals(subLeftName)) return 1;
            return subCompare(subLeftName, subRightName);
        }
        return (leftName.toLowerCase().charAt(0) - rightName.toLowerCase().charAt(0));
    }

    private int subCompare(String leftName, String rightName) {
        if (!isLetter(leftName.charAt(0)) && isLetter(rightName.charAt(0))) return -1;
        else if (!isLetter(rightName.charAt(0)) && isLetter(leftName.charAt(0))) return 1;
        else if (leftName.toLowerCase().charAt(0) == rightName.charAt(0)) return -1;
        else if (leftName.charAt(0) == rightName.toLowerCase().charAt(0)) return 1;
        else if (leftName.charAt(0) == rightName.charAt(0)) {
            String subLeftName = leftName.substring(1), subRightName = rightName.substring(1);
//            Log.d(TAG, "左侧: " + subLeftName + " 右侧: " + subRightName);
            if ("".equals(subLeftName) && !"".equals(subRightName)) return -1;
            else if ("".equals(subRightName) && !"".equals(subLeftName)) return 1;
            return subCompare(subLeftName, subRightName);
        }
        return (leftName.toLowerCase().charAt(0) - rightName.toLowerCase().charAt(0));
    }

    private boolean isLetter(char s) {
        if ((s > 'a' && s < 'z') || (s > 'A' && s < 'Z')) return true;
        return false;
    }
}
