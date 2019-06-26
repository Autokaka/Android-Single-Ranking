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
import cn.dshitpie.filemanager.utils.ConsoleDebugger;
import cn.dshitpie.filemanager.utils.EditTextWatcher;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.utils.TagConsultant;

public class RenameActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editText;
    private Button btnConfirm, btnCancel;
    private EditTextWatcher editTextWatcher;
    private FileManager fileManager;
    private File nowSelectFile;

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
        setContentView(R.layout.activity_rename);
        setActivitySize(0.2, 0.7);
        Intent intent = getIntent();
        nowSelectFile = (File) intent.getSerializableExtra(TagConsultant.NOW_SELECT_FILE);
        fileManager = new FileManager();

        editText = findViewById(R.id.rename_activity_edit_text);
        showSoftInputWhenReady();
        btnConfirm = findViewById(R.id.rename_activity_btn_confirm);
        btnCancel = findViewById(R.id.rename_activity_btn_cancel);

        editTextWatcher = new EditTextWatcher(this, editText, 20);
        editText.addTextChangedListener(editTextWatcher);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: break;
            case R.id.rename_activity_btn_confirm: {
                String nowContent = editTextWatcher.getNowContent();
                int result = fileManager.rename(nowSelectFile, nowContent);
                if (CodeConsultant.OPERATE_SUCCESS == result) Toast.makeText(this, "重命名成功", Toast.LENGTH_LONG).show();
                else if (CodeConsultant.OPERATE_FAIL == result) Toast.makeText(this, "重命名失败", Toast.LENGTH_LONG).show();
                else if (CodeConsultant.FILE_NOT_EXISTS == result) Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra(TagConsultant.FEEDBACK, result);
                intent.putExtra(TagConsultant.ITEM_NAME, nowContent);
                setResult(CodeConsultant.RENAME_ACTIVITY, intent);
                finish();
                break;
            }
            case R.id.rename_activity_btn_cancel: {
                finish();
                break;
            }
        }
    }
}
