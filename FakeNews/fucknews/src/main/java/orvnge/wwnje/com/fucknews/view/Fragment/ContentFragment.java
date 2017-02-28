package orvnge.wwnje.com.fucknews.view.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orvnge.xutils.MyFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.adapter.NewsAdapter;
import orvnge.wwnje.com.fucknews.bean.NewsBean;
import orvnge.wwnje.com.fucknews.utils.MyApplication;

//TODO:列表显示的时候不转圈

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ContentFragment";
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swip;
    private NewsAdapter mNewsAdapter;
    private int page = 1;

    private String mText;

    public static ContentFragment newInstance(String text) {
        ContentFragment f = new ContentFragment(text);
        return f;
    }

    public ContentFragment() {
    }

    public ContentFragment(String text) {
        this.mText = text;
    }


    private List<NewsBean> newsNow = new ArrayList<>();//现在加载的新数据

    private Boolean isLoadMore = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.content_RecyclerView);
        swip = (SwipeRefreshLayout) view.findViewById(R.id.content_swip);

        initData();
        initView();
    }

    /**
     * 界面设置
     */
    private void initView() {

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(getActivity());
        mRecyclerView.setAdapter(mNewsAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    Toast.makeText(getActivity(), "滑动到底了", Toast.LENGTH_SHORT).show();
                    swip.setRefreshing(true);
                    new LoadAllAppsTask().execute(loadMore());//下拉刷新请求
                }
            }

        });

        swip.setOnRefreshListener(this);

        //进入就刷新
        swip.post(new Runnable() {
            @Override
            public void run() {
                swip.setRefreshing(true);
                int start = 20 * (page - 1);
                new LoadAllAppsTask().execute(start);
            }
        });
    }

    private String mUrl;//文章url
    private String mTitle;//文章标题

    /**
     * 获取items内容
     */
    private void initData() {
        mUrl = this.getArguments().getString("tags_url");
        mTitle = this.getArguments().getString("tags_title");
    }

    /**
     * 下拉刷新
     * @return
     */
    private Integer loadMore() {
        isLoadMore = true;
        page++;
        return  20 * (page - 1);
        //new LoadAllAppsTask().execute(start);
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        isLoadMore = false;
        page = 1;
        int start = 20 * (page - 1);
        new LoadAllAppsTask().execute(start);
    }


    /**
     * 后台操作请求
     */
    private class LoadAllAppsTask extends AsyncTask<Integer, Integer, Long> {
        @Override
        protected Long doInBackground(Integer... params) {
            getNews(params[0], page * 20);//分页
            return 100L;
        }
    }


    //Volley请求
    /*
    * POST
    * offset起始点
    * limit最多条数
     */
    public void getNews(int offset, int limit) {//传递进来

        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                mUrl,//相应标签 请求链接
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("user");

                            for (int j = 0; j < array.length(); j++) {
                                add(array.getJSONObject(j));
                            }
                            if(isLoadMore){
                                mNewsAdapter.addMore(newsNow);
                            }else {
                                mNewsAdapter.addAll(newsNow);
                            }
                            newsNow = new ArrayList<>();//清除，防止重复加入

                            swip.setRefreshing(false);
                            mNewsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "刷新出错", Toast.LENGTH_SHORT).show();
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

    private void add(JSONObject jsonObject) {
        try {
            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");
            String time = jsonObject.getString("time");
            String content_url = jsonObject.getString("content_url");
            String pic_url = jsonObject.getString("pic_url");
            String type = jsonObject.getString("type");
            String finder = jsonObject.getString("finder");

            NewsBean data = new NewsBean();
            data.setTitle(title);
            data.setDesc(desc);
            data.setTime(time);
            data.setContent_url(content_url);
            data.setPic_url(pic_url);
            data.setType(type);
            data.setFinder(finder);

            newsNow.add(data);
            //mNewsAdapter.add(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
