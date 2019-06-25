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

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "testapp";
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
        nowInFile = (File) intent.getSerializableExtra("nowInFile");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: break;
            case R.id.add_item_activity_btn_new_file: {
                String editTextContent = editTextWatcher.getNowContent();
                int result = fileManager.newFileIn(nowInFile, editTextContent);
                if (0 == result) Toast.makeText(this, "文件创建成功", Toast.LENGTH_LONG).show();
                else if (-1 == result) Toast.makeText(this, "文件创建失败", Toast.LENGTH_LONG).show();
                else if (1 == result) Toast.makeText(this, "文件已存在", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("feedback", result);
                intent.putExtra("itemName", editTextContent);
                setResult(CodeConsultant.ADD_ITEM_ACTIVITY_CODE, intent);
                finish();
                break;
            }
            case R.id.add_item_activity_btn_new_directory: {
                String editTextContent = editTextWatcher.getNowContent();
                int result = fileManager.newDirectoryIn(nowInFile, editTextContent);
                if (0 == result) Toast.makeText(this, "文件夹创建成功", Toast.LENGTH_LONG).show();
                else if (-1 == result) Toast.makeText(this, "新建文件夹失败", Toast.LENGTH_LONG).show();
                else if (1 == result) Toast.makeText(this, "文件夹已存在", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("feedback", result);
                intent.putExtra("itemName", editTextContent);
                setResult(CodeConsultant.ADD_ITEM_ACTIVITY_CODE, intent);
                finish();
            }
            case R.id.add_item_activity_btn_cancel: finish();
        }
    }
}
