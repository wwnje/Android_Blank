package orvnge.wwnje.com.fucknews.view.Activity;

import com.orvnge.xutils.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orvnge.xutils.recyclevIew.RecycleViewActivity;

import orvnge.wwnje.com.fucknews.other.AppConstants;
import orvnge.wwnje.com.fucknews.LogUtil;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.utils.EventManager;
import orvnge.wwnje.com.fucknews.utils.SharedPreferencesUtils;
import orvnge.wwnje.com.fucknews.utils.myCheckTools;
import orvnge.wwnje.com.fucknews.view.Fragment.BlankFragment;
import orvnge.wwnje.com.fucknews.view.Fragment.TwentyFragment;
public class HomeActivity extends BaseActivity {

    private Snackbar snackbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private Fragment currentFragment;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initNavigationViewHeader();
        initFragment(savedInstanceState);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = new BlankFragment();
            switchContent(currentFragment);
        } else {
            //activity销毁后记住销毁前所在页面，用于夜间模式切换
            currentIndex = savedInstanceState.getInt(AppConstants.CURRENT_INDEX);
            switch (this.currentIndex) {
                case 0:
                    currentFragment = new BlankFragment();
                    switchContent(currentFragment);
                    break;
                case 1:
                    currentFragment = new TwentyFragment();
                    switchContent(currentFragment);
                    break;
                case 2:
                    /*currentFragment = new LifeFragment();
                    switchContent(currentFragment);*/
                    break;
            }
        }
    }

    public void switchContent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment).commit();
        invalidateOptionsMenu();
    }

    private void initNavigationViewHeader() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //设置头像，布局app:headerLayout="@layout/drawer_header"所指定的头布局
        View view = navigationView.inflateHeaderView(R.layout.activity_home_nav_header_home);

        ImageView view2 = (ImageView) view.findViewById(R.id.imageView);
        Glide
                .with(getApplicationContext())
                .load(R.drawable.testgif)
                .error(R.drawable.nav1)
                .placeholder(R.drawable.nav1)
                .into(view2);

        if ((String) SharedPreferencesUtils.getParam("me", getApplicationContext(), "name", "Finder") != null)
            navigationView.getMenu().getItem(0).setTitle((String) SharedPreferencesUtils.getParam("me", getApplicationContext(), "name", "finder"));

        //View mNavigationViewHeader = View.inflate(HomeActivity.this, R.layout.drawer_header, null);
        //navigationView.addHeaderView(mNavigationViewHeader);//此方法在魅族note 1，头像显示不全
        //菜单点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelected());
    }

    class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(final MenuItem menuItem) {

            mDrawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.nav_me:
                    login_InitView();
//                    if((String)SharedPreferencesUtils.getParam("me", getApplicationContext(), "name", "Finder") != "Finder"){
//                        Toast.makeText(HomeActivity.this, (String)SharedPreferencesUtils.getParam("me", getApplicationContext(), "name", "Finder"), Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(HomeActivity.this, MeActivity.class));
//                    }else {
                    new AlertDialog.Builder(HomeActivity.this).setTitle("hello 发现者")
                            .setView(View_Desc)
                            .setNegativeButton("cancel", null)
                            .setNeutralButton("注册", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //注册操作
                                    String name = edit_name.getText().toString();
                                    String pwd = edit_password.getText().toString();
                                    if (myCheckTools.CheckLength(name, 10) && myCheckTools.CheckLength(pwd, 10)) {
                                        EventManager.Register(getApplicationContext(), show_if_success, name, pwd);
                                        Toast.makeText(mActivity, "注册", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(HomeActivity.this, "姓名和密码不能为空并且10位以内", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //登陆操作
                                    String name = edit_name.getText().toString();
                                    String pwd = edit_password.getText().toString();
                                    if (myCheckTools.CheckLength(name, 10) && myCheckTools.CheckLength(pwd, 10)) {
                                        EventManager.Login(getApplicationContext(), name, pwd, menuItem);
                                    } else {
                                        Toast.makeText(HomeActivity.this, "格式不正确", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).show();
//            }
                    break;

                case R.id.nav_about:
                    snackbar =
                            Snackbar.make(mDrawerLayout, "By wwnje", Snackbar.LENGTH_LONG)
                                    .setAction("提交Bug", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {//提交Bug
                                            try {
                                                Uri uri = Uri.parse(getString(R.string.mail_account));
                                                Intent intent = new Intent(Intent.ACTION_SENDTO, uri); //邮箱账号
                                                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject)); //主题
                                                intent.putExtra(Intent.EXTRA_TEXT,
                                                        getString(R.string.device_model) + Build.MODEL   //设备
                                                                + "\n" + getString(R.string.sdk_version)    //手机版本
                                                                + Build.VERSION.RELEASE + "\n");
                                                startActivity(intent);
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Snackbar.make(mDrawerLayout, R.string.no_mail_app, Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                    snackbar.show();
                    break;
                case R.id.nav_tags:
                    startActivity(new Intent(HomeActivity.this, TwentyActivity.class));
                    break;

                case R.id.v_score: //评分
                    try {
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Snackbar.make(mDrawerLayout, R.string.no_market_app, Snackbar.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.v_t:
                    Toast.makeText(mActivity, test.get("hello"), Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(HomeActivity.this, GitHubActivity.class));
                    startActivity(new Intent(HomeActivity.this, RecycleViewActivity.class));
                    break;

             /*   case R.id.navigation_item_night:
                    SharedPreferencesUtil.setBoolean(mActivity, AppConstants.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                    return true;
                case R.id.navigation_item_day:
                    SharedPreferencesUtil.setBoolean(mActivity, AppConstants.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                    return true;*/
                /*case R.id.navigation_item_1:
                    currentIndex = 0;
                    menuItem.setChecked(true);
                    currentFragment = new FristFragment();
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_item_2:
                    currentIndex = 2;
                    menuItem.setChecked(true);
                    currentFragment = new ThirdFragment();
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_item_3:
                    currentIndex = 1;
                    menuItem.setChecked(true);
                    currentFragment = new SecondFragment();
                    switchContent(currentFragment);
                    return true;
                case R.id.navigation_item_night:
                    SharedPreferencesUtil.setBoolean(mActivity, AppConstants.ISNIGHT, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                    return true;
                case R.id.navigation_item_day:
                    SharedPreferencesUtil.setBoolean(mActivity, AppConstants.ISNIGHT, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                    return true;*/
                default:
                    return true;
            }
            return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.d("onSaveInstanceState=" + currentIndex);
        outState.putInt(AppConstants.CURRENT_INDEX, currentIndex);
        super.onSaveInstanceState(outState);
    }

    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);//声明
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_copyright:
                break;
            default:
                //对没有处理的事件，交给父类来处理
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    View View_Desc;
    EditText edit_name;
    EditText edit_password;
    TextView show_if_success;

    private void login_InitView() {
        //login界面数据
        View_Desc = getLayoutInflater().inflate(R.layout.dialog_login, null);
        edit_name = (EditText) View_Desc.findViewById(R.id.login_name_dialog_edit);
        edit_password = (EditText) View_Desc.findViewById(R.id.login_pwd_dialog_edit);
        show_if_success = (TextView) View_Desc.findViewById(R.id.login_show_dialog);
    }

//    private void saveMyMessage() {
//        //保存信息
//        SharedPreferencesUtils.setParam("my_sharedPreferences", getApplicationContext(), "name", name);
//        SharedPreferencesUtils.setParam("my_sharedPreferences", getApplicationContext(), "password", topic);
//        SharedPreferencesUtils.setParam("my_sharedPreferences", getApplicationContext(), "name", name);
//        SharedPreferencesUtils.setParam("my_sharedPreferences", getApplicationContext(), "tel", tel);
//        SharedPreferencesUtils.setParam("my_sharedPreferences", getApplicationContext(), "email", email);
//        SharedPreferencesUtils.setParam("my_sharedPreferences", getApplicationContext(), "message", message);
//    }
}
