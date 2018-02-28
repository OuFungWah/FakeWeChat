package com.crazywah.fakewechat.common;

import com.crazywah.fakewechat.crazytools.codetools.Base64Coder;

/**
 * Created by FungWah on 2018/2/27.
 */

public class UrlValues {

    public static final String HEADER_KEY = "Authorization";
    public static final String HEADER_VALUES = Base64Coder.encode(AppConfig.APP_KEY + ":" + AppConfig.MASTER_SECRET);

    public static final String BASE_HOST = "https://api.im.jpush.cn";

    /**
     * 用户注册API
     */
    public static final String USER_REGISTER = BASE_HOST + "/v1/users/";


}
