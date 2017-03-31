package orvnge.wwnje.com.fucknews.view.Fragment;


import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orvnge.xutils.TextProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.LogUtil;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.TestActivity;
import orvnge.wwnje.com.fucknews.data.CheckString;
import orvnge.wwnje.com.fucknews.data.Finder_List_Data;
import orvnge.wwnje.com.fucknews.data.SPData;
import orvnge.wwnje.com.fucknews.utils.BlankUtils;
import orvnge.wwnje.com.fucknews.utils.CODE;
import orvnge.wwnje.com.fucknews.utils.SPUtils;
import orvnge.wwnje.com.fucknews.view.Activity.ShareNewsActivity;
import orvnge.wwnje.com.fucknews.view.Activity.HomeActivity;
import orvnge.wwnje.com.fucknews.view.Activity.TwentyActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends BaseFragment  implements TextProvider, View.OnClickListener {

    private static final String TAG = "BlankFragment";

    private Context context;

    public static ArrayList<String> Titles = new ArrayList<>();

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

    /**
     * Login 界面初始化
     */
    View View_Desc;
    EditText edit_name;
    EditText edit_password;
    TextView show_if_success;

    private void login_InitView() {
        View_Desc = getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null);
        edit_name = (EditText) View_Desc.findViewById(R.id.login_name_dialog_edit);
        edit_password = (EditText) View_Desc.findViewById(R.id.login_pwd_dialog_edit);
        show_if_success = (TextView) View_Desc.findViewById(R.id.login_show_dialog);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);

        context = getActivity();
        btn_share.setOnClickListener(this);

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
     * 粘贴内容链接
     */
    @Override
    public void onClick(View v) {

        Toast.makeText(mActivity, CheckString.Share_1, Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(getActivity()).setTitle("Add a Link")
                .setNegativeButton("取消", null)
                .setNeutralButton("粘贴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String news_link = BlankUtils.Paste(getContext());
                        if(news_link.isEmpty()){
                            Toast.makeText(mActivity, "粘贴的是空值", Toast.LENGTH_SHORT).show();
                        }else{
                            SPUtils.setParam(SPData.ss_news_name, getContext(), SPData.news_url, news_link);
                            Toast.makeText(mActivity, CheckString.Share_2, Toast.LENGTH_SHORT).show();

                            //TODO 检测链接格式
                            if(true){
                                Toast.makeText(mActivity, CheckString.Share_3, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), ShareNewsActivity.class));
                            }else{
                                Toast.makeText(mActivity, "链接格式不正确", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setPositiveButton("使用上次的链接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String news_link = (String) SPUtils.getParam(
                                SPData.ss_news_name,
                                context,
                                SPData.news_url,
                                "");
                        if(!news_link.isEmpty()){
                            SPUtils.setParam(SPData.ss_news_name, context, SPData.news_url, news_link);
                            Toast.makeText(context, news_link, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), ShareNewsActivity.class));
                        }else{
                            Toast.makeText(context, "空值", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
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
        return Finder_List_Data.Fragments.get(position);
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
            Log.d(TAG, "对应的Fragment: ------" + "position:" + position + Finder_List_Data.Fragments.get(position).getArguments().getString("type"));
            Log.d(TAG, "Fragment数量" + mProvider.getCount() + "，"+Finder_List_Data.Fragments.size());

            return mProvider.getTrag(position);
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
            Log.d(TAG, "Fragment数量" + mProvider.getCount() + "，" + Finder_List_Data.Fragments.size());

            for(int i = 0; i< Finder_List_Data.Fragments.size(); i++){
                Log.d(TAG, "Fragment里面的东西:" + Finder_List_Data.Fragments.get(i).getArguments().getInt("id")
                        + Finder_List_Data.Fragments.get(i).getArguments().getString("type") );
            }
            //return baseId + position;
            return Finder_List_Data.Fragments.get(position).getArguments().getInt("id");
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

        Finder_List_Data.Fragments = new ArrayList<>();
        Finder_List_Data.NEWS_TYPE_NAME = new ArrayList<>();

        Bundle data;
        Finder_List_Data.NEWS_TYPE_NAME.add("Blank");
        Finder_List_Data.NEWS_TYPE_ID.add(0);


        for(int i = 0; i < Finder_List_Data.NEWS_TYPE_NAME.size(); i++){
            ContentFragment newfragment = new ContentFragment();
            data = new Bundle();
            data.putInt("id", i);
            data.putString("type", Finder_List_Data.NEWS_TYPE_NAME.get(i));
            data.putInt("type_id", Finder_List_Data.NEWS_TYPE_ID.get(i));

            newfragment.setArguments(data);

            Finder_List_Data.Fragments.add(newfragment);
        }

        mAdapter = new MyPagerAdapter(getChildFragmentManager(),this);
        viewPager.setAdapter(mAdapter);
    }

    /**
     * 增加
     */
    public static  void AddNewItem(String type_name, int type_id) {

        ContentFragment newfragment = new ContentFragment();

        Bundle data;
        data = new Bundle();

        data.putInt("id", Finder_List_Data.NEWS_TYPE_NAME.size());
        data.putString("type", type_name);
        data.putInt("type_id", type_id);

        newfragment.setArguments(data);

        Finder_List_Data.NEWS_TYPE_NAME.add(type_name);
        Finder_List_Data.NEWS_TYPE_ID.add(type_id);

        Finder_List_Data.Fragments.add(newfragment);

        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);
    }

    /**
     * 取消
     */
    public static void DeleteItem(int position) {

        Finder_List_Data.NEWS_TYPE_NAME.remove(position);
        Finder_List_Data.NEWS_TYPE_ID.remove(position);

        Finder_List_Data.Fragments.remove(position);

        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);//更新
    }

    /**
     * 删去当前页面
     */
    public static void removeCurrentItem() {
        int position = viewPager.getCurrentItem();//当前view

        Finder_List_Data.NEWS_TYPE_NAME.remove(position);
        Finder_List_Data.NEWS_TYPE_ID.remove(position);

        Finder_List_Data.Fragments.remove(position);

        Log.d(TAG, "删除的的位置: " + position);

        mAdapter.notifyDataSetChanged();
        viewPager.setAdapter(mAdapter);//更新
    }
}
