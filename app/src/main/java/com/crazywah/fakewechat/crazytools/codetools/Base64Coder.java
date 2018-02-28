package com.crazywah.fakewechat.crazytools.codetools;

import android.util.Log;

/**
 * Created by FungWah on 2018/2/27.
 */

public class Base64Coder {

    private static final String TAG = "Base64Coder";

    /**
     * base64 编码
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        String asB64 = null;
        try {
            asB64 = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "encode: 编码出错");
        }
        // 编码

        return asB64;
    }

    /**
     * Base64解码
     * @param str
     * @return
     */
    public static String deCode(String str) {
        String result = null;
        try {
            // 解码
            byte[] asBytes = Base64.getDecoder().decode(str);
            result = new String(asBytes, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "deCoder: 解码出错");
        }
        return result;
    }

}
