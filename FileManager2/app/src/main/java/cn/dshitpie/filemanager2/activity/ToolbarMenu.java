package cn.dshitpie.filemanager2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.event.BindEventBus;
import cn.dshitpie.filemanager2.event.MainEvent;
import cn.dshitpie.filemanager2.event.ToolbarMenuEvent;
import cn.dshitpie.filemanager2.utils.ActivityCollector;

@BindEventBus
public class ToolbarMenu extends Base {
    private File inFile;

    /**
     * 用EventBus从Event类接受activity/Main的消息
     * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeMain(MainEvent event) {
        inFile = event.tellInFile();
    }

    /**
     * 初始化全部Button
     * */
    private void initButton() {
        Button btnNewBuild = findViewById(R.id.new_build);
        Button btnSelectAll = findViewById(R.id.select_all);
        Button btnSortBy = findViewById(R.id.sort_by);
        Button btnExit = findViewById(R.id.exit);
        btnNewBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToolbarMenu.this, NewBuild.class);
                startActivity(intent);
                EventBus.getDefault().postSticky(new ToolbarMenuEvent(inFile));
                finish();
            }
        });
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_menu);
        setActivityDisplay(0.35, 0.4, Gravity.RIGHT|Gravity.TOP);
        initButton();
    }
}
