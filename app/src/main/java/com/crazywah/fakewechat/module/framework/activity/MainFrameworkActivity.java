package com.crazywah.fakewechat.module.framework.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.common.callback.OnFragmentUpdateListener;
import com.crazywah.fakewechat.common.receiver.InfoUpdateRecevier;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.crazytools.fragment.BaseFragment;
import com.crazywah.fakewechat.crazytools.util.WindowSizeHelper;
import com.crazywah.fakewechat.module.fakechat.fragment.ChatListFragment;
import com.crazywah.fakewechat.module.fakecontacts.fragment.ContactsListFragment;
import com.crazywah.fakewechat.module.fakediscovery.fragment.DiscoveryListFragment;
import com.crazywah.fakewechat.module.fakemine.fragment.MineListFragment;
import com.crazywah.fakewechat.module.framework.adapter.FrameworkMainAdapter;
import com.crazywah.fakewechat.module.framework.reciver.MainExitReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FungWah on 2018/2/28.
 */

public class MainFrameworkActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, MainCallBack {

    private static final String TAG = "MainFrameworkActivity";

    private static final int CHAT_PAGE = 0;
    private static final int CONTACTS_PAGE = 1;
    private static final int DISCOVERY_PAGE = 2;
    private static final int MINE_PAGE = 3;

    private static final int UPDATE_PAGE = 0;

    private static final int SELECTED_ICON[] = {R.drawable.ic_chat_selected, R.drawable.ic_contacts_selected, R.drawable.ic_discovery_selected, R.drawable.ic_mine_selected};
    private static final int UN_SELECTED_ICON[] = {R.drawable.ic_chat, R.drawable.ic_contacts, R.drawable.ic_discovery, R.drawable.ic_mine};

    private ImageView actionBarSearchImg;
    private ImageView actionBarAddImg;

    private MainExitReceiver mainExitReceiver;
    private InfoUpdateRecevier infoUpdateRecevier;

    private ViewPager viewPager;

    private LinearLayout[] cellLls = new LinearLayout[4];
    private TextView[] titleTvs = new TextView[4];
    private ImageView[] iconImgs = new ImageView[4];

    private List<BaseFragment> fragmentList = new ArrayList<>();

    private FragmentPagerAdapter fragmentPagerAdapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PAGE:

                    break;
            }
            return false;
        }
    });

    private PopupWindow addPw;
    private View addPopView;

    private LinearLayout popGroupChatLl;
    private LinearLayout popAddLl;
    private LinearLayout popScanLl;
    private LinearLayout popPayLl;
    private LinearLayout popFeedbackLl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_framework_main;
    }

    @Override
    protected void initView() {

        actionBarSearchImg = findView(R.id.actionbar_search);
        actionBarAddImg = findView(R.id.actionbar_add);
        viewPager = findView(R.id.main_viewpager);

        cellLls[CHAT_PAGE] = findView(R.id.chat_ll);
        cellLls[CONTACTS_PAGE] = findView(R.id.contacts_ll);
        cellLls[DISCOVERY_PAGE] = findView(R.id.discovery_ll);
        cellLls[MINE_PAGE] = findView(R.id.mine_ll);

        titleTvs[CHAT_PAGE] = findView(R.id.chat_tv);
        titleTvs[CONTACTS_PAGE] = findView(R.id.contacts_tv);
        titleTvs[DISCOVERY_PAGE] = findView(R.id.discovery_tv);
        titleTvs[MINE_PAGE] = findView(R.id.mine_tv);

        iconImgs[CHAT_PAGE] = findView(R.id.chat_img);
        iconImgs[CONTACTS_PAGE] = findView(R.id.contacts_img);
        iconImgs[DISCOVERY_PAGE] = findView(R.id.discovery_img);
        iconImgs[MINE_PAGE] = findView(R.id.mine_img);

        fragmentList.add(new ChatListFragment());
        fragmentList.add(new ContactsListFragment());
        fragmentList.add(new DiscoveryListFragment());
        fragmentList.add(new MineListFragment());

        //注册退出App广播接收器
        mainExitReceiver = new MainExitReceiver();
        IntentFilter mianExitIntentFilter = new IntentFilter();
        mianExitIntentFilter.addAction(AppConfig.ACTION_EXIT_APP);
        this.registerReceiver(mainExitReceiver, mianExitIntentFilter);

        //注册信息更新广播接收器
        infoUpdateRecevier = new InfoUpdateRecevier();
        IntentFilter infoUpdateIntentFilter = new IntentFilter();
        infoUpdateIntentFilter.addAction(AppConfig.ACTION_UPDATE_INFO);
        this.registerReceiver(infoUpdateRecevier, infoUpdateIntentFilter);

        for (int i = 0; i < fragmentList.size(); i++) {
            Log.d(TAG, "initView: " + i + " : " + titleTvs[i].getText().toString());
        }

        fragmentPagerAdapter = new FrameworkMainAdapter(getSupportFragmentManager(), fragmentList);

        //初始化弹窗
        initPopuWindows();

    }

    @Override
    protected void setView() {
        viewPager.setAdapter(fragmentPagerAdapter);
        selectPage(CHAT_PAGE);

    }

    @Override
    protected void initListener() {
        viewPager.setOnPageChangeListener(this);
        for (int i = 0; i < fragmentList.size(); i++) {
            cellLls[i].setOnClickListener(this);
        }
        actionBarSearchImg.setOnClickListener(this);
        actionBarAddImg.setOnClickListener(this);
        //注册弹窗点击事件监听
        popGroupChatLl.setOnClickListener(this);
        popAddLl.setOnClickListener(this);
        popScanLl.setOnClickListener(this);
        popPayLl.setOnClickListener(this);
        popFeedbackLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_ll:
                selectPage(CHAT_PAGE);
                break;
            case R.id.contacts_ll:
                selectPage(CONTACTS_PAGE);
                break;
            case R.id.discovery_ll:
                selectPage(DISCOVERY_PAGE);
                break;
            case R.id.mine_ll:
                selectPage(MINE_PAGE);
                break;
            case R.id.actionbar_add:
                addPw.showAsDropDown(actionBarAddImg, (int) (-360 * WindowSizeHelper.getHelper().getProporationX()), 0);
                break;
            case R.id.actionbar_pop_group_chat_ll:
                break;
            case R.id.actionbar_pop_add_ll:
                startActivityWithAnim(AddFriendActivity.class, R.anim.move_in_from_right, R.anim.move_out_from_right);
                addPw.dismiss();
                break;
            case R.id.actionbar_pop_scan_ll:
                break;
            case R.id.actionbar_pop_pay_ll:
                break;
            case R.id.actionbar_pop_feedback_ll:
                break;
        }
    }


    private void selectPage(int index) {
        viewPager.setCurrentItem(index, true);
        iconImgs[index].setImageResource(SELECTED_ICON[index]);
        titleTvs[index].setTextColor(getResources().getColor(R.color.colorWeChatGreen));
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i != index) {
                iconImgs[i].setImageResource(UN_SELECTED_ICON[i]);
                titleTvs[i].setTextColor(getResources().getColor(R.color.colorBottomTitleText));
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectPage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void startActivityWithAnim(Class c, int enterAnim, int outAnim) {
        startActivity(c);
        overridePendingTransition(enterAnim, outAnim);
    }

    @Override
    public void refreshData(Intent intent) {
        super.refreshData(intent);
        ((OnFragmentUpdateListener) fragmentList.get(MINE_PAGE)).onFragmentUpdate(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mainExitReceiver);
        unregisterReceiver(infoUpdateRecevier);
    }

    private void initPopuWindows() {
        //初始化弹窗的视图
        addPopView = getLayoutInflater().inflate(R.layout.popupwindow_actionbar_add, null);

        //新建弹窗并绑定视图
        addPw = new PopupWindow(addPopView, (int) (525 * WindowSizeHelper.getHelper().getProporationX()), LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popGroupChatLl = addPopView.findViewById(R.id.actionbar_pop_group_chat_ll);
        popAddLl = addPopView.findViewById(R.id.actionbar_pop_add_ll);
        popScanLl = addPopView.findViewById(R.id.actionbar_pop_scan_ll);
        popPayLl = addPopView.findViewById(R.id.actionbar_pop_pay_ll);
        popFeedbackLl = addPopView.findViewById(R.id.actionbar_pop_feedback_ll);

    }

}
