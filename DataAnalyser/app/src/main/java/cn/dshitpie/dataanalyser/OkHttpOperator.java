package cn.dshitpie.dataanalyser;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.StringReader;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpOperator {
    private static final String TAG = "testapp";
    protected static final int JSON = 1;
    protected static final int XML = 2;
    protected static final int PHP = 3;
    protected static final int IMG = 4;
    private String responseData;

    OkHttpOperator() {
        responseData = "";
    }

    //Okhttp请求发送器
    protected String sendRequestForResult(int requestDataType) {
        responseData = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "sendRequestForResult1 -> Running!");
                    OkHttpClient client = new OkHttpClient();
                    Request request = generateRequest(requestDataType);
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                    Log.d(TAG, "sendRequestForResult1 -> 返回结果1:\n" + responseData);
                    responseData = parseResponse(responseData, requestDataType);
                    Log.d(TAG, "sendRequestForResult1 -> Done!");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while ("".equals(responseData)) continue;
        Log.d(TAG, "检查返回结果1: \n" + responseData);
        return responseData;
    }
    protected String sendRequestForResult(int requestDataType, String imagePath) {
        responseData = "";
        if (IMG != requestDataType) {
            Log.d(TAG,"sendRequestForResult2 -> 解析类型错误!");
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Log.d(TAG, "sendRequestForResult2 -> Running!");
                        Log.d(TAG, "获取图片路径: " + imagePath);
                        Request request = generateRequest(IMG, imagePath);
                        Response response = okHttpClient.newCall(request).execute();
                        responseData = response.body().string();
                        Log.d(TAG, "sendRequestForResult2 -> 返回结果2: \n" + responseData);
                        responseData = parseResponse(responseData, PHP);
                        Log.d(TAG, "sendRequestForResult2 -> Done!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        while ("".equals(responseData)) continue;
        Log.d(TAG, "检查返回结果2: \n" + responseData);
        return responseData;
    }

    //依据场景的request生成器
    protected Request generateRequest(int requestDataType) {
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
                url += "connector.php";
                break;
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d(TAG, "generateRequest -> Done!");
        return request;
    }
    protected Request generateRequest(int requestDataType, String imagePath) {
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
    protected String parseResponse(String responseData, int responseDataType) {
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
