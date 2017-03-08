
package orvnge.wwnje.com.fucknews.view.Activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
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
import orvnge.wwnje.com.fucknews.utils.MyUtils;

/**
 * 书签待阅读
 */
public class BookMarkActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "BookMarkActivity";

    @Bind(R.id.itemsbase_recycleview)
    RecyclerView recycleView;
    @Bind(R.id.itemsbase_swip)
    SwipeRefreshLayout swip;

    private BookMarkAdapter bookMarkAdapter;
    private List<String> mdata = new ArrayList<>();
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
        setTitle("所有书签待阅读");
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);

        bookMarkAdapter = new BookMarkAdapter(getApplicationContext(), mdata);
        recycleView.setAdapter(bookMarkAdapter);

        //点击进行订阅
        bookMarkAdapter.setOnItemClickListener(new BookMarkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //此处实现onItemClick的接口
                TextView tvRecycleViewItemText = (TextView) view.findViewById(R.id.item_tags_name);
                //如果字体本来是黑色就变成红色，反之就变为黑色
                if (tvRecycleViewItemText.getCurrentTextColor() == Color.BLACK) {
                    tvRecycleViewItemText.setTextColor(Color.RED);
                    //订阅
                } else {
                    tvRecycleViewItemText.setTextColor(Color.BLACK);
                }
            }
        });

        //禁止刷新移动  放刷新最下面
        recycleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isRefresh;
            }
        });
        swip.setOnRefreshListener(this);

        //进入就刷新
        swip.post(new Runnable() {
            @Override
            public void run() {
                swip.setRefreshing(true);
                isRefresh = true;
                page = 1;
                bookMarkAdapter.clear();
                setList();
            }
        });
        isPrepared = true;
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            page = 1;
            bookMarkAdapter.clear();
            setList();
        }
    }

    /**
     * 标签请求数据
     */
    private void setList() {
        runnable = new Runnable() {
            @Override
            public void run() {
                int start = 20 * (page - 1);
                if (MyUtils.isOpenNetwork(getApplicationContext())) {
                    getALLTags(start, page * 20);
                } else {
                    Toast.makeText(getApplicationContext(), "没有网络连接", Toast.LENGTH_SHORT).show();
                }
                bookMarkAdapter.notifyDataSetChanged();
                swip.setRefreshing(false);
                isRefresh = false;
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 500);

    }

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
    public void getALLTags(int offset, int limit) {//传递进来
//        Toast.makeText(this, "正在发起请求", Toast.LENGTH_SHORT).show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("FINDER_ID", String.valueOf(FinderData.FINDER_ID));
        params.put("book_version", String.valueOf(FinderData.BookVersion));

        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BlankAPI.GET_BOOKMARK_URL,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("bookmarks");
                            for (int j = 0; j < array.length(); j++) {
                                add(array.getJSONObject(j));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "刷新出错", Toast.LENGTH_SHORT).show();
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
            String bookmark_id = jsonObject.getString("bookmark_id");
            String news_title = jsonObject.getString("news_title");
            String news_desc = jsonObject.getString("news_desc");
            String news_content_url = jsonObject.getString("news_pic_url");
            String type = jsonObject.getString("type");
            String finder_id = jsonObject.getString("FINDER_ID");
            String finder_name = jsonObject.getString("finder_name");
            String book_version = jsonObject.getString("book_version");
            String news_pic_url = jsonObject.getString("news_pic_url");

            BookMarkBean data = new BookMarkBean();

            data.setBookmark_id(bookmark_id);
            data.setBook_version(Integer.parseInt(book_version));
            data.setFinder_id(finder_id);
            data.setFinder_name(finder_name);
            data.setNews_content_url(news_content_url);
            data.setNews_desc(news_desc);
            data.setNews_pic_url(news_pic_url);
            data.setNews_title(news_title);
            data.setType(type);

            bookMarkAdapter.add(data);
//            Toast.makeText(this, "正在加载数据..." + data.getTags_name(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
