package cn.dshitpie.filemanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import cn.dshitpie.filemanager.utils.CodeConsultant;
import cn.dshitpie.filemanager.utils.EditTextWatcher;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.utils.TagConsultant;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;
    private Button buttonNewFile, buttonNewDirectory, buttonCancel;
    private File nowInFile;
    private EditTextWatcher editTextWatcher;
    private FileManager fileManager;

    private void setActivitySize(double heightScale, double widthScale) {
        //设置Activity宽高
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (getWindowManager().getDefaultDisplay().getHeight() * heightScale); // 高度设置为屏幕的0.3
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * widthScale); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(params);
    }

    //自动弹出键盘
    private void showSoftInputWhenReady() {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 250);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setActivitySize(0.2, 0.7);

        editText = findViewById(R.id.add_item_activity_edit_text);
        showSoftInputWhenReady();
        editTextWatcher = new EditTextWatcher(this, editText, 20);
        editText.addTextChangedListener(editTextWatcher);

        buttonNewFile = findViewById(R.id.add_item_activity_btn_new_file);
        buttonNewDirectory = findViewById(R.id.add_item_activity_btn_new_directory);
        buttonCancel = findViewById(R.id.add_item_activity_btn_cancel);
        buttonNewFile.setOnClickListener(this);
        buttonNewDirectory.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        fileManager = new FileManager();
        Intent intent = getIntent();
        nowInFile = (File) intent.getSerializableExtra(TagConsultant.NOW_IN_FILE);
    }

    //统一的按钮点击事件处理方法
    private void dealOnClick(int fileType) {
        String editTextContent = editTextWatcher.getNowContent();
        int result = -1;
        if (CodeConsultant.TYPE_FILE == fileType) result = fileManager.newFileIn(nowInFile, editTextContent);
        else if (CodeConsultant.TYPE_DIRECTORY == fileType) result = fileManager.newDirIn(nowInFile, editTextContent);

        if (CodeConsultant.OPERATE_SUCCESS == result) Toast.makeText(this, "创建成功", Toast.LENGTH_LONG).show();
        else if (CodeConsultant.OPERATE_FAIL == result) Toast.makeText(this, "创建失败", Toast.LENGTH_LONG).show();
        else if (CodeConsultant.FILE_ALREADY_EXISTS == result) Toast.makeText(this, "文件已存在", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra(TagConsultant.FEEDBACK, result);
        intent.putExtra(TagConsultant.ITEM_NAME, editTextContent);
        setResult(CodeConsultant.ADD_ITEM_ACTIVITY, intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: break;
            case R.id.add_item_activity_btn_new_file: {
                dealOnClick(CodeConsultant.TYPE_FILE);
                finish();
                break;
            }
            case R.id.add_item_activity_btn_new_directory: {
                dealOnClick(CodeConsultant.TYPE_DIRECTORY);
                finish();
            }
            case R.id.add_item_activity_btn_cancel: finish();
        }
    }
}
