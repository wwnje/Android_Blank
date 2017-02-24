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
import orvnge.wwnje.com.fucknews.adapter.TagsAdapter;
import orvnge.wwnje.com.fucknews.bean.TagsBean;
import orvnge.wwnje.com.fucknews.data.FinderData;
import orvnge.wwnje.com.fucknews.utils.API;
import orvnge.wwnje.com.fucknews.utils.MyApplication;
import orvnge.wwnje.com.fucknews.utils.MyUtils;
import orvnge.wwnje.com.fucknews.utils.SharedPreferencesUtils;

/**
 * 用户订阅显示界面
 */
public class SubscribeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SubscribeActivity";

    @Bind(R.id.tags_recycleview)
    RecyclerView recycleView;
    @Bind(R.id.tags_swip)
    SwipeRefreshLayout swip;

    private TagsAdapter tagsAdapter;

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
        setContentView(R.layout.activity_tags);
        setTitle("我订阅的标签");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);

        tagsAdapter = new TagsAdapter(getApplicationContext());
        recycleView.setAdapter(tagsAdapter);

        //点击进行订阅
        tagsAdapter.setOnItemClickListener(new TagsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //此处实现onItemClick的接口
                TextView tvRecycleViewItemText = (TextView) view.findViewById(R.id.item_tags_name);
                //如果字体本来是黑色就变成红色，反之就变为黑色
                if (tvRecycleViewItemText.getCurrentTextColor() == Color.BLACK){
                    tvRecycleViewItemText.setTextColor(Color.RED);
                    //订阅
                }
                else{
                    tvRecycleViewItemText.setTextColor(Color.BLACK);
                }
            }
        });

        //禁止刷新移动  放刷新最下面
//        recycleView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return isRefresh;
//            }
//        });

        swip.setOnRefreshListener(this);

        //进入就刷新
        swip.post(new Runnable() {
            @Override
            public void run() {
                swip.setRefreshing(true);
                isRefresh = true;
                page = 1;
                //tagsAdapter.clear();
                setList();
            }
        });
        isPrepared = true;
    }



    //下拉刷新
    @Override
    public void onRefresh() {
        if(!isRefresh){
            isRefresh = true;
            page = 1;
            tagsAdapter.clear();
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
                if(MyUtils.isOpenNetwork(getApplicationContext())) {
                    getMyTags(start, page * 20);
                }else {
                    Toast.makeText(getApplicationContext(), "没有网络连接", Toast.LENGTH_SHORT).show();
                }
                tagsAdapter.notifyDataSetChanged();
                swip.setRefreshing(false);
                isRefresh= false;
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
    public void getMyTags(int offset, int limit) {//传递进来

        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("finder_id", String.valueOf(FinderData.finder_id));
        params.put("myTags_version", String.valueOf(FinderData.MyTagsVersion));

        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                API.GET_MY_TAGS_URL,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("tags");
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
                Toast.makeText(getApplicationContext(), "获取订阅标签出错" + error.toString(), Toast.LENGTH_SHORT).show();
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
            String tags_name = jsonObject.getString("tags_name");
            String tags_id = jsonObject.getString("tags_id");

            TagsBean data = new TagsBean();
            data.setTags_name(tags_name);
            data.setTags_id(Integer.parseInt(tags_id));

            tagsAdapter.add(data);
//            Toast.makeText(this, "正在加载数据..." + data.getTags_name(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
