package com.crazywah.fakewechat.module.framework.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.common.util.DataToFileUtil;
import com.crazywah.fakewechat.crazytools.codetools.Base64Coder;
import com.crazywah.fakewechat.crazytools.util.SPUtil;
import com.crazywah.fakewechat.module.fakecontacts.activity.ContactNewFriendActivity;
import com.crazywah.fakewechat.module.fakecontacts.adapter.NewFriendItem;
import com.crazywah.fakewechat.module.framework.activity.MainFrameworkActivity;
import com.crazywah.fakewechat.module.framework.reciver.NewFriendRequestReceiver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ChatRoomMessageEvent;
import cn.jpush.im.android.api.event.CommandNotificationEvent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.GroupApprovalEvent;
import cn.jpush.im.android.api.event.GroupApprovalRefuseEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageReceiptStatusChangeEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.MyInfoUpdatedEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/2.
 */

public class MainService extends Service {

    private static final String TAG = "MainService";

    private static final String NOTIFICATION_CONTACT = "com.crazyWah.contact";
    private static final String NOTIFICATION_MESSAGE = "com.crazyWah.message";
    private static final String NOTIFICATION_LOGINSTATE = "com.crazyWah.loginState";

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 注册极光事件监听成功");
        JMessageClient.registerEventReceiver(this);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //高于Android 8的，创建消息频道
            NotificationChannel contactChannel = new NotificationChannel(NOTIFICATION_CONTACT, "通讯录相关通知", NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel messageChannel = new NotificationChannel(NOTIFICATION_MESSAGE, "消息相关通知", NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel loginStateChannel = new NotificationChannel(NOTIFICATION_LOGINSTATE, "登录状态相关通知", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(contactChannel);
            mNotificationManager.createNotificationChannel(messageChannel);
            mNotificationManager.createNotificationChannel(loginStateChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 绑定服务成功");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onStartCommand: 绑定服务成功");

        return new MainServieBinder();
    }

    public class MainServieBinder extends Binder {
        public MainService getService() {
            return MainService.this;
        }
    }

    /**
     * 监听消息事件
     *
     * @param event
     */
    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                    case group_info_updated://since 2.2.1
                        //群信息变更事件
                        break;
                }
                break;
        }
    }


    /**
     * 监听离线消息
     *
     * @param event
     */
    public void onEvent(OfflineMessageEvent event) {

    }

    /**
     * 监听回话刷新事件
     *
     * @param event
     */
    public void onEvent(ConversationRefreshEvent event) {

    }

    /**
     * 监听当前用户信息刷新事件
     *
     * @param event
     */
    public void onEvent(MyInfoUpdatedEvent event) {

    }

    /**
     * 通知栏点击事件
     *
     * @param event
     */
    public void onEvent(NotificationClickEvent event) {

    }

    /**
     * 监听用户下线事件
     *
     * @param event
     */
    public void onEvent(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        switch (reason) {
            case user_password_change:
                //用户密码在服务器端被修改
                break;
            case user_logout:
                //用户换设备登录
                break;
            case user_deleted:
                //用户被删除
                break;
        }
    }


    /**
     * 监听消息被撤回事件
     *
     * @param event
     */
    public void onEvent(MessageRetractEvent event) {

    }

    /**
     * 消息未回执人数变更事件
     *
     * @param event
     */
    public void onEvent(MessageReceiptStatusChangeEvent event) {

    }

    /**
     * 命令透传事件
     *
     * @param event
     */
    public void onEvent(CommandNotificationEvent event) {

    }

    /**
     * 群成员审批事件
     *
     * @param event
     */
    public void onEvent(GroupApprovalEvent event) {

    }

    /**
     * 群成员审批拒绝事
     *
     * @param event
     */
    public void onEvent(GroupApprovalRefuseEvent event) {

    }

    /**
     * 聊天室消息事件
     *
     * @param event
     */
    public void onEvent(ChatRoomMessageEvent event) {

    }

    /**
     * 通讯录事件
     *
     * @param event
     */
    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received:
                //收到好友邀请
                Log.d(TAG, "onEvent: 收到好友邀请");
                Intent intent = new Intent(this, ContactNewFriendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fromUsername", fromUsername);
                bundle.putString("reason", reason);
                bundle.putString("appkey", appkey);
                intent.putExtras(bundle);
                showSimpleNotification("Fake微信", fromUsername + "：" + reason, intent, false, true, NOTIFICATION_CONTACT);
                Intent sendNewFriendRequestIntent = new Intent();
                sendNewFriendRequestIntent.setAction(AppConfig.ACTION_NEW_FRIEND_REQUEST);
                sendBroadcast(sendNewFriendRequestIntent);
                // TODO: 2018/3/5 添加使用Serializable存储申请记录
                DataToFileUtil.saveNewFriendRequest(fromUsername, reason);
                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        JMessageClient.unRegisterEventReceiver(this);
        return super.onUnbind(intent);
    }

    private void notifyNewFriend() {

    }


    /**
     * 展示通知
     *
     * @param titleStr
     * @param content
     * @param intent
     * @param isAutoCancel
     */
    private void showSimpleNotification(String titleStr, String content, Intent intent, boolean isOngoing, boolean isAutoCancel, String channelId) {
        //NotificationCompat总会调用setSound()方法，所以当没有设置setSound时，通知将无法弹出
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.icon)
                .setContentTitle(titleStr)
                .setContentText(content)
                .setOngoing(false)
                .setAutoCancel(isAutoCancel);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //高于Android 8的，设置消息频道
            builder.setChannelId(channelId);
        }
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainFrameworkActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * 序列化对象为String字符串
     *
     * @param o Object
     * @return String
     * @throws Exception
     */
    public static String writeObject(Object o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.flush();
        oos.close();
        bos.close();
        return Base64Coder.encode(bos.toByteArray().toString());
    }


    /**
     * 反序列化字符串为对象
     *
     * @param object String
     * @return
     * @throws Exception
     */
    public static Object readObject(String object) throws Exception {

        ByteArrayInputStream bis = new ByteArrayInputStream(android.util.Base64.decode(object, android.util.Base64.DEFAULT));
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o = ois.readObject();
        bis.close();
        ois.close();
        return o;
    }

}
