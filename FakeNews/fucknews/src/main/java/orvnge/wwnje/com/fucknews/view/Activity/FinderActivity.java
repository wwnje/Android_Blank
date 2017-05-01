package orvnge.wwnje.com.fucknews.view.Activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.adapter.MyNewsAdapter;
import orvnge.wwnje.com.fucknews.bean.BookMarkBean;
import orvnge.wwnje.com.fucknews.bean.NewsBean;
import orvnge.wwnje.com.fucknews.data.FinderData;
import orvnge.wwnje.com.fucknews.utils.BlankAPI;
import orvnge.wwnje.com.fucknews.utils.MyApplication;

/**
 * 我所有发布的内容
 */
public class FinderActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FinderActivity";


    @Bind(R.id.itemsbase_recycleview)
    RecyclerView recycleView;
    @Bind(R.id.itemsbase_swip)
    SwipeRefreshLayout swip;

    private MyNewsAdapter myNewsAdapter;
    private List<NewsBean> books = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    private Handler handler;
    private Runnable runnable;
    private int page = 1;

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;
    private boolean isRefresh;
    //标志当前页面是否可见
    private boolean isVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemsbase);
        setTitle("ALL My NEWS");
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);

        myNewsAdapter = new MyNewsAdapter(getApplicationContext());
        recycleView.setAdapter(myNewsAdapter);

        //点击进行订阅
        myNewsAdapter.setOnItemClickListener(new MyNewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //此处实现onItemClick的接口
                TextView tvRecycleViewItemText = (TextView) view.findViewById(R.id.item_tags_name);
                //如果字体本来是黑色就变成红色，反之就变为黑色
//                if (tvRecycleViewItemText.getCurrentTextColor() == Color.BLACK) {
//                    tvRecycleViewItemText.setTextColor(Color.RED);
//                    //订阅
//                } else {
//                    tvRecycleViewItemText.setTextColor(Color.BLACK);
//                }
            }
        });

        swip.setOnRefreshListener(this);

        //进入就刷新
        swip.post(new Runnable() {
            @Override
            public void run() {
                swip.setRefreshing(true);
                new LoadAllAppsTask().execute("Test AsyncTask");
            }
        });
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        page = 1;
        new LoadAllAppsTask().execute("Test AsyncTask");
    }

    /**
     * 标签请求数据
     */
    private class LoadAllAppsTask extends AsyncTask<String, Integer, Long> {

        /**
         * 后台处理 请求
         *
         * @param params
         * @return
         */
        @Override
        protected Long doInBackground(String... params) {
            getBookMarks(0, 20);//请求
            return 100L;
        }
    }
//    private void setList() {
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                int start = 20 * (page - 1);
//                if (BlankUtils.isOpenNetwork(getApplicationContext())) {
//                    getALLMyNews(start, page * 20);
//                } else {
//                    Toast.makeText(getApplicationContext(), "没有网络连接", Toast.LENGTH_SHORT).show();
//                }
//                myNewsAdapter.notifyDataSetChanged();
//                swip.setRefreshing(false);
//                isRefresh = false;
//            }
//        };
//        handler = new Handler();
//        handler.postDelayed(runnable, 500);
//
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    //Volley请求
    /*
    * POST
    * offset起始点
    * limit最多条数
     */
    public void getBookMarks(int offset, int limit) {//传递进来
        Map<String, String> params = new HashMap<String, String>();

        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("finder_id", String.valueOf(FinderData.FINDER_ID));

        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BlankAPI.GET_MY_SHARED_NEWS,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response.toString());
                        try {
                            JSONArray array = response.getJSONArray("my_news");
                            for (int j = 0; j < array.length(); j++) {
                                add(array.getJSONObject(j));
                            }
                            //请求完成
                            myNewsAdapter.addAll(books);
                            books = new ArrayList<>();//清除

                            //处理完毕进行设置
                            swip.setRefreshing(false);
                            myNewsAdapter.notifyDataSetChanged();

                            Toast.makeText(FinderActivity.this, "获取书签成功", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swip.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "刷新出错" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        MyApplication.getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * Volley获取加入
     */
    public void add(JSONObject jsonObject) {
        try {
            String news_title = jsonObject.getString("news_title");
            String news_desc = jsonObject.getString("news_desc");
            String news_content_url = jsonObject.getString("news_content_url");
            String type = jsonObject.getString("type_name");
            String news_pic_url = jsonObject.getString("news_pic_url");
            int news_id = jsonObject.getInt("news_id");
            Integer tags_id = jsonObject.getInt("tags_id");
            Integer type_id = jsonObject.getInt("type_id");

            NewsBean data = new NewsBean();

            data.setNews_id(news_id);
            data.setContent_url(news_content_url);
            data.setDesc(news_desc);
            data.setPic_url(news_pic_url);
            data.setTitle(news_title);

            data.setType_id(type_id);
            data.setTags_id(tags_id);

            data.setType(type);

            books.add(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
