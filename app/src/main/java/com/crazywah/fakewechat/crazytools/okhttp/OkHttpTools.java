package com.crazywah.fakewechat.crazytools.okhttp;

import android.support.annotation.Nullable;
import android.util.Log;


import com.crazywah.fakewechat.common.UrlValues;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by FungWah on 2018/2/27.
 */

public class OkHttpTools {

    public static final String TAG = "OkHttpTools";

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charser=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public static final MediaType MEDIA_TYPE_XML = MediaType.parse("application/xml; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
    public static final MediaType MEDIA_TYPE_GIF = MediaType.parse("image/gif");
    public static final MediaType MEDIA_TYPE_MP4 = MediaType.parse("video/mp4");

    /**
     * 同步Get 方法
     *
     * @param url
     * @param callBack
     */
    public static void synGet(String url,@Nullable String headerKey, @Nullable String headerValues, MyCallBack callBack) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().addHeader(headerKey, headerKey).url(url).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                callBack.onSuccess(response.body().string());
            } else {
                callBack.onFail();
            }
        } catch (IOException e) {
            Log.d(TAG, "异常:" + e.toString());
        }
    }

    public static void AsynGet(String url,@Nullable String headerKey, @Nullable String headerValues, final MyCallBack callBack) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().addHeader(headerKey, headerKey).url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onSuccess(response.body().string());
            }
        });
    }

    /**
     * 自定义请求的同步请求
     * 新建Request例如:Request request = new Request.Builder()
     * .url("https://api.github.com/repos/square/okhttp/issues")
     * .header("User-Agent", "OkHttp Headers.java")
     * .addHeader("Accept", "application/json; q=0.5")
     * .addHeader("Accept", "application/vnd.github.v3+json")
     * .build();
     * * 需要注意的是，4.0以后不允许在主线程中进行网络请求，
     * 以免网络请求造成主线程假死，所以需要在另外自己新设
     * 的子线程中进行
     *
     * @param request  自定义请求
     * @param callBack
     */
    public static void synRequest(Request request, MyCallBack callBack) throws JSONException {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                callBack.onSuccess(response.body().string());
            } else {
                callBack.onFail();
            }
        } catch (IOException e) {
            Log.d(TAG, "异常:" + e.toString());
        }
    }

    /**
     * 自定义请求的异步请求
     * 新建Request例如:Request request = new Request.Builder()
     * .url("https://api.github.com/repos/square/okhttp/issues")
     * .header("User-Agent", "OkHttp Headers.java")
     * .addHeader("Accept", "application/json; q=0.5")
     * .addHeader("Accept", "application/vnd.github.v3+json")
     * .build();
     *
     * @param request  自定义的请求
     * @param callBack
     */
    public static void asynRequest(Request request, final MyCallBack callBack) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    callBack.onSuccess(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 同步Post請求
     * * 需要注意的是，4.0以后不允许在主线程中进行网络请求，
     * 以免网络请求造成主线程假死，所以需要在另外自己新设
     * 的子线程中进行
     *
     * @param requestBody 请求体
     * @param url         链接
     * @param callBack    回调接口
     */
    public static void synPost(RequestBody requestBody,@Nullable String headerKey, @Nullable String headerValues, String url, MyCallBack callBack) throws JSONException {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
        Request request = null;
        if(headerKey!=null&&headerValues!=null){
            request = new Request.Builder().url(url).addHeader(headerKey,headerValues).post(requestBody).build();
        }else{
            request = new Request.Builder().url(url).post(requestBody).build();
        }
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                callBack.onSuccess(response.body().string());
            } else {
                callBack.onFail();
            }
        } catch (IOException e) {
            Log.d(TAG, "同步POST异常:" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 异步POST请求
     *
     * @param requestBody  请求体
     * @param url          链接
     * @param callBack     回调接口
     * @param headerKey    Http头的键
     * @param headerValues Http头的值
     */
    public static void asynPost(RequestBody requestBody, @Nullable String headerKey, @Nullable String headerValues, String url, final MyCallBack callBack) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build();
        Request request = null;
        if(headerKey!=null&&headerValues!=null){
            request = new Request.Builder().url(url).addHeader(headerKey,headerValues).post(requestBody).build();
        }else{
            request = new Request.Builder().url(url).post(requestBody).build();
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFail();
                Log.d(TAG, "异步POST异常:" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    callBack.onSuccess(response.body().string());
                } catch (Exception
                        e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
