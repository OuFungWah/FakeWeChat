package com.crazywah.fakewechat.common.util;

import android.content.Context;
import android.util.Log;

import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.module.fakecontacts.adapter.NewFriendItem;
import com.crazywah.fakewechat.module.framework.service.FriendRequestBean;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/6.
 */

public class DataToFileUtil {

    private static final String TAG = "DataToFileUtil";

    private static List<OnNewFriendSaveListener> onNewFriendSaveListeners = new ArrayList<>();

    private static Context context;

    public static void init(Context context) {
        DataToFileUtil.context = context;
    }

    /**
     * @param username
     * @param reason
     */
    public static void saveNewFriendRequest(final String username, final String reason) {
        FriendRequestBean bean = null;
        ObjectInputStream in = null;
        try {
            // TODO: 2018/3/6 Android IO Serializable
            in = new ObjectInputStream(context.openFileInput(AppConfig.FILE_NEW_FRIEND_REQUEST));
            bean = (FriendRequestBean) in.readObject();
            in.close();
            Log.d(TAG, "saveNewFriendRequest: 文件读取成功");
        } catch (Exception e) {
            bean = new FriendRequestBean();
        } finally {
            getUserAndSave(bean, username, reason);
        }
    }

    /**
     * 把本次的好友申请存到文件中（如果文件中没有来自同一用户的请求）
     *
     * @param bean
     * @param username
     * @param reason
     */
    private static void getUserAndSave(final FriendRequestBean bean, final String username, final String reason) {
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (s.equals("Success")) {
                    boolean isAlreadyHad = false;
                    for (int index = 0; index < bean.getUsernameList().size(); index++) {
                        if (bean.getUsernameList().get(index).equals(username)) {
                            //如果列表中不存在该用户
                            isAlreadyHad = true;
                        }
                    }
                    if (!isAlreadyHad) {
                        bean.setNewFriendRequestNum(bean.getNewFriendRequestNum() + 1);
                        bean.getUsernameList().add(username);
                        bean.getReasonMap().put(username, reason);
                        bean.getAvatarMap().put(username,userInfo.getAvatarFile());
                        bean.getNickname().put(username,userInfo.getNickname());
                        bean.getIsFriendMap().put(username,userInfo.isFriend());
                        ObjectOutputStream out = null;
                        try {
                            out = new ObjectOutputStream(context.openFileOutput(AppConfig.FILE_NEW_FRIEND_REQUEST, Context.MODE_PRIVATE));
                            out.writeObject(bean);
                            for (int index = 0; index < onNewFriendSaveListeners.size(); index++) {
                                onNewFriendSaveListeners.get(index).onNewFriendSave();
                            }
                            out.close();
                            Log.d(TAG, "gotResult: 存储文件成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     *
     */
    public static void initNewFriendNum() {
        try {
            ObjectInputStream in = new ObjectInputStream(context.openFileInput(AppConfig.FILE_NEW_FRIEND_REQUEST));
            FriendRequestBean bean = (FriendRequestBean) in.readObject();
            bean.setNewFriendRequestNum(0);
            ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(AppConfig.FILE_NEW_FRIEND_REQUEST, Context.MODE_PRIVATE));
            out.writeObject(bean);
            in.close();
            out.close();
            for (int i = 0; i < onNewFriendSaveListeners.size(); i++) {
                onNewFriendSaveListeners.get(i).onNewFriendSave();
            }
            Log.d(TAG, "initNewFriendNum: 新好友请求数清零成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 获取从文件中 FriendRequestBean
     *
     * @return
     */
    public static FriendRequestBean getFriendRequestFromFile() {
        FriendRequestBean bean = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(context.openFileInput(AppConfig.FILE_NEW_FRIEND_REQUEST));
            Log.d(TAG, "getFriendRequestFromFile: 存在该文件 " + AppConfig.FILE_NEW_FRIEND_REQUEST);
            bean = (FriendRequestBean) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bean == null) {
            bean = new FriendRequestBean();
        }
        return bean;
    }

    public static void registerOnNewFriendSaveListener(OnNewFriendSaveListener onNewFriendSaveListener) {
        onNewFriendSaveListeners.add(onNewFriendSaveListener);
    }

    public static void unRegisterOnNewFriendSaveListener(OnNewFriendSaveListener onNewFriendSaveListener) {
        onNewFriendSaveListeners.remove(onNewFriendSaveListener);
    }

    public interface OnNewFriendSaveListener {
        void onNewFriendSave();
    }

}
