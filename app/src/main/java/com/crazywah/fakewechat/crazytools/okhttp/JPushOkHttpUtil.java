package com.crazywah.fakewechat.crazytools.okhttp;

import android.support.annotation.Nullable;

import com.crazywah.fakewechat.common.UrlValues;
import com.crazywah.fakewechat.crazytools.codetools.Base64Coder;

import org.json.JSONException;

import okhttp3.RequestBody;

/**
 * Created by FungWah on 2018/2/27.
 */

public class JPushOkHttpUtil {

    public static void synPost(RequestBody requestBody, String url, MyCallBack callBack) throws JSONException {
        OkHttpTools.synPost(requestBody, UrlValues.HEADER_KEY, UrlValues.HEADER_VALUES, url, callBack);
    }

    public static void asynPost(RequestBody requestBody, String url, MyCallBack callBack) {
        OkHttpTools.asynPost(requestBody, UrlValues.HEADER_KEY, UrlValues.HEADER_VALUES, url, callBack);
    }
}
