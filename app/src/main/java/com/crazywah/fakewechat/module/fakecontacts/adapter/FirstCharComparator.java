package com.crazywah.fakewechat.module.fakecontacts.adapter;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Comparator;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by FungWah on 2018/3/4.
 */

public class FirstCharComparator implements Comparator<ContactListItem> {

    private int position = 0;

    @Override
    public int compare(ContactListItem o1, ContactListItem o2) {
        UserInfo userInfo1 = o1.getUserInfo();
        UserInfo userInfo2 = o2.getUserInfo();
        if (userInfo1 != null && userInfo2 != null) {
            char first1 = userInfo1.getNickname().charAt(position);
            char first2 = userInfo2.getNickname().charAt(position);

            //如果是中文，先获取它的拼音首字母
            if (Pinyin.isChinese(first1)) {
                first1 = Pinyin.toPinyin(first1).charAt(0);
                first2 = Pinyin.toPinyin(first2).charAt(0);
            }
            //开始排序
            if (first1 < first2) {
                return -1;
            } else if (first1 == first2) {
                return 0;
            } else {
                return 1;
            }
        }
        return 0;
    }
}
