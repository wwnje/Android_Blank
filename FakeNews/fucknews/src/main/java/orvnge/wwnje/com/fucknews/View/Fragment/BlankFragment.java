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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
    private DatabaseHelper dbHelper;

    static ArrayList<Fragment> Fragments = new ArrayList<>();
    static ArrayList<String> Titles = new ArrayList<>();

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

        dbHelper = new DatabaseHelper(getActivity(), DatabaseHelper.DATABASE_LOCAL_MESSAGE, null, DatabaseHelper.DATABASE_LOCAL_MESSAGE_VERSION);

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
        return Titles.get(position);
    }

    @Override
    public int getCount() {
        return Titles.size();
    }

    @Override
    public Fragment getTrag(int position) {
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

        @Override
        public Fragment getItem(int position) {
            return Fragments.get(position);
            //return ContentFragment.newInstance(mProvider.getTextForPosition(position));
        }

        @Override
        public int getCount() {
            return mProvider.getCount();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles.get(position);
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



        List<String> typeNames = new ArrayList<>();
        List<String> typeUrls = new ArrayList<>();

        typeNames.add("Blank");
        typeUrls.add(BlankAPI.GET_NEWS_URL);//全部

        /**
         * 读取本地local新闻类型
         */
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询所有数据
        Cursor cursor = db.query(DatabaseHelper.DB_TABLE_NEWSTYPE_LOCAL,//表明
                null,//查询列名
                null,//where约束条件
                null ,//where具体值
                null ,//group by的列
                null,//进一步约束
                null);//order by排列方式

        if(cursor.moveToFirst()){//移到第一条数据
            do{
                //遍历cursor对象
                String type_name = cursor.getString(cursor.getColumnIndex("type_name"));
                String type_url = "http://www.wwnje.com/FakeNews/getNewsJSON_" + type_name + ".php";
                //int type_id = cursor.getInt(cursor.getColumnIndex("type_id"));

                //存放订阅后的标签页
                typeNames.add(type_name);
                typeUrls.add(type_url);
            }while (cursor.moveToNext());
        }
        cursor.close();

        Fragment newfragment;
        Bundle data;

        for(int i = 0; i < typeNames.size(); i++){
            newfragment = new ContentFragment();
            data = new Bundle();
            data.putInt("id", i);
            data.putString("tags_title", typeNames.get(i));
            data.putString("tags_url", typeUrls.get(i));

            newfragment.setArguments(data);
            Fragments.add(newfragment);
            Titles.add(typeNames.get(i));
        }

        mAdapter = new MyPagerAdapter(getChildFragmentManager(),this);
        viewPager.setAdapter(mAdapter);

    }

    private void addNewItem() {
        Titles.add("new item");
        mAdapter.notifyDataSetChanged();
    }

    public static void removeCurrentItem() {
        int position = viewPager.getCurrentItem();
        Titles.remove(position);
        Fragments.remove(position);
        mAdapter.notifyDataSetChanged();
    }

}
