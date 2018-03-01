package com.crazywah.fakewechat.module.fakemine.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.fragment.BaseFragment;
import com.crazywah.fakewechat.module.fakemine.activity.MineSettingActivity;

/**
 * Created by FungWah on 2018/2/28.
 */

public class MineListFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout userInfoLl;
    private LinearLayout walletLl;
    private LinearLayout favorLl;
    private LinearLayout albumLl;
    private LinearLayout cardBagLl;
    private LinearLayout expressionLl;
    private LinearLayout settingLl;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine_list;
    }

    @Override
    protected void initView(View parent) {
        userInfoLl = findView(R.id.mine_userinfo_ll);
        walletLl = findView(R.id.mine_wallet_ll);
        favorLl = findView(R.id.mine_favor_ll);
        albumLl = findView(R.id.mine_album_ll);
        cardBagLl = findView(R.id.mine_card_bag_ll);
        expressionLl = findView(R.id.mine_expression_ll);
        settingLl = findView(R.id.mine_setting_ll);
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initListener() {
        userInfoLl.setOnClickListener(this);
        walletLl.setOnClickListener(this);
        favorLl.setOnClickListener(this);
        albumLl.setOnClickListener(this);
        cardBagLl.setOnClickListener(this);
        expressionLl.setOnClickListener(this);
        settingLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_userinfo_ll:
                break;
            case R.id.mine_wallet_ll:
                break;
            case R.id.mine_favor_ll:
                break;
            case R.id.mine_album_ll:
                break;
            case R.id.mine_card_bag_ll:
                break;
            case R.id.mine_expression_ll:
                break;
            case R.id.mine_setting_ll:
                startActivity(MineSettingActivity.class);
                getActivity().overridePendingTransition(R.anim.move_in_from_right,R.anim.move_out_from_right);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
