package orvnge.wwnje.com.fucknews.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wwnje on 2016/5/18.
 * 主界面数据
 */
public class HomeTagsNameAdapter extends FragmentStatePagerAdapter {

    List<Fragment> mFragments;
    //
    String[] titles = {
            "Blank",//推荐Apps 游戏
            //"For you",//你的订阅内容
            //"Music",//音乐
            "World",//国内外新闻
            "Life",//生活和搞笑
            //"Coding",//编程
            "Game",//游戏
            "Movie",//电影
            //"Technology",//科技
            "Design",//设计
            "Arts",//艺术
            "Book",//书本推荐介绍
            };

    public HomeTagsNameAdapter(FragmentManager fm, List<Fragment>fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return mTitles.get(position);
        return titles[position];
    }
}
