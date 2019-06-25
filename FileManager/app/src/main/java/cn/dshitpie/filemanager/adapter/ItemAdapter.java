package cn.dshitpie.filemanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import cn.dshitpie.filemanager.R;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.view.RecyclerViewItem;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String TAG = "testapp";
    FileManager fileManager;
    private ArrayList<RecyclerViewItem> itemList;
    private Stack<File> accessRoute;

    public ItemAdapter(ArrayList<RecyclerViewItem> recyclerViewItemList) {
        fileManager = new FileManager();
        itemList = recyclerViewItemList;
        accessRoute = new Stack<>();
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
        if (accessRoute.isEmpty()) return false;
        File parentFile[] = accessRoute.pop().getParentFile().listFiles();
        updateRecyclerView(parentFile);
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File clickedItemFile = itemList.get(position).getItemFile();
                accessRoute.push(clickedItemFile);
                File childFile[] = clickedItemFile.listFiles();
                updateRecyclerView(childFile);
            }
        });
    }

    //Adapter内部更新RecyclerView内容的完整方法
    private void updateRecyclerView(File fileList[]) {
        itemList.clear();
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
        notifyDataSetChanged();
    }

    //渲染视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
