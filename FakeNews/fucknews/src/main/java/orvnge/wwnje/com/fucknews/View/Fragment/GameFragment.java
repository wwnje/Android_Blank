package orvnge.wwnje.com.fucknews.View.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import orvnge.wwnje.com.fucknews.Adapter.HomeTagsAdapter;
import orvnge.wwnje.com.fucknews.Model.Tags;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.Utils.MyApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private boolean mIsRefreshing = true;

    public GameFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public GameFragment(String title) {
        mTitle = title;
    }


    String mTitle;
    View view;
    private HomeTagsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉
    private int offset = 0;
    private int limit = 100;
    /*服务器地址*/
    public static final String GET_NEWS_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Game.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank, container, false);

        initView();
        return view;
    }

    private void initView() {

        swipeRefresh();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lvNews);
        adapter = new HomeTagsAdapter(getActivity());
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsRefreshing) {
                    return true;//不能动
                } else {
                    return false;
                }
            }
        });

        //滑动事件监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastposition = manager.findLastVisibleItemPosition();//获取最后一个位置


            }
        });
    }

    //下拉刷新
    private void swipeRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                mIsRefreshing = true;

                get(offset,limit);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;

                adapter.clear();
                adapter.notifyDataSetChanged();

                get(offset,limit);

            }
        });
    }

    //Volley请求
    /*
    * POST
    * offset起始点
    * limit最多条数
     */
    public void get(int offset, int limit) {//传递进来
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                GET_NEWS_URL,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        adapter.notifyDataSetChanged();
                        try {
                            JSONArray array = response.getJSONArray("user");
                            for (int j = 0; j < array.length(); j++)
                            {
                                add(array.getJSONObject(j));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        mIsRefreshing = false;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(swipeRefreshLayout, "刷新出错" , Snackbar.LENGTH_LONG).setAction("刷新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeRefresh();
                    }
                }).show();
                swipeRefreshLayout.setRefreshing(false);
                mIsRefreshing = false;

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers  = new HashMap<>();
                headers.put("Content-Type","application/json; charset=utf-8");
                return headers;
            }
        };
        MyApplication.getRequestQueue().add(jsonObjectRequest);
    }

    private void add(JSONObject jsonObject) {
        try {
            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");
            String time = jsonObject.getString("time");
            String content_url = jsonObject.getString("content_url");
            String pic_url = jsonObject.getString("pic_url");
            String type = jsonObject.getString("type");

            Tags data = new Tags();
            data.setTitle(title);
            data.setDesc(desc);
            data.setTime(time);
            data.setContent_url(content_url);
            data.setPic_url(pic_url);
            data.setType(type);
            adapter.add(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}