package cn.dshitpie.dataanalyser;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zhihu.matisse.*;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import org.json.*;
import org.xmlpull.v1.*;
import java.io.*;
import java.util.*;
import okhttp3.*;
import cn.dshitpie.dataanalyser.OkHttpOperator;

public class TestModeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "testapp";
    private static final int MATISSE_REQUEST_CODE = 100;
    private OkHttpOperator myOperator;
    TextView responseText;
    ImageView selectedImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);
        myOperator = new OkHttpOperator();
        applyForPermission();
        Button chooseImage = (Button) findViewById(R.id.choose_img);
        Button getJSONData = (Button) findViewById(R.id.get_json_data);
        Button getXMLData = (Button) findViewById(R.id.get_xml_data);
        Button getPHPData = (Button) findViewById(R.id.get_php_data);
        selectedImgView = (ImageView) findViewById(R.id.selected_image);
        responseText = (TextView) findViewById(R.id.response_text);
        chooseImage.setOnClickListener(this);
        getJSONData.setOnClickListener(this);
        getXMLData.setOnClickListener(this);
        getPHPData.setOnClickListener(this);
    }

    //集中处理按钮点击请求
    @Override
    public void onClick(View v) {
        String responseData = "";
        showResponse("(空)");
        switch (v.getId()) {
            default:
                break;
            case R.id.get_json_data: {
                responseData = myOperator.sendRequestForResult(myOperator.JSON);
                showResponse(responseData);
                break;
            }
            case R.id.get_xml_data: {
                responseData = myOperator.sendRequestForResult(myOperator.XML);
                showResponse(responseData);
                break;
            }
            case R.id.get_php_data: {
                responseData = myOperator.sendRequestForResult(myOperator.PHP);
                showResponse(responseData);
                break;
            }
            case R.id.choose_img: {
                Matisse.from(TestModeActivity.this)
                        .choose(MimeType.ofAll())//图片类型
                        .countable(false)//true:选中后显示数字;false:选中后显示对号
                        .maxSelectable(1)//可选的最大数
                        .capture(true)//选择照片时，是否显示拍照
                        .captureStrategy(new CaptureStrategy(true, "cn.dshitpie.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        .imageEngine(new MyGlideEngine())//图片加载引擎
                        .forResult(MATISSE_REQUEST_CODE);
                break;
            }
        }
    }

    //申请权限
    private void applyForPermission() {
        Log.d(TAG, "applyForPermission -> Running!");
        ArrayList<String> permissionList = new ArrayList<String>();
        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        for (int i = 0 ; i <permissionList.size(); i++) {
            int permissionStatus = ContextCompat.checkSelfPermission(TestModeActivity.this, permissionList.get(i));
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TestModeActivity.this, new String[]{ permissionList.get(i) }, 1);
            }
        }
        Log.d(TAG, "applyForPermission -> Done!");
    }

    //处理权限申请结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "[请注意] App权限未全部允许, 可能无法正常工作", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }

    //在UI线程展示解析结果
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MATISSE_REQUEST_CODE && resultCode == RESULT_OK) {
            List<Uri> result = Matisse.obtainResult(data);
            Glide.with(this)
                    .asBitmap() // some .jpeg files are actually gif
                    .load(result.get(0))
                    .apply(new RequestOptions() {{
                        override(Target.SIZE_ORIGINAL);
                    }})
                    .into(selectedImgView);
            String filePath = myOperator.getImgRealPath(this, result.get(0));
            showResponse(filePath);
            String responseData = myOperator.sendRequestForResult(myOperator.IMG, filePath);
            showResponse(responseData);
        }
    }
}