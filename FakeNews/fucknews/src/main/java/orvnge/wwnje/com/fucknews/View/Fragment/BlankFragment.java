package orvnge.wwnje.com.fucknews.view.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.LogUtil;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.TestActivity;
import orvnge.wwnje.com.fucknews.utils.API;
import orvnge.wwnje.com.fucknews.utils.CODE;
import orvnge.wwnje.com.fucknews.view.Activity.AddNewsActivity;
import orvnge.wwnje.com.fucknews.view.Activity.HomeActivity;
import orvnge.wwnje.com.fucknews.view.Activity.TwentyActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends BaseFragment {

    private static final String TAG = "BlankFragment";

    private Toolbar mToolbar;
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
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddNewsActivity.class));
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


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
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
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        List<String> Frags = new ArrayList<>();
        List<String> FragsURL = new ArrayList<>();

        //存放订阅后的标签页
        Frags.add("Blank");
        Frags.add("World");
        Frags.add("Life");
        Frags.add("Game");
        Frags.add("Design");
        Frags.add("Book");
        Frags.add("Movie");
        Frags.add("Arts");

        FragsURL.add(API.GET_NEWS_URL);
        FragsURL.add(API.GET_WORLD_URL);
        FragsURL.add(API.GET_LIFE_URL);
        FragsURL.add(API.GET_GAME_URL);
        FragsURL.add(API.GET_DESIGN_URL);
        FragsURL.add(API.GET_BOOK_URL);
        FragsURL.add(API.GET_MOVIE_URL);
        FragsURL.add(API.GET_ARTS_URL);

        Fragment newfragment;
        Bundle data;

        for(int i = 0; i < 8; i++){
            newfragment = new ContentFragment();
            data = new Bundle();
            data.putInt("id", i);
            data.putString("title", Frags.get(i));
            data.putString("url", FragsURL.get(i));
            Log.d(TAG, "setupViewPager: " + Frags.get(i) + FragsURL.get(i));
            newfragment.setArguments(data);
            adapter.addFrag(newfragment, Frags.get(i));
        }

        viewPager.setAdapter(adapter);

    }

}
