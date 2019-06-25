package cn.dshitpie.filemanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "testapp";
    private EditText editText;
    private Button buttonNewFile;
    private Button buttonNewDirectory;
    private Button buttonCancel;
    private File nowInFile;

    private void setActivityToDialogSize() {
        //设置Activity宽高
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (getWindowManager().getDefaultDisplay().getHeight() * 0.2); // 高度设置为屏幕的0.3
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * 0.65); // 宽度设置为屏幕的0.65
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
        setActivityToDialogSize();
        editText = findViewById(R.id.add_item_activity_edit_text);
        showSoftInputWhenReady();
        buttonNewFile = findViewById(R.id.add_item_activity_btn_new_file);
        buttonNewDirectory = findViewById(R.id.add_item_activity_btn_new_directory);
        buttonCancel = findViewById(R.id.add_item_activity_btn_cancel);
        editText.addTextChangedListener(new EditTextWatcher(editText, 20, this));
        buttonNewFile.setOnClickListener(this);
        buttonNewDirectory.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        Intent intent = getIntent();
        nowInFile = (File) intent.getSerializableExtra("nowInFile");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: break;
            case R.id.add_item_activity_btn_new_file: {
                break;
            }
            case R.id.add_item_activity_btn_new_directory: {

                break;
            }
            case R.id.add_item_activity_btn_cancel: {
                break;
            }
        }
    }
}

class EditTextWatcher implements TextWatcher {
    private static final String TAG = "testapp";

    // 字符个数限制
    private int limit;
    // 编辑框控件
    private EditText text;
    // 上下文对象
    private Context context;
    // 用来记录输入字符的时候光标的位置
    private int cursor = 0;
    // 用来标注输入某一内容之前的编辑框中的内容的长度
    private int before_length;

    public EditTextWatcher(EditText text, int limit, Context context) {
        this.limit = limit;
        this.text = text;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        before_length = s.length();
    }

    /**
     * s 编辑框中全部的内容 、start 编辑框中光标所在的位置（从0开始计算）、count 从手机的输入法中输入的字符个数
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        cursor = start;
        Log.d(TAG,"此时光标的位置为" + cursor);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // 这里可以知道你已经输入的字数，大家可以自己根据需求来自定义文本控件实时的显示已经输入的字符个数
        Log.d(TAG, "此时你已经输入了" + s.length());
        // 输入内容后编辑框所有内容的总长度
        int after_length = s.length();
        // 如果字符添加后超过了限制的长度，那么就移除后面添加的那一部分，这个很关键
        if (after_length > limit) {
            // 比限制的最大数超出了多少字
            int d_value = after_length - limit;
            // 这时候从手机输入的字的个数
            int d_num = after_length - before_length;
            // 需要删除的超出部分的开始位置
            int st = cursor + (d_num - d_value);
            // 需要删除的超出部分的末尾位置
            int en = cursor + d_num;
            // 调用delete()方法将编辑框中超出部分的内容去掉
            Editable s_new = s.delete(st, en);
            // 给编辑框重新设置文本
            text.setText(s_new.toString());
            // 设置光标最后显示的位置为超出部分的开始位置，优化体验
            text.setSelection(st);
            // 弹出信息提示已超出字数限制
            Toast.makeText(context, "最大字数限制为20. 您已超出最大字数限制", Toast.LENGTH_LONG).show();
        }
    }
}

