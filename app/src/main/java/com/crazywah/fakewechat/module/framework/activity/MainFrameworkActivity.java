package com.crazywah.fakewechat.module.framework.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.crazytools.fragment.BaseFragment;
import com.crazywah.fakewechat.module.fakechat.fragment.ChatListFragment;
import com.crazywah.fakewechat.module.fakecontacts.fragment.ContactsListFragment;
import com.crazywah.fakewechat.module.fakediscovery.fragment.DiscoveryListFragment;
import com.crazywah.fakewechat.module.fakemine.fragment.MineListFragment;
import com.crazywah.fakewechat.module.framework.adapter.FrameworkMainAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FungWah on 2018/2/28.
 */

public class MainFrameworkActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

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

        for (int i = 0; i < fragmentList.size(); i++) {
            Log.d(TAG, "initView: " + i + " : " + titleTvs[i].getText().toString());
        }

        fragmentPagerAdapter = new FrameworkMainAdapter(getSupportFragmentManager(), fragmentList);
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
}