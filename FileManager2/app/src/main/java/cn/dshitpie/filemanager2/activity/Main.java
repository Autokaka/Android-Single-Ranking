package cn.dshitpie.filemanager2.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.List;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.adapter.RecyclerViewAdapter;
import cn.dshitpie.filemanager2.model.Item;
import cn.dshitpie.filemanager2.utils.FileManager;
import cn.dshitpie.filemanager2.utils.PermissionManager;

public class Main extends Base {
    private LinearLayoutManager layoutManager;
    private RecyclerView recyView;
    private RecyclerViewAdapter recyAdapter;

    /**
     * 初始化SmartRefreshLayout
     * */
    private void initRefreshLayout() {
        RefreshLayout refreshLayout = findViewById(R.id.main_reflayout);
        refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        refreshLayout.setRefreshHeader(new TaurusHeader(this));
        refreshLayout.setDragRate(0.7f);
        refreshLayout.setHeaderHeight(70);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                recyAdapter.loadPage();
                refreshlayout.finishRefresh(0);
            }
        });
    }

    /**
     * 初始化recyView, 绑定recyAdapter, 初始化recyView的数据, 初始化accessRoute, 设置事件监听
     * */
    private void initRecyView() {
        /**
         * 初始化RecyclerView
         * */
        recyView = findViewById(R.id.main_recyview);

        /**
         * 创建布局管理
         * */
        layoutManager = new LinearLayoutManager(this);
        recyView.setLayoutManager(layoutManager);

        /**
         * recyView适配器
         * */
        File sdCardFiles[] = FileManager.getSdCard0Directory().listFiles();
        List<Item> itemList = FileManager.convertToItemList(sdCardFiles);
        recyAdapter = new RecyclerViewAdapter(R.layout.item, itemList);
        recyView.setAdapter(recyAdapter);

        /**
         * adapter事件监听, 点击后展示子目录, 同时将当前访问路径加入到accessRoute中, 并记录上次访问目录的位置lastPosition(以便回退时能回到正确的位置)
         * */
        recyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Item clickedItem = (Item) adapter.getData().get(position);
                File file = clickedItem.getItemFile();
                recyAdapter.forwardPage(file, layoutManager);
            }
        });
        recyAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(Main.this, OperationMenu.class);
                startActivity(intent);
                return true;
            }
        });
    }

    /**
     * 权限申请器初始化, 申请权限
     * */
    private void initPermission() {
        PermissionManager permissionManager = new PermissionManager();
        permissionManager.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionManager.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionManager.request(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRefreshLayout();
        initPermission();
        initRecyView();
    }

    /**
     * 重写返回按钮点击逻辑, 到达根目录时, 程序退出, 其他情况下均回到上级目录, 并且要跳转到上次的位置
     * */
    @Override
    public void onBackPressed() {
        if (recyAdapter.lastPosition().isEmpty()) super.onBackPressed();
        else recyAdapter.backPage(layoutManager);
    }
}
