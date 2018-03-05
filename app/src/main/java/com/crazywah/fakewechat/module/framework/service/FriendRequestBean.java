package com.crazywah.fakewechat.module.framework.service;

import com.crazywah.fakewechat.module.fakecontacts.adapter.NewFriendItem;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FungWah on 2018/3/5.
 */

public class FriendRequestBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private int newFriendRequestNum = 0;

    private List<String> usernameList = new ArrayList<>();

    private HashMap<String, String> nickname = new HashMap<>();

    private HashMap<String, String> reasonMap = new HashMap<>();

    private HashMap<String, File> avatarMap = new HashMap<>();

    private HashMap<String, Boolean> isFriendMap = new HashMap<>();

    public FriendRequestBean() {

    }

    public int getNewFriendRequestNum() {
        return newFriendRequestNum;
    }

    public void setNewFriendRequestNum(int newFriendRequestNum) {
        this.newFriendRequestNum = newFriendRequestNum;
    }

    public List<String> getUsernameList() {
        return usernameList;
    }

    public void setUsernameList(List<String> usernameList) {
        this.usernameList = usernameList;
    }

    public HashMap<String, String> getReasonMap() {
        return reasonMap;
    }

    public void setReasonMap(HashMap<String, String> reasonMap) {
        this.reasonMap = reasonMap;
    }

    public HashMap<String, File> getAvatarMap() {
        return avatarMap;
    }

    public void setAvatarMap(HashMap<String, File> avatarMap) {
        this.avatarMap = avatarMap;
    }

    public HashMap<String, String> getNickname() {
        return nickname;
    }

    public void setNickname(HashMap<String, String> nickname) {
        this.nickname = nickname;
    }

    public HashMap<String, Boolean> getIsFriendMap() {
        return isFriendMap;
    }

    public void setIsFriendMap(HashMap<String, Boolean> isFriendMap) {
        this.isFriendMap = isFriendMap;
    }

}
