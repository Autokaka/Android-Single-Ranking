package cn.dshitpie.filemanager;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import cn.dshitpie.filemanager.utils.CodeConsultant;
import cn.dshitpie.filemanager.utils.FileManager;
import cn.dshitpie.filemanager.utils.TagConsultant;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonCopy, buttonShear, buttonLink, buttonRename, buttonDelete, buttonCompress, buttonAttribute, buttonShare, buttonOpenas, buttonBookmark;
    private File nowSelectFile;
    FileManager fileManager;

    private void setActivitySize(double heightScale, double widthScale) {
        //设置Activity宽高
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (getWindowManager().getDefaultDisplay().getHeight() * heightScale); // 高度设置为屏幕的0.3
        params.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * widthScale); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setActivitySize(0.45, 0.8);
        Intent intent = getIntent();
        nowSelectFile = (File) intent.getSerializableExtra(TagConsultant.NOW_SELECT_FILE);
        fileManager = new FileManager();

        buttonCopy = findViewById(R.id.menu_activity_text_view_copy);
        buttonShear = findViewById(R.id.menu_activity_text_view_shear);
        buttonLink = findViewById(R.id.menu_activity_text_view_link);
        buttonRename = findViewById(R.id.menu_activity_text_view_rename);
        buttonDelete = findViewById(R.id.menu_activity_text_view_delete);
        buttonCompress = findViewById(R.id.menu_activity_text_view_compress);
        buttonAttribute = findViewById(R.id.menu_activity_text_view_attribute);
        buttonShare = findViewById(R.id.menu_activity_text_view_share);
        buttonOpenas = findViewById(R.id.menu_activity_text_view_openas);
        buttonBookmark = findViewById(R.id.menu_activity_text_view_bookmark);
        buttonCopy.setOnClickListener(this);
        buttonShear.setOnClickListener(this);
        buttonLink.setOnClickListener(this);
        buttonRename.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonCompress.setOnClickListener(this);
        buttonAttribute.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonOpenas.setOnClickListener(this);
        buttonBookmark.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: break;
            case R.id.menu_activity_text_view_copy: {
                break;
            }
            case R.id.menu_activity_text_view_shear: {
                break;
            }
            case R.id.menu_activity_text_view_link: {
                break;
            }
            case R.id.menu_activity_text_view_rename: {
                Intent intent = new Intent(MenuActivity.this, RenameActivity.class);
                intent.putExtra(TagConsultant.NOW_SELECT_FILE, nowSelectFile);
                startActivityForResult(intent, CodeConsultant.RENAME_ACTIVITY);
                break;
            }
            case R.id.menu_activity_text_view_delete: {
                int result = fileManager.delete(nowSelectFile);
                if (CodeConsultant.OPERATE_SUCCESS == result) Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
                else if (CodeConsultant.OPERATE_FAIL == result) Toast.makeText(this, "删除失败", Toast.LENGTH_LONG).show();
                else if (CodeConsultant.FILE_NOT_EXISTS == result) Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra(TagConsultant.FEEDBACK, result);
                setResult(CodeConsultant.MENU_ACTIVITY, intent);
                finish();
                break;
            }
            case R.id.menu_activity_text_view_compress: {
                break;
            }
            case R.id.menu_activity_text_view_attribute: {
                break;
            }
            case R.id.menu_activity_text_view_share: {
                break;
            }
            case R.id.menu_activity_text_view_openas: {
                break;
            }
            case R.id.menu_activity_text_view_bookmark: {
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            default: break;
            case CodeConsultant.RENAME_ACTIVITY: {
                break;
            }
        }
    }
}
