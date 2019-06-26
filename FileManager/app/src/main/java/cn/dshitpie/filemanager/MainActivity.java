package cn.dshitpie.filemanager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;

import cn.dshitpie.filemanager.adapter.ItemAdapter;
import cn.dshitpie.filemanager.adapter.RecyclerViewInterface;
import cn.dshitpie.filemanager.utils.CodeConsultant;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.utils.PermissionManager;
import cn.dshitpie.filemanager.utils.TagConsultant;
import cn.dshitpie.filemanager.view.RecyclerViewItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private PermissionManager permissionManager;
    private FileManager fileManager;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化导航栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra("nowInFile", itemAdapter.tellNowInFile());
                startActivityForResult(intent, CodeConsultant.ADD_ITEM_ACTIVITY);
            }
        });
        //初始化左侧抽屉
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //初始化文件工具类
        fileManager = new FileManager();
        //初始化权限申请工具类并申请权限
        permissionManager = new PermissionManager();
        permissionManager.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionManager.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionManager.request(MainActivity.this);
        //初始化RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(this, new ArrayList<RecyclerViewItem>());
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.setRecyclerViewListner(new RecyclerViewInterface() {
            @Override
            public void onItemClick(View v, int position) {
                File clickedItemFile = itemAdapter.getItemList().get(position).getItemFile();
                itemAdapter.addToAccessRoute(clickedItemFile);
                File childFile[] = clickedItemFile.listFiles();
                itemAdapter.updateItemList(childFile);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongPress(View v, int position) {
                File nowSelectFile = itemAdapter.getItemList().get(position).getItemFile();
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra(TagConsultant.NOW_SELECT_FILE, nowSelectFile);
                startActivityForResult(intent, CodeConsultant.MENU_ACTIVITY);
            }
        });
        itemAdapter.updateItemList(itemAdapter.tellNowInFile().listFiles());
        itemAdapter.notifyDataSetChanged();
        //初始化RefreshLayout
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
        refreshLayout.setRefreshHeader(new WaterDropHeader(this));
        refreshLayout.setDragRate(0.8f);
        refreshLayout.setEnableOverScrollBounce(true);
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                itemAdapter.updateItemList(itemAdapter.tellNowInFile().listFiles());
                itemAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else if (!itemAdapter.back()) super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            default: break;
            case CodeConsultant.ADD_ITEM_ACTIVITY: {
                int feedback = data.getIntExtra(TagConsultant.FEEDBACK, CodeConsultant.OPERATE_SUCCESS);
                String itemName = data.getStringExtra(TagConsultant.ITEM_NAME);
                if (CodeConsultant.OPERATE_SUCCESS == feedback) {
                    File childFiles[] = itemAdapter.tellNowInFile().listFiles();
                    itemAdapter.updateItemList(childFiles);
                    itemAdapter.setHighlightItemName(itemName);
                    itemAdapter.notifyDataSetChanged();
                }
                break;
            }
            case CodeConsultant.MENU_ACTIVITY: {
                int feedback = data.getIntExtra(TagConsultant.FEEDBACK, CodeConsultant.OPERATE_SUCCESS);
                if (CodeConsultant.OPERATE_SUCCESS == feedback) {
                    File childFiles[] = itemAdapter.tellNowInFile().listFiles();
                    itemAdapter.updateItemList(childFiles);
                    itemAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }
}
