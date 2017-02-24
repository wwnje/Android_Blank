package orvnge.wwnje.com.fucknews.view.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.adapter.NewsAdapter;
import orvnge.wwnje.com.fucknews.bean.NewsBean;
import orvnge.wwnje.com.fucknews.utils.MyApplication;
import orvnge.wwnje.com.fucknews.utils.MyUtils;
import orvnge.wwnje.com.fucknews.view.Activity.TagsActivity;

//TODO:列表显示的时候不转圈

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ContentFragment";
    private RecyclerView.LayoutManager layoutManager;

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    //标志当前页面是否可见
    private RecyclerView mPullLoadMoreRecyclerView;
    private SwipeRefreshLayout swip;
    private NewsAdapter mNewsAdapter;
    private Handler handler;
    private Runnable runnable;
    private int page = 1;
    private String url;//文章url
    private String mTitle;//文章标题
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

        url = this.getArguments().getString("url");
        mTitle = this.getArguments().getString("title");


        mPullLoadMoreRecyclerView = (RecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        swip = (SwipeRefreshLayout) view.findViewById(R.id.content_swip);

        layoutManager = new LinearLayoutManager(getActivity());
        mPullLoadMoreRecyclerView.setLayoutManager(layoutManager);

        mPullLoadMoreRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mNewsAdapter);

//        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                int start = 20 * (page - 1);
//                new LoadAllAppsTask().execute(start);
//            }
//
//            @Override
//            public void onLoadMore() {
//                loadMore();
//            }
//        });
//        isPrepared = true;
        //lazyLoad();

        mPullLoadMoreRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    new LoadAllAppsTask().execute(loadMore());
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
                url,
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
            Log.d(TAG, "add: ");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(runnable);
    }
}
