package com.crazywah.fakewechat.module.customize;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;

/**
 * Created by FungWah on 2018/3/1.
 */

public abstract class NormalActionBarActivity extends BaseActivity {

    protected ImageView actionbarBackImg;
    protected TextView actionbarTitleTv;
    protected ImageView actionbarFirstIconImg;
    protected ImageView actionbarSecondIconImg;
    protected Button actionRightBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionbarBackImg = findView(R.id.actionbar_back_img);
        actionbarTitleTv = findView(R.id.actionbar_title);
        actionbarFirstIconImg = findView(R.id.actionbar_first_icon_img);
        actionbarSecondIconImg = findView(R.id.actionbar_second_icon_img);
        actionRightBtn = findView(R.id.actionbar_right_btn);

        defineActionBar(actionbarBackImg, actionbarTitleTv, actionbarFirstIconImg, actionbarSecondIconImg,actionRightBtn);
    }

    /**
     * 待实现的控制ActionBar上各个控件的方法
     *
     * @param backIconImg   已绑定的返回按键的对象
     * @param titleTv       已绑定的标题的对象
     * @param firstIconImg  已绑定的右方第一个按钮的对象
     * @param SecondIconImg 已绑定的右方第二个按钮的对象
     */
    protected abstract void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg,Button actionRightBtn);

}
