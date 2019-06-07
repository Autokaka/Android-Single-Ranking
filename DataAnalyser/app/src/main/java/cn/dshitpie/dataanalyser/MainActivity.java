package cn.dshitpie.dataanalyser;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "testapp";
    private static final int MATISSE_REQUEST_CODE = 100;
    private static final int JSON = 1;
    private static final int XML = 2;
    private static final int PHP = 3;
    private static final int IMG = 4;
    TextView responseText;
    ImageView selectedImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        showResponse("(空)");
        switch (v.getId()) {
            default:
                break;
            case R.id.get_json_data: {
                sendRequestWithOkHttp(JSON);
                break;
            }
            case R.id.get_xml_data: {
                sendRequestWithOkHttp(XML);
                break;
            }
            case R.id.get_php_data: {
                sendRequestWithOkHttp(PHP);
                break;
            }
            case R.id.choose_img: {
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

    //Okhttp请求发送器
    private void sendRequestWithOkHttp(int requestDataType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "sendRequestWithOkHttp -> Running!");
                    OkHttpClient client = new OkHttpClient();
                    Request request = generateRequest(requestDataType);
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d(TAG, "sendRequestWithOkHttp -> Original data: \n" + responseData);
                    responseData = parseResponse(responseData, requestDataType);
                    showResponse(responseData);
                    Log.d(TAG, "sendRequestWithOkHttp -> Done!");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void sendRequestWithOkHttp(int requestDataType, String imagePath) {
        if (IMG != requestDataType) {
            Log.d(TAG,"sendRequestWithOkHttp -> 解析类型错误!");
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Log.d(TAG, "uploadImageWithOkHttp -> Running!");
                        Log.d(TAG, "uploadImageWithOkHttp -> imagePath: " + imagePath);
                        Request request = generateRequest(IMG, imagePath);
                        Response response = okHttpClient.newCall(request).execute();
                        String responseData = response.body().string();
                        Log.d(TAG, "sendRequestWithOkHttp -> Original data: \n" + responseData);
                        responseData = parseResponse(responseData, PHP);
                        showResponse(responseData);
                        Log.d(TAG, "sendRequestWithOkHttp -> Done!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //依据场景的request生成器
    private Request generateRequest(int requestDataType) {
        Log.d(TAG, "generateRequest -> Running!");
        String url = "http://134.175.233.183/CardInfo/";
        switch (requestDataType) {
            default: {
                Log.d(TAG, "generateRequest -> 解析类型错误!");
                break;
            }
            case JSON: {
                url += "CardInfo_test.json";
                break;
            }
            case XML: {
                url += "CardInfo_test.xml";
                break;
            }
            case PHP: {
                url += "CardInfo_test.php";
                break;
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d(TAG, "generateRequest -> Done!");
        return request;
    }
    private Request generateRequest(int requestDataType, String imagePath) {
        Log.d(TAG, "generateRequest -> Running!");
        String url = "http://134.175.233.183/CardInfo/uploadHandler.php";
        Request request = null;
        if (IMG != requestDataType) {
            Log.d(TAG,"generateRequest -> 解析类型错误!");
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
        Log.d(TAG, "generateRequest -> Done!");
        return request;
    }

    //依据场景的response结果解析器
    private String parseResponse(String responseData, int responseDataType) {
        Log.d(TAG, "parseResponse -> Running!");
        String returnData = "";
        switch (responseDataType) {
            default: {
                Log.d(TAG, "parseResponse -> 不支持的文件解析类型!");
                break;
            }
            case JSON: {
                returnData = "";
                String jsonData = responseData;
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String version = jsonObject.getString("version");
                        Log.d(TAG, "id: " + id);
                        Log.d(TAG, "name: " + name);
                        Log.d(TAG, "version: " + version);
                        returnData += "id: "+ id + "\n";
                        returnData += "name: "+ name + "\n";
                        returnData += "version: "+ version + "\n\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case XML: {
                returnData = "";
                String xmlData = responseData;
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = factory.newPullParser();
                    xmlPullParser.setInput(new StringReader(xmlData));
                    int eventType = xmlPullParser.getEventType();
                    String id = "";
                    String name = "";
                    String version = "";
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String nodeName = xmlPullParser.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG: {
                                if ("id".equals(nodeName)) {
                                    id = xmlPullParser.nextText();
                                    returnData += "id: " + id + "\n";
                                } else if ("name".equals(nodeName)) {
                                    name = xmlPullParser.nextText();
                                    returnData += "name: " + name + "\n";
                                } else if ("version".equals(nodeName)) {
                                    version = xmlPullParser.nextText();
                                    returnData += "version: " + version + "\n\n";
                                }
                                break;
                            }
                            case XmlPullParser.END_TAG: {
                                if ("app".equals(nodeName)) {
                                    Log.d(TAG, "id: " + id);
                                    Log.d(TAG, "name: " + name);
                                    Log.d(TAG, "version: " + version);
                                }
                                break;
                            }
                            default:
                                break;
                        }
                        eventType = xmlPullParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case PHP: {
                returnData = responseData;
                break;
            }
        }
        Log.d(TAG, "parseResponse -> Done!");
        if ("".equals(returnData)) return "(空)";
        return returnData;
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

    //获取图片文件真实路径
    public static String getImgRealPath(Context context, Uri uri) {
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
            String filePath = getImgRealPath(this, result.get(0));
            showResponse(filePath);
            sendRequestWithOkHttp(IMG, filePath);
        }
    }
}