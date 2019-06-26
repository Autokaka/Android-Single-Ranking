package cn.dshitpie.filemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.dshitpie.filemanager.R;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.utils.TagConsultant;
import cn.dshitpie.filemanager.view.RecyclerViewItem;

public class NewItemAdapter extends BaseQuickAdapter<RecyclerViewItem, BaseViewHolder> {
    FileManager fileManager;
    private ArrayList<RecyclerViewItem> itemList;
    private ArrayList<Integer> highlightItemNameList;
    private Context context;
    private Stack<File> accessRoute;

    public NewItemAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    //列表返回功能
    public boolean back() {
        Log.d(TagConsultant.TAG, "-----back-----");
        if (accessRoute.isEmpty()) {
            Log.d(TagConsultant.TAG, "已到达根目录, 程序退出");
            Log.d(TagConsultant.TAG, "-----back(Done)-----");
            return false;
        }
        File parentFile[] = accessRoute.pop().getParentFile().listFiles();
        updateItemList(parentFile);
        notifyDataSetChanged();
        Log.d(TagConsultant.TAG, "当前访问路径反馈: " + accessRoute);
        Log.d(TagConsultant.TAG, "-----back(Done)-----");
        return true;
    }

    //获取当前路径的File信息
    public File tellNowInFile() {
        if (accessRoute.isEmpty()) return fileManager.getSdCard0Directory();
        return accessRoute.peek();
    }

    public void updateItemList(File fileList[]) {
        Log.d(TagConsultant.TAG, "-----updateItemList-----");
        itemList.clear();
        highlightItemNameList.clear();
        //排序
        fileManager.sortByType(fileList);
        //刷新数据
        RecyclerViewItem addItem = null;
        int addImageId = 0;
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) addImageId = R.drawable.directory;
            else addImageId = R.drawable.file_normal;
            addItem = new RecyclerViewItem(addImageId, fileList[i].getName(), fileList[i]);
            itemList.add(addItem);
        }
        Log.d(TagConsultant.TAG, "-----updateItemList(Done)-----");
    }

    public ArrayList<RecyclerViewItem> getItemList() {
        return itemList;
    }

    public void addToItemList(RecyclerViewItem addItem) {
        itemList.add(addItem);
    }

    private int indexOfItemName(String itemName) {
        for (int i = 0; i < itemList.size(); i++) if (itemName.equals(itemList.get(i).getItemName())) return i;
        return -1;
    }

    public void setHighlightItemName(String itemName) {
        highlightItemNameList.clear();
        int index = indexOfItemName(itemName);
        if (-1 == index) return;
        highlightItemNameList.add(index);
    }

    public void setHighlightItemNameList(String itemNameList[]) {
        highlightItemNameList.clear();
        int index = -1;
        for (int i = 0; i < itemNameList.length; i++) {
            index = indexOfItemName(itemNameList[i]);
            if (-1 != index) highlightItemNameList.add(index);
        }
    }

    public void addToAccessRoute(File addFile) {
        Log.d(TagConsultant.TAG, "-----addToAccessRoute-----");
        accessRoute.push(addFile);
        Log.d(TagConsultant.TAG, "当前访问路径反馈: " + accessRoute);
        Log.d(TagConsultant.TAG, "-----addToAccessRoute(Done)-----");
    }

    @Override
    protected void convert(BaseViewHolder helper, RecyclerViewItem item) {
        helper.setImageResource(R.id.image_view_file_type_img, item.getImageId());
        helper.setText(R.id.item_name, item.getItemName());
        //此处必须重新设置初始颜色和文字粗细, 否则走highlight部分的时候会出现渲染错误(神学, 无力)
        helper.setTextColor(R.id.item_name, ContextCompat.getColor(context, R.color.recyclerViewItemTextPrimary));
        helper.setTypeface(Typeface.DEFAULT);
        if (!highlightItemNameList.isEmpty()) {
            for (int i = 0; i < highlightItemNameList.size(); i++) {
                int highlightIndex = highlightItemNameList.get(i);
                if (helper.getLayoutPosition() == highlightIndex) {
                    helper.setTextColor(R.id.item_name, Color.BLUE);
                    helper.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }
        }
    }
}
