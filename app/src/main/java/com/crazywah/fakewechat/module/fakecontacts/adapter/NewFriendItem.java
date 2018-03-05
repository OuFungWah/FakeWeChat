package com.crazywah.fakewechat.module.fakecontacts.adapter;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/4.
 */

public class NewFriendItem {

    private String noteTextStr;

    private UserInfo userInfo;

    public NewFriendItem(String noteTextStr, UserInfo userInfo) {
        this.noteTextStr = noteTextStr;
        this.userInfo = userInfo;
    }

    public String getNoteTextStr() {
        return noteTextStr;
    }

    public void setNoteTextStr(String noteTextStr) {
        this.noteTextStr = noteTextStr;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
