package cn.dshitpie.filemanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import cn.dshitpie.filemanager.MenuActivity;
import cn.dshitpie.filemanager.R;
import cn.dshitpie.filemanager.utils.CodeConsultant;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.utils.TagConsultant;
import cn.dshitpie.filemanager.view.RecyclerViewItem;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    FileManager fileManager;
    private ArrayList<RecyclerViewItem> itemList;
    private ArrayList<Integer> highlightItemNameList;
    private Context context;
    private Stack<File> accessRoute;
    private RecyclerViewInterface recyclerViewInterface;

    public ItemAdapter(Context context, ArrayList<RecyclerViewItem> recyclerViewItemList) {
        this.context = context;
        fileManager = new FileManager();
        itemList = recyclerViewItemList;
        highlightItemNameList = new ArrayList<>();
        accessRoute = new Stack<>();
    }

    public void setRecyclerViewListner(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    //创建视图层关联
    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView itemImage;
        TextView itemName;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            itemImage = (ImageView) view.findViewById(R.id.image_view_file_type_img);
            itemName = (TextView) view.findViewById(R.id.item_name);
        }
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

    //绑定视图层和数据层
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RecyclerViewItem item = itemList.get(position);
        holder.itemImage.setImageResource(item.getImageId());
        holder.itemName.setText(item.getItemName());
        //此处必须重新设置初始颜色和文字粗细, 否则走highlight部分的时候会出现渲染错误(神学, 无力)
        holder.itemName.setTextColor(ContextCompat.getColor(context, R.color.recyclerViewItemTextPrimary));
        holder.itemName.setTypeface(Typeface.DEFAULT);
        if (!highlightItemNameList.isEmpty()) {
            for (int i = 0; i < highlightItemNameList.size(); i++) {
                int highlightIndex = highlightItemNameList.get(i);
                if (position == highlightIndex) {
                    holder.itemName.setTextColor(Color.BLUE);
                    holder.itemName.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }
        }
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

    //渲染视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //为防止内存泄漏, 采用接口回调方案
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != recyclerViewInterface) recyclerViewInterface.onItemClick(v, holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != recyclerViewInterface) recyclerViewInterface.onItemLongPress(v, holder.getAdapterPosition());
                return true;
            }
        });
        return holder;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
