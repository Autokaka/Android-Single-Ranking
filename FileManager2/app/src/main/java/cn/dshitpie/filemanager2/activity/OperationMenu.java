package cn.dshitpie.filemanager2.activity;

import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.event.BindEventBus;
import cn.dshitpie.filemanager2.event.MainEvent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
