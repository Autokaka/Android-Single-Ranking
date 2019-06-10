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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "testapp";
    private static final int MATISSE_REQUEST_CODE = 100;
    protected static final int JSON = 1;
    protected static final int XML = 2;
    protected static final int PHP = 3;
    protected static final int IMG = 4;
    private MyRecyclerViewAdapter adapter;
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
        Log.d(TAG, "-----MainActivity-----");
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
        btnAdd.setOnClickListener(this);
        Log.d(TAG, "-----Done-----");
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
            String filePath = getImgRealPath(this, result.get(0));
            CardInfo card = new CardInfo(filePath);
            cardInfoList.add(card);
            sendRequest(cardInfoList.size() - 1, IMG, filePath);
            adapter.notifyDataSetChanged();
        }
        Log.d(TAG, "onActivityResult -> Done!");
    }

    //在UI线程更新卡片识别结果
    public void updateCardCode(int cardIndex, String code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardInfoList.get(cardIndex).code = code;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: {
                break;
            }
            case R.id.about: {
                Toast.makeText(this, "页面建设中...=A=", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.test_mode: {
                Intent intent = new Intent(MainActivity.this, TestModeActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    //请求发送器
    protected void sendRequest(int cardIndex, int requestDataType, String imagePath) {
        Log.d(TAG, "-----sendRequestForResult(图片模式)-----");
        if (IMG != requestDataType) {
            Log.d(TAG,"解析类型错误!");
        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            Log.d(TAG, "获取图片路径: " + imagePath);
            Request request = generateRequest(IMG, imagePath);
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "请求发送失败: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d(TAG, "请求发送成功: " + responseData);
                    updateCardCode(cardIndex, responseData);
                }
            });
        }
        Log.d(TAG, "-----Done-----");
    }

    //请求生成器
    protected Request generateRequest(int requestDataType, String imagePath) {
        Log.d(TAG, "-----generateRequest-----");
        String url = "http://134.175.233.183/CardInfo/uploadHandler.php";
        Request request = null;
        if (IMG != requestDataType) {
            Log.d(TAG,"解析类型错误!");
        } else {
            File file = new File(imagePath);
            RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), image)
                    .build();
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }
        Log.d(TAG, "-----Done-----");
        return request;
    }

    //获取图片文件真实路径
    protected String getImgRealPath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (null == scheme || ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (null != uri) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (!file.exists()) {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    file = new File(storageDir, imageName);
                }
                realPath = file.getAbsolutePath();
            }
        }
        return realPath;
    }
}
