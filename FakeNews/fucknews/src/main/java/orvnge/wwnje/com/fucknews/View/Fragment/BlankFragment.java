package orvnge.wwnje.com.fucknews.view.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.LogUtil;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.model.MyAPI;
import orvnge.wwnje.com.fucknews.view.Activity.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends BaseFragment {
    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);

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
        searchView.setQueryHint("搜索…");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showToast("query=" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LogUtil.d("onQueryTextChange=" + s);
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
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
                        Toast.makeText(mActivity, "BlanFragment", Toast.LENGTH_SHORT).show();
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
        data.putString("url", MyAPI.GET_NEWS_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Blank");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 1);
        data.putString("title", "World");
        data.putString("url", MyAPI.GET_WORLD_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "World");


        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 3);
        data.putString("title", "Life");
        data.putString("url", MyAPI.GET_LIFE_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Life");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 4);
        data.putString("title", "Game");
        data.putString("url", MyAPI.GET_GAME_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Game");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 5);
        data.putString("title", "Design");
        data.putString("url", MyAPI.GET_DESIGN_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Design");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 6);
        data.putString("title", "Book");
        data.putString("url", MyAPI.GET_BOOK_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Book");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 7);
        data.putString("title", "Movie");
        data.putString("url", MyAPI.GET_MOVIE_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Movie");

        newfragment = new ContentFragment();
        data = new Bundle();
        data.putInt("id", 8);
        data.putString("title", "Arts");
        data.putString("url", MyAPI.GET_ARTS_URL);
        newfragment.setArguments(data);
        adapter.addFrag(newfragment, "Arts");

        viewPager.setAdapter(adapter);

    }

}
