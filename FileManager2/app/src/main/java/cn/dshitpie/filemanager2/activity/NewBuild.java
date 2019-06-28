package cn.dshitpie.filemanager2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.event.BindEventBus;
import cn.dshitpie.filemanager2.event.NewBuildEvent;
import cn.dshitpie.filemanager2.event.ToolbarMenuEvent;
import cn.dshitpie.filemanager2.utils.EditTextWatcher;
import cn.dshitpie.filemanager2.utils.FileManager;

@BindEventBus
public class NewBuild extends Base {
    private File inFile;
    private MaterialEditText editText;

    /**
     * 用EventBus从Event类接受activity/ToolbarMenu的消息
     * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeToolBarMenu(ToolbarMenuEvent event) {
        inFile = event.tellInFile();
    }

    /**
     * 初始化Button(文件, 取消, 文件夹)
     * */
    private void initButton() {
        Button btnNewFile = findViewById(R.id.new_file);
        Button btnCancel = findViewById(R.id.build_cancel);
        Button btnNewDir = findViewById(R.id.new_dir);
        btnNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = FileManager.newFileIn(inFile, editText.getText().toString());
                EventBus.getDefault().postSticky(new NewBuildEvent(result));
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNewDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = FileManager.newDirIn(inFile, editText.getText().toString());
                EventBus.getDefault().postSticky(new NewBuildEvent(result));
                finish();
            }
        });
    }

    /**
     * 初始化EditText及其定义的输入行为监视器
     * */
    private void initTextView() {
        editText = findViewById(R.id.build_editext);
        EditTextWatcher watcher = new EditTextWatcher(this, editText, 20);
        editText.addTextChangedListener(watcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);
        initButton();
        initTextView();
        showSoftInputWhenReady(editText);
    }
}
