package orvnge.wwnje.com.fucknews.view.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.orvnge.xutils.MyFragment;
import com.orvnge.xutils.TextProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.LogUtil;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.TestActivity;
import orvnge.wwnje.com.fucknews.data.Finder_List_Data;
import orvnge.wwnje.com.fucknews.data.Finder_News_Data;
import orvnge.wwnje.com.fucknews.utils.BlankAPI;
import orvnge.wwnje.com.fucknews.utils.CODE;
import orvnge.wwnje.com.fucknews.utils.DatabaseHelper;
import orvnge.wwnje.com.fucknews.view.Activity.ShareNewsActivity;
import orvnge.wwnje.com.fucknews.view.Activity.HomeActivity;
import orvnge.wwnje.com.fucknews.view.Activity.TwentyActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends BaseFragment  implements TextProvider {


//    public List<String> Frags;
//    public List<String> FragsURL;
    private static final String TAG = "BlankFragment";

    public static ArrayList<String> Titles = new ArrayList<>();

    public static ArrayList<Fragment> Fragments = new ArrayList<>();


    private Toolbar mToolbar;
    private static ViewPager viewPager;
    private TabLayout tabLayout;
    private static MyPagerAdapter mAdapter;

    @Bind(R.id.share_fab)
    FloatingActionButton btn_share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShareNewsActivity.class));
            }
        });

        //设置标题居中
        //mToolbar.setTitle("");
        //TextView toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        //toolbarTitle.setText("首页");
        ((HomeActivity) getActivity()).initDrawer(mToolbar);

        initTabLayout(view);
        inflateMenu();
        initSearchView();
    }

    /**
     * 搜索功能
     */
    private void initSearchView() {
        final SearchView searchView = (SearchView) mToolbar.getMenu()
                .findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("输入tips试试看…");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showToast("query=" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                switch(s) {
                    case CODE.TWENTY:
                        startActivity(new Intent(getActivity(), TwentyActivity.class));
                        searchView.clearFocus();
                        mToolbar.collapseActionView();
                        searchView.setQuery("", false);
                        break;

                    case CODE.TIPS:
                        new AlertDialog.Builder(getActivity()).setTitle("标题")
                                .setMessage(R.string.copyright_content)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        searchView.clearFocus();
                        searchView.setQuery("", false);
                        break;
                }
                LogUtil.d("onQueryTextChange=" + s);
                return false;
            }
        });
    }
    /**
     * 设置菜单
     */
    private void inflateMenu() {
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                   case R.id.action_copyright:
                        showToast("测试使用");
                       startActivity(new Intent(getActivity(), TestActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public String getTextForPosition(int position) {
        return Finder_List_Data.NEWS_TYPE_NAME.get(position);
    }

    @Override
    public int getCount() {
        return Finder_List_Data.NEWS_TYPE_NAME.size();
    }

    @Override
    public Fragment getTrag(int position) {
//        return Finder_List_Data.Fragments.get(position);
        return Fragments.get(position);
    }

    /**
     * ViewPager设置
     */
    class MyPagerAdapter extends FragmentPagerAdapter {
        private TextProvider mProvider;
        private long baseId = 0;

        public MyPagerAdapter(FragmentManager fragmentManager, TextProvider provider) {
            super(fragmentManager);
            this.mProvider = provider;
        }


        /**
         * 获取给定位置对应的Fragment。
         *
         * @param position 给定的位置
         * @return 对应的Fragment
         */
        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "对应的Fragment: ------" + "position:" + position + Fragments.get(position).getArguments().getString("type"));
            Log.d(TAG, "Fragment数量" + mProvider.getCount() + "，"+Fragments.size());

            return mProvider.getTrag(position);
            //return ContentFragment.newInstance(mProvider.getTextForPosition(position));
            //return Finder_List_Data.Fragments.get(position);
        }

        @Override
        public int getCount() {
            return mProvider.getCount();
            //return Finder_List_Data.Fragments.size();
        }

        /**
         * 获取给定位置的项Id，用于生成Fragment名称
         *
         * @param position 给定的位置
         * @return 项Id
         */

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            Log.d(TAG, "获取给定位置的项Id: --------" + "position" + position);
            Log.d(TAG, "Fragment数量" + mProvider.getCount() + "，" + Fragments.size());

            for(int i = 0; i< Fragments.size(); i++){
                Log.d(TAG, "Fragment里面的东西:" + Fragments.get(i).getArguments().getInt("id")
                        + Fragments.get(i).getArguments().getString("type") );
            }
            //return baseId + position;
            return Fragments.get(position).getArguments().getInt("id");
        }


        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
            //baseId += n;
            Log.d(TAG, "修改位置：notifyChangeInPosition: " + "getCount" + getCount());
        }

        //标题
        @Override
        public CharSequence getPageTitle(int position) {
            return Finder_List_Data.NEWS_TYPE_NAME.get(position);//标题
        }
    }

    /**
     *
     * @param view
     */
    private void initTabLayout(View view) {

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        setupViewPager();//设置adapter

        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());

        // 设置ViewPager的数据等
        tabLayout.setupWithViewPager(viewPager);
        //适合很多tab
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tab均分,适合少的tab
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tab均分,适合少的tab,TabLayout.GRAVITY_CENTER
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    /**
     * 设置首页展示标签的页面
     */

    public void setupViewPager() {

        Fragments = new ArrayList<>();

        Finder_List_Data.NEWS_TYPE_NAME = new ArrayList<>();

        //Finder_List_Data.Fragments = new ArrayList<>();

        Bundle data;
        Finder_List_Data.NEWS_TYPE_NAME.add("Blank");

        for(int i = 0; i < Finder_List_Data.NEWS_TYPE_NAME.size(); i++){
            ContentFragment newfragment = new ContentFragment();
            data = new Bundle();
            data.putInt("id", i);
            data.putString("type", Finder_List_Data.NEWS_TYPE_NAME.get(i));

            newfragment.setArguments(data);

            //Finder_List_Data.Fragments.add(newfragment);
            Fragments.add(newfragment);
        }

        mAdapter = new MyPagerAdapter(getChildFragmentManager(),this);
        viewPager.setAdapter(mAdapter);
    }

    /**
     * 增加
     */
    public static  void AddNewItem(String type_name) {

        ContentFragment newfragment = new ContentFragment();

        Bundle data;
        data = new Bundle();

        data.putInt("id", Finder_List_Data.NEWS_TYPE_NAME.size());
        data.putString("type", type_name);

        newfragment.setArguments(data);

        Finder_List_Data.NEWS_TYPE_NAME.add(type_name);
        Fragments.add(newfragment);

        //mAdapter.notifyChangeInPosition(1);

        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);
    }

    /**
     * 取消
     */
    public static void DeleteItem(int position) {

        Finder_List_Data.NEWS_TYPE_NAME.remove(position);
        //Titles.remove(position);
//        Finder_List_Data.Fragments.remove(position);
        Fragments.remove(position);

        //mAdapter.notifyChangeInPosition(1);
        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);//更新
    }

    /**
     * 删去当前页面
     */
    public static void removeCurrentItem() {
        int position = viewPager.getCurrentItem();//当前view

        Finder_List_Data.NEWS_TYPE_NAME.remove(position);

        Fragments.remove(Fragments.get(position));

        Log.d(TAG, "删除的的位置: " + position);

        mAdapter.notifyChangeInPosition(1);

        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);//更新
    }
}
