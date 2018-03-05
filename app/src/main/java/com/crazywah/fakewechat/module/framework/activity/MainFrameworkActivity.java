package com.crazywah.fakewechat.module.framework.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazywah.fakewechat.R;
import com.crazywah.fakewechat.common.AppConfig;
import com.crazywah.fakewechat.common.callback.OnFragmentUpdateListener;
import com.crazywah.fakewechat.common.receiver.InfoUpdateRecevier;
import com.crazywah.fakewechat.common.util.DataToFileUtil;
import com.crazywah.fakewechat.crazytools.activity.BaseActivity;
import com.crazywah.fakewechat.crazytools.fragment.BaseFragment;
import com.crazywah.fakewechat.crazytools.util.WindowSizeHelper;
import com.crazywah.fakewechat.module.fakechat.fragment.ChatListFragment;
import com.crazywah.fakewechat.module.fakecontacts.fragment.ContactsListFragment;
import com.crazywah.fakewechat.module.fakediscovery.fragment.DiscoveryListFragment;
import com.crazywah.fakewechat.module.fakemine.fragment.MineListFragment;
import com.crazywah.fakewechat.module.framework.adapter.FrameworkMainAdapter;
import com.crazywah.fakewechat.module.framework.reciver.MainExitReceiver;
import com.crazywah.fakewechat.module.framework.reciver.NewFriendRequestReceiver;
import com.crazywah.fakewechat.module.framework.reciver.OnNewFriendListener;
import com.crazywah.fakewechat.module.framework.service.MainService;
import com.crazywah.fakewechat.module.welcome.activity.BlankActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FungWah on 2018/2/28.
 */

public class MainFrameworkActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, OnNewFriendListener, MainCallBack, DataToFileUtil.OnNewFriendSaveListener {

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
    private NewFriendRequestReceiver newFriendRequestReceiver;

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

    //弹窗按钮
    private LinearLayout popGroupChatLl;
    private LinearLayout popAddLl;
    private LinearLayout popScanLl;
    private LinearLayout popPayLl;
    private LinearLayout popFeedbackLl;

    //底栏的红点
    private RelativeLayout chatRedDotRl;
    private RelativeLayout contactRedDotRl;
    private View discoveryRedDotView;
    private RelativeLayout discoveryNumRedDotRl;
    private TextView chatNumTv;
    private TextView contactNumTv;
    private TextView discoveryNumTv;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_framework_main;
    }

    @Override
    protected void initView() {

        //开启消息服务
        startService(new Intent(this, MainService.class));

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

        //底栏红点
        chatRedDotRl = findView(R.id.chat_red_dot_rl);
        contactRedDotRl = findView(R.id.contacts_red_dot_rl);
        discoveryRedDotView = findView(R.id.discovery_new_moment_reddot_view);
        discoveryNumRedDotRl = findView(R.id.discovery_red_dot_rl);
        chatNumTv = findView(R.id.chat_red_dot_num_tv);
        contactNumTv = findView(R.id.contacts_red_dot_num_tv);
        discoveryNumTv = findView(R.id.discovery_red_dot_num_tv);

        fragmentList.add(new ChatListFragment());
        fragmentList.add(new ContactsListFragment());
        fragmentList.add(new DiscoveryListFragment());
        fragmentList.add(new MineListFragment());

        initReceiver();

        for (int i = 0; i < fragmentList.size(); i++) {
            Log.d(TAG, "initView: " + i + " : " + titleTvs[i].getText().toString());
        }

        fragmentPagerAdapter = new FrameworkMainAdapter(getSupportFragmentManager(), fragmentList);

        //初始化弹窗
        initPopupWindows();

        DataToFileUtil.registerOnNewFriendSaveListener(this);
    }

    /**
     * 初始化广播接收器
     */
    private void initReceiver() {
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

        //注册信息更新广播接收器
        newFriendRequestReceiver = new NewFriendRequestReceiver();
        IntentFilter newFriendRequestIntentFilter = new IntentFilter();
        newFriendRequestIntentFilter.addAction(AppConfig.ACTION_NEW_FRIEND_REQUEST);
        this.registerReceiver(newFriendRequestReceiver, newFriendRequestIntentFilter);
    }

    /**
     * 初始化弹窗
     */
    private void initPopupWindows() {
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

    /**
     * 初始化监听器
     */
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
    protected void setView() {
        //初始化刷新红点
        onNewFriendSave();

        viewPager.setAdapter(fragmentPagerAdapter);
        //四个页面缓存在后台
        viewPager.setOffscreenPageLimit(4);
        selectPage(CHAT_PAGE);
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
            case R.id.actionbar_search:
                // TODO: 2018/3/5 测试用的Activity，要删除
                startActivity(BlankActivity.class);
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

    /**
     * 选择页面
     *
     * @param index
     */
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

    /**
     * 更新底栏消息数字
     */
    private void notifyNum(int pageIndex, int num) {
        switch (pageIndex) {
            case CHAT_PAGE:
                if (num > 0) {
                    chatRedDotRl.setVisibility(View.VISIBLE);
                    chatNumTv.setText(num + "");
                } else {
                    chatRedDotRl.setVisibility(View.GONE);
                }
                break;
            case CONTACTS_PAGE:
                if (num > 0) {
                    contactRedDotRl.setVisibility(View.VISIBLE);
                    contactNumTv.setText(num + "");
                } else {
                    contactRedDotRl.setVisibility(View.GONE);
                }
                break;
            case DISCOVERY_PAGE:
                if (num > 0) {
                    discoveryNumRedDotRl.setVisibility(View.VISIBLE);
                    discoveryNumTv.setText(num + "");
                } else if (num == -1) {
                    discoveryRedDotView.setVisibility(View.VISIBLE);
                } else {
                    discoveryRedDotView.setVisibility(View.GONE);
                    discoveryNumRedDotRl.setVisibility(View.GONE);
                }
                break;
        }
    }

    /*---------------页面滑动监听----------------*/
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
    /*---------------页面滑动监听----------------*/


    @Override
    public void refreshData(Intent intent) {
        super.refreshData(intent);
        ((OnFragmentUpdateListener) fragmentList.get(MINE_PAGE)).onFragmentUpdate(intent);
    }

    @Override
    public void onNewFriendRequest(Intent intent) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                notifyNum(CONTACTS_PAGE, DataToFileUtil.getFriendRequestFromFile().getNewFriendRequestNum());
//            }
//        });
//        ((OnNewFriendListener) fragmentList.get(CONTACTS_PAGE)).onNewFriendRequest(intent);
    }

    @Override
    public void startActivityWithAnim(Class c, int enterAnim, int outAnim) {
        startActivity(c);
        overridePendingTransition(enterAnim, outAnim);
    }

    @Override
    public void onBackPressed() {
        //不退出App，仅返回桌面
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataToFileUtil.registerOnNewFriendSaveListener(this);
        unregisterReceiver(mainExitReceiver);
        unregisterReceiver(infoUpdateRecevier);
    }

    @Override
    public void onNewFriendSave() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyNum(CONTACTS_PAGE, DataToFileUtil.getFriendRequestFromFile().getNewFriendRequestNum());
            }
        });
    }
}
