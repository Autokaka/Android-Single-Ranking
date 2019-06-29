package cn.dshitpie.filemanager2.activity;

import android.os.Bundle;
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
import cn.dshitpie.filemanager2.event.NewBuildEvent;
import cn.dshitpie.filemanager2.event.ToolbarMenuEvent;
import cn.dshitpie.filemanager2.utils.CodeConsultant;
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
        final Button btnNewFile = findViewById(R.id.btn_file);
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnNewDir = findViewById(R.id.btn_dir);
        btnNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                int result = FileManager.newFileIn(inFile, name);
                File newFile = new File(inFile.getAbsolutePath() + "/" + name);
                if (result == CodeConsultant.OPERATE_FAIL)
                    Toast.makeText(NewBuild.this, "操作失败", Toast.LENGTH_LONG).show();
                else if (result == CodeConsultant.FILE_ALREADY_EXISTS)
                    Toast.makeText(NewBuild.this, "文件已存在", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(NewBuild.this, "操作成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky(new NewBuildEvent(newFile));
                }
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
                String name = editText.getText().toString();
                int result = FileManager.newDirIn(inFile, name);
                File newDir = new File(inFile.getAbsolutePath() + "/" + name);
                if (result == CodeConsultant.OPERATE_FAIL)
                    Toast.makeText(NewBuild.this, "操作失败", Toast.LENGTH_LONG).show();
                else if (result == CodeConsultant.FILE_ALREADY_EXISTS)
                    Toast.makeText(NewBuild.this, "文件已存在", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(NewBuild.this, "操作成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky(new NewBuildEvent(newDir));
                }
                finish();
            }
        });
    }

    /**
     * 初始化EditText及其定义的输入行为监视器
     * */
    private void initEditText() {
        editText = findViewById(R.id.editext);
        editText.addTextChangedListener(new EditTextWatcher(this, editText, 20));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_build);
        restrictActivityWidth(0.75, Gravity.CENTER);
        initButton();
        initEditText();
        showSoftInputWhenReady(editText);
    }
}
