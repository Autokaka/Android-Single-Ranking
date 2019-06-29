package cn.dshitpie.filemanager2.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import cn.dshitpie.filemanager2.R;
import cn.dshitpie.filemanager2.annotation.BindEventBus;
import cn.dshitpie.filemanager2.event.MainEvent;
import cn.dshitpie.filemanager2.event.OperationMenuEvent;
import cn.dshitpie.filemanager2.event.RenameEvent;
import cn.dshitpie.filemanager2.utils.AppManager;
import cn.dshitpie.filemanager2.utils.CodeConsultant;
import cn.dshitpie.filemanager2.utils.EditTextWatcher;
import cn.dshitpie.filemanager2.utils.FileManager;

@BindEventBus
public class Rename extends Base {
    private File selectFile;
    private MaterialEditText editText;

    /**
     * 用EventBus从Event类接受activity/OperationMenu的消息
     * */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void subscribeOperationMenu(OperationMenuEvent event) {
        selectFile = event.tellSelectFile();
    }

    /**
     * 初始化全部按钮, 并添加事件响应
     * */
    private void initButton() {
        Button confirm = findViewById(R.id.btn_confirm);
        Button cancel = findViewById(R.id.btn_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                int result = FileManager.rename(selectFile, name);
                File renamedFile = new File(selectFile.getParent() + "/" + name);
                if (result == CodeConsultant.OPERATE_FAIL)
                    Toast.makeText(Rename.this, "操作失败", Toast.LENGTH_SHORT).show();
                else if (result == CodeConsultant.FILE_ALREADY_EXISTS)
                    Toast.makeText(Rename.this, "文件已存在", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(Rename.this, "操作成功", Toast.LENGTH_LONG).show();
                    EventBus.getDefault().postSticky(new RenameEvent(renamedFile));
                }
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化EditText, 并添加事件响应
     * */
    private void initEditText() {
        editText = findViewById(R.id.editext);
        editText.addTextChangedListener(new EditTextWatcher(AppManager.getContext(), editText, 20));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);
        restrictActivityWidth(0.75, Gravity.CENTER);
        initButton();
        initEditText();
        showSoftInputWhenReady(editText);
    }
}
