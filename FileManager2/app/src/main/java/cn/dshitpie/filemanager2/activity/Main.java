package cn.dshitpie.filemanager2.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.adapter.RecyclerViewAdapter;
import cn.dshitpie.filemanager2.annotation.BindEventBus;
import cn.dshitpie.filemanager2.event.CopyEvent;
import cn.dshitpie.filemanager2.event.DeleteEvent;
import cn.dshitpie.filemanager2.event.MainEvent;
import cn.dshitpie.filemanager2.event.NewBuildEvent;
import cn.dshitpie.filemanager2.event.RenameEvent;
import cn.dshitpie.filemanager2.model.Item;
import cn.dshitpie.filemanager2.utils.CodeConsultant;
import cn.dshitpie.filemanager2.utils.ColorManager;
import cn.dshitpie.filemanager2.utils.FileManager;
import cn.dshitpie.filemanager2.utils.PermissionManager;

@BindEventBus
public class Main extends Base {
    //RecyView
    private LinearLayoutManager layoutManager;
    private RecyclerView recyView;
    private RecyclerViewAdapter recyAdapter;
    //ToolBar
    Toolbar toolbar;
    LinearLayoutCompat info;
    private TextView routeInfo;
    private TextView romInfo;
    File copyFile;
    Button btnPaste, btnExitPaste;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeDelete(DeleteEvent event) {
        recyAdapter.loadPage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeNewBuild(NewBuildEvent event) {
        File file = event.tellHighlightFile();
        recyAdapter.loadPage();
        recyAdapter.highlight(file, R.color.colorPrimary);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeRename(RenameEvent event) {
        File renamedFile = event.tellRenamedFile();
        recyAdapter.loadPage();
        recyAdapter.highlight(renamedFile, R.color.colorPrimary);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeCopy(CopyEvent event) {
        copyFile = event.tellCopyFile();
        enterCopyMode();
    }

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
     * 初始化ToolBar的内容
     * */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * 初始化路径信息和内存信息并同步显示到toolbar
         * */
        info = findViewById(R.id.info);
        routeInfo = findViewById(R.id.route_info);
        romInfo = findViewById(R.id.rom_info);
        syncInfo(FileManager.getSdCard0Directory());

        /**
         * info添加事件响应
         * */
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 右上角的菜单
         * */
        Button menu = findViewById(R.id.toolbar_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, ToolbarMenu.class);
                startActivity(intent);
                EventBus.getDefault().postSticky(new MainEvent(recyAdapter.accessRoute().peek()));
            }
        });

        /**
         * 右上角粘贴按钮
         * 当且仅当进入复制模式的时候, 粘贴按钮才会显示,
         * 所以粘贴按钮的事件响应必然建立在"已经进入复制模式"这个事实,
         * 复制模式: toolbar主题色改变, 粘贴按钮显现, 复制路径绝不会为空
         * */
        btnExitPaste = findViewById(R.id.btn_exit);
        btnPaste = findViewById(R.id.btn_paste);

        btnExitPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitCopyMode();
            }
        });
        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = FileManager.copy(copyFile, recyAdapter.accessRoute().peek());
                if (CodeConsultant.OPERATE_FAIL == result)
                    Toast.makeText(Main.this, "操作失败", Toast.LENGTH_LONG).show();
                else if (CodeConsultant.FILE_NOT_EXISTS == result)
                    Toast.makeText(Main.this, "拷贝文件不存在", Toast.LENGTH_LONG).show();
                else if (CodeConsultant.FILE_NOT_READABLE == result)
                    Toast.makeText(Main.this, "文件只读", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(Main.this, "操作成功", Toast.LENGTH_LONG).show();
                    exitCopyMode();
                    recyAdapter.loadPage();
                }
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
         * recyView适配器.
         * */
        File sdCard = FileManager.getSdCard0Directory();
        List<Item> itemList = FileManager.convertToItemList(sdCard.listFiles());
        recyAdapter = new RecyclerViewAdapter(R.layout.item, itemList);
        recyView.setAdapter(recyAdapter);

        /**
         * adapter事件监听, 点击后展示子目录, 同时将当前访问路径加入到accessRoute中, 记录上次访问目录的位置lastPosition(以便回退时能回到正确的位置), 并将路径信息打印到toolbar
         * */
        recyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Item clickedItem = (Item) adapter.getData().get(position);
                File clickedFile = clickedItem.getItemFile();
                recyAdapter.forwardPage(clickedFile, layoutManager);
                syncInfo(clickedFile);
            }
        });
        recyAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(Main.this, OperationMenu.class);
                startActivity(intent);
                Item selectItem = (Item) recyAdapter.getData().get(position);
                EventBus.getDefault().postSticky(new MainEvent(selectItem.getItemFile()));
                return true;
            }
        });
    }

    /**
     * 初始化左侧MaterialDrawer
     * */
    private void initDrawer() {
        new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        //pass your items here
                )
                .build();
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
        initPermission();
        initToolbar();
        initRefreshLayout();
        initRecyView();
        initDrawer();
    }

    /**
     * 重写返回按钮点击逻辑,
     * 如果在复制模式, 需要先退出复制模式,
     * 到达根目录时, 程序退出,
     * 其他情况下均回到上级目录, 跳转到上次的位置, 并将路径信息打印到toolbar
     * */
    @Override
    public void onBackPressed() {
        if (recyAdapter.lastPosition().isEmpty()) super.onBackPressed();
        else {
            recyAdapter.backPage(layoutManager);
            syncInfo(recyAdapter.accessRoute().peek());
        }
    }

    /**
     * 激活Main的复制粘贴模式:
     * 修改toolbar的主题色为colorAccent,
     * 展示"退出复制模式"按钮 和 "粘贴"按钮
     * */
    private void enterCopyMode() {
        toolbar.setBackgroundColor(ColorManager.findColorById(R.color.colorAccent));
        btnExitPaste.setVisibility(View.VISIBLE);
        btnPaste.setVisibility(View.VISIBLE);
    }

    /**
     * 激活Main的复制粘贴模式:
     * 修改toolbar的主题色为colorPrimary,
     * 隐藏"退出复制模式"按钮 和 "粘贴"按钮
     * */
    private void exitCopyMode() {
        toolbar.setBackgroundColor(ColorManager.findColorById(R.color.colorPrimary));
        btnExitPaste.setVisibility(View.GONE);
        btnPaste.setVisibility(View.GONE);
    }

    /**
     * 将file对应的信息同步打印到ToolBar上
     * */
    private void syncInfo(File file) {
        routeInfo.setText(file.toString());
        String genInfo = FileManager.getRomInfo(Main.this, file) + " " + FileManager.getCntInfo(file);
        romInfo.setText(genInfo);
    }
}
