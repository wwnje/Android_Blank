package orvnge.wwnje.com.fakenews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fakenews.adapter.NewsAdapter;
import orvnge.wwnje.com.fakenews.model.News;
import orvnge.wwnje.com.fakenews.utils.HttpUtils;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {


    private static final String TAG = "MainActivity";

    private ListView lvNews;
    private NewsAdapter adapter;
    private List<News> newsList;

    /*服务器地址*/
    public static final String GET_NEWS_URL = "http://115.159.149.175/FakeNews/getNewsJSON.php";

    private Handler getNewsHandler = new Handler(){//获取数据
        public void handleMessage(android.os.Message msg) { //工具类handler.sendMessage(msg)
            String jsonData = (String) msg.obj;
            Log.d(TAG, "handleMessage: "+jsonData);//json数据
            try {//解析
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("desc");
                    String time = object.getString("time");
                    String content_url = object.getString("content_url");
                    String pic_url = object.getString("pic_url");
                    newsList.add(new News(title, desc, time, content_url, pic_url));
                }
                adapter.notifyDataSetChanged();//数据变化更新
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvNews = (ListView) findViewById(R.id.lvNews);
        newsList = new ArrayList<News>();
        adapter = new NewsAdapter(this, newsList);//绑定

        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(this);//点击事件

        HttpUtils.getNewsJSON(GET_NEWS_URL, getNewsHandler);
    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        News news = newsList.get(position);
        Intent intent = new Intent(this, BrowseNewsActivity.class);
        intent.putExtra("content_url", news.getContent_url());//参数给下一个activity
        startActivity(intent);
    }
}
