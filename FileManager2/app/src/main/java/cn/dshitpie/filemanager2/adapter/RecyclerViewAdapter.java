package cn.dshitpie.filemanager2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;
import java.util.Stack;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.model.Item;
import cn.dshitpie.filemanager2.utils.FileManager;

/**
 * accessRoute: 如其名, 储存访问路径, 栈顶即为当前所在目录的File
 * */
public class RecyclerViewAdapter extends BaseQuickAdapter<Item, BaseViewHolder> {
    private Stack<File> accessRoute;
    private Stack<Integer> lastPosition;

    public Stack<File> accessRoute() {
        return accessRoute;
    }

    public Stack<Integer> lastPosition() {
        return lastPosition;
    }

    public RecyclerViewAdapter(int layoutResId, List<Item> data) {
        super(layoutResId, data);
        Logger.addLogAdapter(new AndroidLogAdapter());
        accessRoute = new Stack<>();
        accessRoute.push(FileManager.getSdCard0Directory());
        lastPosition = new Stack<>();
    }

    /**
    * RecyclerView的刷新机制存在神学不同步性和滞后性(不知道是不是我不会玩), 只能通过这个方法实现跳转到指定位置
    * */
    public void moveToPosition(LinearLayoutManager manager, int position) {
        manager.scrollToPositionWithOffset(position, 0);
        manager.setStackFromEnd(false);
    }

    /**
     * 加载当前页面内容(动态刷新, 不操作accessRoute和lastPosition), 用于临时加载或者刷新当前页面
     * */
    public void loadPage() {
        File file = accessRoute.peek();
        List<Item> itemList = FileManager.convertToItemList(file.listFiles());
        setNewData(itemList);
    }

    /**
     * 加载某个路径页面的内容(临时加载, 不记录到accessRoute和lastPosition), 用于临时加载选中页面
     * */
    public void loadPage(File file) {
        List<Item> itemList = FileManager.convertToItemList(file.listFiles());
        setNewData(itemList);
    }

    /**
     * 前进到file的子页面(记录到accessRoute和lastPosition), 用于跳转和加载下一页
     * */
    public void forwardPage(File file, LinearLayoutManager manager) {
        accessRoute.add(file);
        lastPosition.add(manager.findFirstCompletelyVisibleItemPosition());
        loadPage(file);
        moveToPosition(manager, 0);
    }

    /**
     * 退回到上一页面(更新accessRoute和lastPosition)
     * */
    public void backPage(LinearLayoutManager manager) {
        accessRoute.pop();
        loadPage();
        moveToPosition(manager, lastPosition.pop());
    }

    /**
     * 通过传入的File将列表的某一个item高亮, 颜色通过colorId传入
     * */
    public void highlight(File file, int colorId) {
        int position = FileManager.locate(file);
        Item item = getItem(position);
        item.setTextColor(colorId);
        item.setTypeface(Typeface.DEFAULT_BOLD);
        notifyItemChanged(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, Item item) {
        helper.setImageResource(R.id.item_img, item.getImgResId())
                .setText(R.id.item_name, item.getItemName())
                .setTextColor(R.id.item_name, item.getTextColor())
                .setTypeface(R.id.item_name, item.getTypeface());
    }
}
