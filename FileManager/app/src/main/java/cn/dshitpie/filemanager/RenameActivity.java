package cn.dshitpie.filemanager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import cn.dshitpie.filemanager.utils.EditTextWatcher;

public class RenameActivity extends AppCompatActivity implements View.OnClickListener{
    EditText editText;
    Button btnConfirm, btnCancel;
    private EditTextWatcher editTextWatcher;

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

        editText = findViewById(R.id.rename_activity_edit_text);
        showSoftInputWhenReady();
        btnConfirm = findViewById(R.id.rename_activity_btn_confirm);
        btnCancel = findViewById(R.id.add_item_activity_btn_cancel);

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
                break;
            }
            case R.id.rename_activity_btn_cancel: {
                break;
            }
        }
    }
}
