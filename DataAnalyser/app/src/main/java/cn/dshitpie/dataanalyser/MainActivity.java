package cn.dshitpie.dataanalyser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "testapp";
    private static final int MATISSE_REQUEST_CODE = 100;
    private MyRecyclerViewAdapter adapter;
    private OkHttpOperator myOperator;
    List<CardInfo> cardInfoList = new ArrayList<>();

    //申请权限
    private void applyForPermission() {
        Log.d(TAG, "applyForPermission -> Running!");
        ArrayList<String> permissionList = new ArrayList<String>();
        permissionList.add(Manifest.permission.INTERNET);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        for (int i = 0 ; i <permissionList.size(); i++) {
            int permissionStatus = ContextCompat.checkSelfPermission(MainActivity.this, permissionList.get(i));
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{ permissionList.get(i) }, 1);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate -> Running!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyForPermission();
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.add_card);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //实例化MyAdapter并传入cardInfoList对象
        adapter = new MyRecyclerViewAdapter(cardInfoList);
        //为RecyclerView对象mRecyclerView设置adapter
        mRecyclerView.setAdapter(adapter);
        myOperator = new OkHttpOperator();
        btnAdd.setOnClickListener(this);
        Log.d(TAG, "onCreate -> Done!");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: {
                break;
            }
            case R.id.add_card: {
                Matisse.from(MainActivity.this)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult -> Running!");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MATISSE_REQUEST_CODE && resultCode == RESULT_OK) {
            List<Uri> result = Matisse.obtainResult(data);
            String filePath = myOperator.getImgRealPath(this, result.get(0));
            String responseData = myOperator.sendRequestForResult(myOperator.IMG, filePath);
            CardInfo card = new CardInfo("测试卡片" + (cardInfoList.size() + 1), responseData, filePath);
            cardInfoList.add(card);
            adapter.notifyDataSetChanged();
        }
        Log.d(TAG, "onActivityResult -> Done!");
    }
}
