package com.crazywah.fakewechat.module.fakemine.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.crazytools.util.BitmapTools;
import com.crazywah.fakewechat.crazytools.util.PermissionUtil;
import com.crazywah.fakewechat.crazytools.util.ToastUtil;
import com.crazywah.fakewechat.crazytools.util.WindowSizeHelper;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;
import com.crazywah.fakewechat.common.receiver.InfoUpdateRecevier;
import com.facebook.common.time.SystemClock;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by FungWah on 2018/3/2.
 */

public class MineUserInfoActivity extends NormalActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MineUserInfoActivity";

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAPTURE_AND_CROP = 2;
    private static final int SAVE_BITMAP_AFTER_GRANTED = 3;
    private static final int AFTER_GET_DATA = 4;
    private static final int OVER_TIME = 5;
    private static final int TOKEN_MISTAKE = 6;
    private static final int SHOW_TOAST = 7;

    private static final int LOAD_LOCAL_URI_COMPLETE = 0;

    private UserInfo userInfo;

    private RelativeLayout avatarRl;
    private RelativeLayout nicknameRl;
    private RelativeLayout usernameRl;
    private RelativeLayout genderRl;
    private RelativeLayout addressRl;

    private TextView nicknameTv;
    private TextView usernameTv;
    private TextView genderTv;
    private TextView regionTv;

    private SimpleDraweeView avatarSdv;

    private String nicknameStr;
    private String usernameStr;
    private String genderStr;
    private String regionStr;

    private Dialog genderDialog;
    private View genderDialogView;

    private RelativeLayout maleRl;
    private RelativeLayout femaleRl;

    private Button maleBtn;
    private Button femaleBtn;

    private Dialog avatarDialog;
    private View avatarDialogView;

    private RelativeLayout takePhotoRl;
    private RelativeLayout selectPhotoRl;

    private File destination;
    private Uri imageUri;
    private Bitmap bitmap;

    private boolean isUploadingAvatar = false;

    private BroadcastReceiver updateRecreiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshData(intent);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_userinfo;
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg, Button actionbarRightBtn) {
        backIconImg.setOnClickListener(this);
        titleTv.setText("个人信息");
    }

    @Override
    protected void initView() {

        userInfo = JMessageClient.getMyInfo();

        avatarRl = findView(R.id.userinfo_avatar_rl);
        nicknameRl = findView(R.id.userinfo_nickname_rl);
        usernameRl = findView(R.id.userinfo_username_rl);
        genderRl = findView(R.id.userinfo_gender_rl);
        addressRl = findView(R.id.userinfo_region_rl);

        avatarSdv = findView(R.id.userinfo_avatar_sdv);

        nicknameTv = findView(R.id.userinfo_nickname_tv);
        usernameTv = findView(R.id.userinfo_username_tv);
        genderTv = findView(R.id.userinfo_gender_tv);
        regionTv = findView(R.id.userinfo_region_tv);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConfig.ACTION_UPDATE_INFO);
        registerReceiver(updateRecreiver, intentFilter);

        genderDialog = new Dialog(this, R.style.CloseDialog);
        genderDialogView = getLayoutInflater().inflate(R.layout.dialog_gender, null);

        maleRl = genderDialogView.findViewById(R.id.dialog_male_rl);
        femaleRl = genderDialogView.findViewById(R.id.dialog_female_rl);

        maleBtn = genderDialogView.findViewById(R.id.dialog_male_btn);
        femaleBtn = genderDialogView.findViewById(R.id.dialog_female_btn);

        //初始化选择头像的对话框
        avatarDialog = new Dialog(this, R.style.CloseDialog);
        avatarDialogView = getLayoutInflater().inflate(R.layout.dialog_avatar, null);

        takePhotoRl = avatarDialogView.findViewById(R.id.avatar_dialog_take_photo_rl);
        selectPhotoRl = avatarDialogView.findViewById(R.id.avatar_dialog_select_photo_rl);
    }

    @Override
    protected void setView() {

        try {
            avatarSdv.setImageURI(Uri.fromFile(userInfo.getAvatarFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        nicknameStr = (!userInfo.getNickname().equals("") ? userInfo.getNickname() : "未设置昵称");
        usernameStr = (!userInfo.getUserName().equals("") ? userInfo.getUserName() : "未设置用户名");
        genderStr = userInfo.getGender() == UserInfo.Gender.male ? "男" : userInfo.getGender() == UserInfo.Gender.female ? "女" : "未知";
        regionStr = (!userInfo.getRegion().equals("") ? userInfo.getRegion() : "未设置地区");
        nicknameTv.setText(nicknameStr);
        usernameTv.setText(usernameStr);
        genderTv.setText(genderStr);
        regionTv.setText(regionStr);

        genderDialog.setContentView(genderDialogView, new ViewGroup.LayoutParams((int) (870 * WindowSizeHelper.getHelper().getProporationX()), (int) (510 * WindowSizeHelper.getHelper().getProporationY())));
        genderDialog.getWindow().setGravity(Gravity.CENTER);

        maleBtn.setEnabled(userInfo.getGender() == UserInfo.Gender.male);
        femaleBtn.setEnabled(userInfo.getGender() == UserInfo.Gender.female);

        avatarDialog.setContentView(avatarDialogView, new ViewGroup.LayoutParams((int) (1080 * WindowSizeHelper.getHelper().getProporationX()), (int) (300 * WindowSizeHelper.getHelper().getProporationY())));
        avatarDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        avatarDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void initListener() {
        avatarRl.setOnClickListener(this);
        nicknameRl.setOnClickListener(this);
        usernameRl.setOnClickListener(this);
        genderRl.setOnClickListener(this);
        addressRl.setOnClickListener(this);

        maleRl.setOnClickListener(this);
        femaleRl.setOnClickListener(this);

        takePhotoRl.setOnClickListener(this);
        selectPhotoRl.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (!isUploadingAvatar) {
            if (genderDialog.isShowing()) {
                genderDialog.dismiss();
            } else {
                finishWithAnim(R.anim.move_in_from_left, R.anim.move_out_from_left);
            }
        } else {
            ToastUtil.showShort("头像上传中...");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
            case R.id.userinfo_avatar_rl:
                if (!isUploadingAvatar) {
                    avatarDialog.show();
                } else {
                    ToastUtil.showShort("头像上传中...");
                }
                break;
            case R.id.userinfo_nickname_rl:
                startActivityWithAnim(ModifyNickNameActivity.class, R.anim.move_in_from_right, R.anim.move_out_from_right);
                break;
            case R.id.userinfo_username_rl:
                break;
            case R.id.userinfo_gender_rl:
                genderDialog.show();

                break;
            case R.id.userinfo_region_rl:
                break;
            case R.id.dialog_male_rl:
                updateGender(UserInfo.Gender.male);
                break;
            case R.id.dialog_female_rl:
                updateGender(UserInfo.Gender.female);
                break;
            case R.id.avatar_dialog_take_photo_rl:
                //判断是否有权限操作
                if (PermissionUtil.checkPermission(this, Manifest.permission.CAMERA)) {
                    if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        isUploadingAvatar = true;
                        takePhoto();
                    } else {
                        ToastUtil.showShort("请先授予存储权限");
                        //获取权限
                        PermissionUtil.getPermission(this, PermissionUtil.STORAGE_PERMISSION);
                    }
                } else {
                    ToastUtil.showShort("请先授予照相权限");
                    //获取权限
                    PermissionUtil.getPermission(this, PermissionUtil.CAMERA_PERMISSION);
                }
                break;
            case R.id.avatar_dialog_select_photo_rl:
                //判断是否有权限操作
                if (PermissionUtil.checkPermission(this, Manifest.permission.CAMERA)) {
                    if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        isUploadingAvatar = true;
                        gotoPickImage();
                    } else {
                        ToastUtil.showShort("请先授予存储权限");
                        //获取权限
                        PermissionUtil.getPermission(this, PermissionUtil.STORAGE_PERMISSION);
                    }
                } else {
                    ToastUtil.showShort("请先授予照相权限");
                    //获取权限
                    PermissionUtil.getPermission(this, PermissionUtil.CAMERA_PERMISSION);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 更新性别信息
     *
     * @param gender
     */
    void updateGender(UserInfo.Gender gender) {
        userInfo.setGender(gender);
        maleBtn.setEnabled(gender == UserInfo.Gender.male);
        femaleBtn.setEnabled(gender == UserInfo.Gender.female);
        JMessageClient.updateMyInfo(UserInfo.Field.gender, userInfo, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (s.equals("Success")) {
                    Intent intent = new Intent();
                    intent.setAction(AppConfig.ACTION_UPDATE_INFO);
                    sendBroadcast(intent);
                } else {
                    ToastUtil.showShort("请检查网络连接");
                }
                genderDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void refreshData(Intent intent) {
        super.refreshData(intent);
        //重新获取新的用户信息
        userInfo = JMessageClient.getMyInfo();
        onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateRecreiver);
    }

    private void gotoPickImage() {
        avatarDialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    protected void takePhoto() {
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        avatarDialog.dismiss();
        destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "fakeWechatUser:" + userInfo.getUserName() + ".png");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, REQUEST_CAPTURE_AND_CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "int requestCode:" + requestCode + "\n int resultCode:" + resultCode + "\n Intent data:" + data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                bitmap = BitmapTools.getBitmapFormUri(this, imageUri);
                File tempFile = BitmapTools.saveToFile(bitmap, getFilesDir(), userInfo.getUserName());
                JMessageClient.updateUserAvatar(tempFile, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (s.equals("Success")) {
                            ToastUtil.showShort("上传头像成功");
                            Intent updateInfoIntent = new Intent();
                            updateInfoIntent.setAction(AppConfig.ACTION_UPDATE_INFO);
                            sendBroadcast(updateInfoIntent);
                        }
                        isUploadingAvatar = false;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
//            handler.sendEmptyMessage(LOAD_LOCAL_URI_COMPLETE);
        } else if (requestCode == REQUEST_CAPTURE_AND_CROP && resultCode == RESULT_OK) {
            //处理相机
            Log.d(TAG, "接收到相机处理请求");
            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                /**
                 * 用于预防某些机型会自动旋转图片，所以需要获取图片的旋转信息以把图片重新旋转回来
                 */
                if (BitmapTools.getBitmapDegree(destination.getPath()) != 0) {
                    bitmap = BitmapTools.rotateBitmapByDegree(bitmap, BitmapTools.getBitmapDegree(destination.getPath()));
                }
//                handler.sendEmptyMessage(LOAD_LOCAL_URI_COMPLETE);
                JMessageClient.updateUserAvatar(destination, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (s.equals("Success")) {
                            ToastUtil.showShort("上传头像成功");
                            Intent updateInfoIntent = new Intent();
                            updateInfoIntent.setAction(AppConfig.ACTION_UPDATE_INFO);
                            sendBroadcast(updateInfoIntent);
                        }
                        isUploadingAvatar = false;
                    }
                });
                Log.d(TAG, "拍照处理成功");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "拍照异常");
            } finally {

            }
        }
    }

    private void uploadAvatar(){

    }

}
