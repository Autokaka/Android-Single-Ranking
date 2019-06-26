package cn.dshitpie.filemanager.adapter;

import android.view.View;

public interface RecyclerViewInterface {
    void onItemClick(View v, int position);
    void onItemLongPress(View v, int position);
}
