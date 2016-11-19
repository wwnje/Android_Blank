package orvnge.wwnje.com.fucknews.View.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orvnge.wwnje.com.fucknews.Adapter.HomeTagsNameAdapter;
import orvnge.wwnje.com.fucknews.Model.PHP_Data;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.View.Fragment.ArtsFragment;
import orvnge.wwnje.com.fucknews.View.Fragment.BlankFragment;
import orvnge.wwnje.com.fucknews.View.Fragment.BookFragment;
import orvnge.wwnje.com.fucknews.View.Fragment.DesignFragment;
import orvnge.wwnje.com.fucknews.View.Fragment.GameFragment;
import orvnge.wwnje.com.fucknews.View.Fragment.LifeFragment;
import orvnge.wwnje.com.fucknews.View.Fragment.MovieFragment;
import orvnge.wwnje.com.fucknews.View.Fragment.WorldlFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;//侧边栏
    DrawerLayout drawer;
    ActionBarDrawerToggle mDrawerToggle;

    ViewPager viewPager;
    TabLayout tabs;
    Snackbar snackbar;
    Toolbar toolbar;

    public FragmentManager fm = getSupportFragmentManager();

    View View_Desc;
    EditText edit_name;
    EditText edit_password;
    TextView show_if_success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(this, R.layout.activity_home, null);
        setContentView(view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // 打開 up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        drawer.setDrawerListener(mDrawerToggle);

        tabs = (TabLayout) findViewById(R.id.tabs);//Tabs滑动

        List<Fragment> fragments = new ArrayList<>();
        Fragment fragment1 = new BlankFragment();
        Fragment fragment2 = new WorldlFragment();
        Fragment fragment3 = new LifeFragment();
        //Fragment fragment4 = new CodeFragment();
        Fragment fragment5 = new GameFragment();
        Fragment fragment6 = new MovieFragment();
        //Fragment fragment7 = new TechnologyFragment();
        Fragment fragment8 = new DesignFragment();
        Fragment fragment9 = new ArtsFragment();
        Fragment fragment10 = new BookFragment();

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        //fragments.add(fragment4);
        fragments.add(fragment5);
        fragments.add(fragment6);
        //fragments.add(fragment7);
        fragments.add(fragment8);
        fragments.add(fragment9);
        fragments.add(fragment10);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new HomeTagsNameAdapter(getSupportFragmentManager(), fragments));
        tabs.post(new Runnable() {
            @Override
            public void run() {
                tabs.setupWithViewPager(viewPager);
            }
        });
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(final MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            /*case R.id.nav_home:
                break;
            case R.id.nav_tags:
                toolbar.setTitle("Like");
                drawer.closeDrawer(navigationView);
                fm.beginTransaction().replace(R.id.content_frame, new TagsFragment()).commit();
                Log.d(TAG, "onNavigationItemSelected: tags");
                break;
            case R.id.nav_manage:
                String read = FileUtil2.readString(android.os.Environment.getExternalStorageDirectory() + "/aa.txt", "utf-8");
                Log.d("Log", "read" + read);
                break;
*/
           /* case R.id.nav_talk:
                toolbar.setTitle("Talk");
                drawer.closeDrawer(navigationView);
                startActivity(new Intent(HomeActivity.this, TalkActivity.class));
                break;*/

            //若没有登陆则显示登陆界面
            case R.id.nav_me:

                login_InitView();

                new AlertDialog.Builder(this).setTitle("hello 发现者")
                        .setView(View_Desc)
                        .setNegativeButton("cancel", null)
                        .setNeutralButton("注册", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //注册操作
//                                if (!edit_name.getText().toString().equals("") && !edit_password.getText().toString().equals("")) {
//                                    register(edit_name.getText().toString(), edit_password.getText().toString());
//                                } else {
//                                    Toast.makeText(HomeActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
//                                }
                                Toast.makeText(HomeActivity.this, "手机端现不支持", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                item.setTitle(edit_name.getText());
                                //登陆操作
                            }
                        }).show();
                break;

            case R.id.nav_about:
                snackbar =
                        Snackbar.make(drawer, "By wwnje", Snackbar.LENGTH_LONG)
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
                                            Snackbar.make(drawer, R.string.no_mail_app, Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                snackbar.show();
                break;

            case R.id.v_score: //评分
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Snackbar.make(drawer, R.string.no_market_app, Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    //注册方法
    private void register(final String name, final String password) {
        //判断注册信息是否正确

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = PHP_Data.Register_Url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("username", name);
                map.put("password", password);
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
        Toast.makeText(HomeActivity.this, "注册操作" + "name:" + name + " pwd:" + password, Toast.LENGTH_SHORT).show();


    }

    Response.Listener listener = new Response.Listener() {
        @Override
        public void onResponse(Object response) {
            show_if_success.setText(response.toString());
        }
    };


    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            show_if_success.setText("error");
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);//声明
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_copyright) {
            new AlertDialog.Builder(this).setTitle("标题")
                    .setMessage(R.string.copyright_content)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            return true;
        } else if (id == R.id.action_add_news) {
            Toast.makeText(HomeActivity.this, "手机端现不支持", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(HomeActivity.this, AddNewsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void login_InitView() {
        //login界面数据
        View_Desc = getLayoutInflater().inflate(R.layout.dialog_login, null);
        edit_name = (EditText) View_Desc.findViewById(R.id.login_name_dialog_edit);
        edit_password = (EditText) View_Desc.findViewById(R.id.login_pwd_dialog_edit);
        show_if_success = (TextView) View_Desc.findViewById(R.id.login_show_dialog);
    }
}
