package cn.dshitpie.filemanager2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.annotation.BindEventBus;
import cn.dshitpie.filemanager2.event.CopyEvent;
import cn.dshitpie.filemanager2.event.DeleteEvent;
import cn.dshitpie.filemanager2.event.MainEvent;
import cn.dshitpie.filemanager2.event.OperationMenuEvent;
import cn.dshitpie.filemanager2.event.RenameEvent;
import cn.dshitpie.filemanager2.utils.CodeConsultant;
import cn.dshitpie.filemanager2.utils.FileManager;

/**
 * 添加了注解BindEventBus以后, 就可以直接使用eventbus而不需要再写register和unregister了. 当然你也可以不加注解, 自己写register和unregister
 * */
@BindEventBus
public class OperationMenu extends Base {
    private File selectFile;

    /**
     * 用EventBus从Event类接受activity/Main的消息
     * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeMain(MainEvent event) {
        selectFile = event.tellSelectFile();
    }

    /**
     * 用EventBus从Event类接受activity/Rename的消息,
     * 注意这里不能用sticky, 因为它不是在启动的时候就要收到消息, 而sticky在下一次新数据进来之前, 会一直保持之前的状态(上次Rename发过来的),
     * 所以加了sticky的话, 这个Activity会一直finish()
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeRename(RenameEvent event) {
        finish();
    }

    /**
     * 初始化所有的Button, 并添加事件响应
     * */
    private void initButton() {
        Button btnCopy = findViewById(R.id.menu_copy);
        Button btnShear = findViewById(R.id.menu_shear);
        Button btnLink = findViewById(R.id.menu_link);
        Button btnRename = findViewById(R.id.menu_rename);
        Button btnDelete = findViewById(R.id.menu_delete);
        Button btnCompress = findViewById(R.id.menu_compress);
        Button btnAttribute = findViewById(R.id.menu_attribute);
        Button btnShare = findViewById(R.id.menu_share);
        Button btnOpenas = findViewById(R.id.menu_openas);
        Button btnBookmark = findViewById(R.id.menu_bookmark);

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new CopyEvent(selectFile));
                finish();
            }
        });
        btnShear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OperationMenu.this, Rename.class);
                startActivity(intent);
                EventBus.getDefault().postSticky(new OperationMenuEvent(selectFile));
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = FileManager.delete(selectFile);
                if (result == CodeConsultant.OPERATE_FAIL)
                    Toast.makeText(OperationMenu.this, "操作失败", Toast.LENGTH_LONG).show();
                else if (result == CodeConsultant.FILE_NOT_EXISTS)
                    Toast.makeText(OperationMenu.this, "文件不存在", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(OperationMenu.this, "操作成功", Toast.LENGTH_LONG).show();
                    EventBus.getDefault().postSticky(new DeleteEvent());
                    finish();
                }
                finish();
            }
        });
        btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAttribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnOpenas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_menu);
        restrictActivityWidth(0.75, Gravity.CENTER);
        initButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
