package cn.dshitpie.filemanager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import cn.dshitpie.filemanager.adapter.ItemAdapter;
import cn.dshitpie.filemanager.utils.CodeConsultant;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.utils.PermissionManager;
import cn.dshitpie.filemanager.view.RecyclerViewItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "testapp";
    private PermissionManager permissionManager;
    private FileManager fileManager;
    private ArrayList<RecyclerViewItem> itemList;
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
                startActivityForResult(intent, CodeConsultant.ADD_ITEM_ACTIVITY_CODE);
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
        itemList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(this, itemList);
        recyclerView.setAdapter(itemAdapter);
        File sdCard0ChildFile[] = fileManager.getSdCard0Directory().listFiles();
        fileManager.sortByType(sdCard0ChildFile);
        RecyclerViewItem addItem = null;
        int addImageId = 0;
        for (int i = 0; i < sdCard0ChildFile.length; i++) {
            if (sdCard0ChildFile[i].isDirectory()) addImageId = R.drawable.directory;
            else addImageId = R.drawable.file_normal;
            addItem = new RecyclerViewItem(addImageId, sdCard0ChildFile[i].getName(), sdCard0ChildFile[i]);
            itemList.add(addItem);
        }
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
            case CodeConsultant.ADD_ITEM_ACTIVITY_CODE: {
                int feedback = data.getIntExtra("feedback", 1);
                String itemName = data.getStringExtra("itemName");
                if (0 == feedback) {
                    Log.d(TAG, "走到反馈内容");
                    File childFiles[] = itemAdapter.tellNowInFile().listFiles();
                    itemAdapter.updateItemList(childFiles);
                    itemAdapter.setHighlightItemName(itemName);
                    itemAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
