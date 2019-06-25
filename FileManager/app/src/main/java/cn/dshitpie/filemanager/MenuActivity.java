package cn.dshitpie.filemanager;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "testapp";
    private TextView textViewCopy, textViewShear;
    private Drawable drawableCopy, drawableShear;

    private void setActivitySize(double heightScale, double widthScale) {
        //设置Activity宽高
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (getWindowManager().getDefaultDisplay().getHeight() * heightScale); // 高度设置为屏幕的0.3
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * widthScale); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(params);
    }

    private void initTextView() {
        textViewCopy = findViewById(R.id.menu_activity_text_view_copy);
        textViewShear = findViewById(R.id.menu_activity_text_view_shear);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setActivitySize(0.45, 0.8);
        initTextView();
    }
}
