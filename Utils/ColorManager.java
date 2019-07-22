package cn.dshitpie.filemanager2.utils;

import androidx.core.content.ContextCompat;

public class ColorManager {
    public static int findColorById(int resId) {
        return ContextCompat.getColor(AppManager.getContext(), resId);
    }
}
