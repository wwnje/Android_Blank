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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        Fragment newfragment = new ContentFragment();
        Bundle data = new Bundle();
        data.putInt("id", 0);
        data.putString("title", "Blank");
        data.putString("url", API.GET_NEWS_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Blank");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 1);
        data.putString("title", "World");
        data.putString("url", API.GET_WORLD_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "World");


        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 3);
        data.putString("title", "Life");
        data.putString("url", API.GET_LIFE_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Life");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 4);
        data.putString("title", "Game");
        data.putString("url", API.GET_GAME_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Game");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 5);
        data.putString("title", "Design");
        data.putString("url", API.GET_DESIGN_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Design");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 6);
        data.putString("title", "Book");
        data.putString("url", API.GET_BOOK_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Book");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 7);
        data.putString("title", "Movie");
        data.putString("url", API.GET_MOVIE_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Movie");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 8);
        data.putString("title", "Arts");
        data.putString("url", API.GET_ARTS_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Arts");

        viewPager.setAdapter(adapter);

    }

}
