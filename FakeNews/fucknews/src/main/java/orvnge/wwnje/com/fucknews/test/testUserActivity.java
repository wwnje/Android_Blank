package orvnge.wwnje.com.fucknews.test;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.data.FinderData;
import orvnge.wwnje.com.fucknews.data.Finder_List_Data;
import orvnge.wwnje.com.fucknews.utils.BlankNetMehod;

/**
 * 模拟用户 注册 登录 上传内容
 */
public class testUserActivity extends AppCompatActivity {

    @Bind(R.id.test_bookmark)Button test_Bookmark;
    @Bind(R.id.test_de_bookmark)Button test_de_bookmark;
    @Bind(R.id.test_like)Button test_like;
    @Bind(R.id.test_de_like)Button test_de_like;
    @Bind(R.id.test_share)Button test_share;
    @Bind(R.id.test_sys)Button test_sys;
    @Bind(R.id.test_login)Button test_login;
    @Bind(R.id.test_re)Button test_re;
    @Bind(R.id.test_add_news)Button test_add_news;

    private int testCount = 100;

    private Context context;
    private String news_link = "http://wwnje.com";
    private String news_img_link = "http://wwnje.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_user);
        ButterKnife.bind(this);
        context = getApplicationContext();
    }

    /**
     * 模拟添加新闻
     * 添加新闻的type是登陆用户选择后的type
     * 还有tag选择
     *
     */
    private void TestADDNews(){
        for(int i = 0; i < testCount; i++){
            int news_type_id = (int)Math.random() * Finder_List_Data.NEWS_TYPE_NAME.size();
            String new_type = Finder_List_Data.NEWS_TYPE_NAME.get(news_type_id);

            //类型,标题,描述,链接
            BlankNetMehod.Share_NEWS(context,new_type, i+"", i+"", news_link, news_img_link);
        }
    }


    /**
     * 模拟登陆
     */
    private void TestLogin(){

    }

    /**
     * 模拟注册
     */
    private void TestRe(){

    }

    /**
     * 模拟订阅
     */
    private void TestSubTags(){

    }
}
