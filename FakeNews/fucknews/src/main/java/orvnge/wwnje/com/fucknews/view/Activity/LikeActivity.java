
package orvnge.wwnje.com.fucknews.view.Activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import orvnge.wwnje.com.fucknews.adapter.BookMarkAdapter;
import orvnge.wwnje.com.fucknews.bean.BookMarkBean;
import orvnge.wwnje.com.fucknews.data.FinderData;
import orvnge.wwnje.com.fucknews.utils.BlankAPI;
import orvnge.wwnje.com.fucknews.utils.MyApplication;

/**
 * 存放我喜欢的
 */
public class LikeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "LikeActivity";

    @Bind(R.id.itemsbase_recycleview)
    RecyclerView recycleView;
    @Bind(R.id.itemsbase_swip)
    SwipeRefreshLayout swip;

    private BookMarkAdapter bookMarkAdapter;
    private List<BookMarkBean> books = new ArrayList<>();
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
        setTitle("所有我喜欢的");
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);

        bookMarkAdapter = new BookMarkAdapter(getApplicationContext());
        recycleView.setAdapter(bookMarkAdapter);

        //点击进行订阅
        bookMarkAdapter.setOnItemClickListener(new BookMarkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //此处实现onItemClick的接口
                TextView tvRecycleViewItemText = (TextView) view.findViewById(R.id.item_tags_name);
//                //如果字体本来是黑色就变成红色，反之就变为黑色
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
//                bookMarkAdapter.notifyDataSetChanged();
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
                BlankAPI.GET_LIKELIST_URL,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response.toString());
                        try {
                            JSONArray array = response.getJSONArray("likelists");
                            for (int j = 0; j < array.length(); j++) {
                                add(array.getJSONObject(j));
                            }
                            //请求完成
                            bookMarkAdapter.addAll(books);
                            books = new ArrayList<>();//清除

                            //处理完毕进行设置
                            swip.setRefreshing(false);
                            bookMarkAdapter.notifyDataSetChanged();

                            Toast.makeText(LikeActivity.this, "获取喜欢列表成功", Toast.LENGTH_SHORT).show();

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
            String bookmark_id = jsonObject.getString("like_id");
            String news_title = jsonObject.getString("news_title");
            String news_desc = jsonObject.getString("news_desc");
            String news_content_url = jsonObject.getString("news_content_url");
            String finder_id = jsonObject.getString("finder_id");
            String finder_name = jsonObject.getString("finder_name");
            String news_pic_url = jsonObject.getString("news_pic_url");
            int news_id = jsonObject.getInt("news_id");

            Integer tags_id = jsonObject.getInt("tags_id");
            Integer type_id = jsonObject.getInt("type_id");
            String type = jsonObject.getString("type_name");

            BookMarkBean data = new BookMarkBean();

            data.setNew_id(news_id);
            data.setBookmark_id(bookmark_id);
            data.setFinder_id(finder_id);
            data.setFinder_name(finder_name);
            data.setNews_content_url(news_content_url);
            data.setNews_desc(news_desc);
            data.setNews_pic_url(news_pic_url);
            data.setNews_title(news_title);

            data.setType_id(type_id);
            data.setTags_id(tags_id);

            data.setType(type);

            books.add(data);
//            Toast.makeText(this, "正在加载数据..." + data.getItem_name(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
