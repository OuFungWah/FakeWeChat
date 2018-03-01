package com.crazywah.fakewechat.module.fakemine.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.module.customize.NormalActionBarActivity;

/**
 * Created by FungWah on 2018/3/1.
 */

public class MineSettingActivity extends NormalActionBarActivity implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_setting;
    }

    @Override
    protected void defineActionBar(ImageView backIconImg, TextView titleTv, ImageView firstIconImg, ImageView SecondIconImg) {
        backIconImg.setOnClickListener(this);
        titleTv.setText("设置");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back_img:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishWithAnim(R.anim.move_in_from_left, R.anim.move_out_from_left);
    }

}
