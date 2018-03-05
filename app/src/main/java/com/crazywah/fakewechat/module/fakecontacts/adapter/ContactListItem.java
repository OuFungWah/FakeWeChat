package com.crazywah.fakewechat.module.fakecontacts.adapter;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/4.
 */

public class ContactListItem {

    private String firstChar;
    private UserInfo userInfo;

    public ContactListItem(String firstChar) {
        this.firstChar = firstChar;
        this.userInfo = null;
    }

    public ContactListItem(UserInfo userInfo) {
        this.firstChar = null;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}
