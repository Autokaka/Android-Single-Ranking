package com.example.servicebestpractice;

import android.os.*;
import java.io.*;
import okhttp3.*;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    /*
    * 定义了几个tag用于标识下载的状态：
    * */
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    /*
    * 引入DownloadListener接口并在后面重写其方法实现用于本class调取执行
    * */
    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask (DownloadListener listener) {
        this.listener = listener;
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    /*
    * 获取下载文件的总长度：
    * 发送下载请求，请求的将结果以response储存，最终返回该response的contentLength
    * */
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

    /*
    * 在后台执行具体的下载逻辑：
    * params[]是获取的URL地址
    * 通过URL解析文件的下载名并存入fileName中
    * 设定下载目录为/sdcard/Download并将该路径存入directory
    * 由directory+fileName将储存文件的完整路径存入file中
    * 先行检测是否需要断点续传：如果本地已经有文件，就将该文件的现存大小记录在downloadedLength里面
    * 由getContentLength方法获取将要下载文件的大小，用于和downloadedLength比对实现断点续传
    * 如果将要下载的文件本来就是空的，那就是无效文件，返回下载失败的tag；而如果将要下载的文件长度和本地已存文件大小完全相同，说明文件已经下载到本地，直接返回成功tag，就不用下载了
    * 如果上述return并未执行，也就是说文件既是有效文件，又还没有下载完毕，那么发送下载请求（加入Header防止可能是断点续传的情况发生，这种情况完全取决于本地已存文件的大小）
    * 获取下载请求的结果，并在其有效的情况下执行下载操作。下载过程中，如果是正常下载，就进行文件读写操作并用publishProgress方法展示下载进度条；而如果文件没有下载完，并且下载器状态被设置为isCanceled，那么就返回下载取消tag；同理下载暂停情况。
    * */
    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadedLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    /*
    * 更新当前下载状态的方法：
    * 方法获取当前线程下载进度，如果下载进度大于本地存储进度了，就要更新本地存储状态，并将其展示在通知上
    * */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    /*
    * 监听线程执行doInBackground过后返回的结束状态，并将当前的状态用DownloadListener内的方法（成功，失败，暂停，取消）做出对应处理
    * */
    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }
}
