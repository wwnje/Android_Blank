package orvnge.wwnje.com.fucknews.view.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import java.util.HashMap;
import java.util.Map;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.adapter.NewsAdapter;
import orvnge.wwnje.com.fucknews.bean.NewsBean;
import orvnge.wwnje.com.fucknews.utils.MyApplication;
import orvnge.wwnje.com.fucknews.utils.MyUtils;

//TODO:列表显示的时候不转圈
/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    private static final String TAG = "ContentFragment";

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;
    //标志当前页面是否可见
    private boolean isVisible;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private NewsAdapter mNewsAdapter;
    private Handler handler;
    private Runnable runnable;
    private int page = 1;
    private String url;//文章url
    private String mTitle;//文章标题

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


        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mNewsAdapter = new NewsAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mNewsAdapter);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mNewsAdapter.clear();
                setList();
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        isPrepared = true;
        lazyLoad();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        setList();
    }

    protected void onInvisible() {
    }

    protected void loadMore() {
        page++;
        setList();
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
            mNewsAdapter.add(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setList() {
        runnable = new Runnable() {
            @Override
            public void run() {
                int start = 20 * (page - 1);
                if(MyUtils.isOpenNetwork(getActivity())) {
                    get(start, page * 20);
                }else {
                    Toast.makeText(getActivity(), "没有网络连接", Toast.LENGTH_SHORT).show();
                }
                mNewsAdapter.notifyDataSetChanged();
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
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
}
