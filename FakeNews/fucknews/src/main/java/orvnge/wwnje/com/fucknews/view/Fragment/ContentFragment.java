package orvnge.wwnje.com.fucknews.view.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.adapter.NewsAdapter;
import orvnge.wwnje.com.fucknews.bean.NewsBean;
import orvnge.wwnje.com.fucknews.data.FinderData;
import orvnge.wwnje.com.fucknews.data.VariateName;
import orvnge.wwnje.com.fucknews.ibean.IAdapeter;
import orvnge.wwnje.com.fucknews.utils.BlankAPI;
import orvnge.wwnje.com.fucknews.utils.BlankNetMehod;
import orvnge.wwnje.com.fucknews.utils.MyApplication;

//TODO:列表显示的时候不转圈

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private int pressed = R.drawable.btn_bookmark_style_pressed;
    private int unpressed = R.drawable.btn_bookmark_style_unpressed;

    private int index;
    private String bookmarkText;

    private static final String TAG = "ContentFragment";
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swip;
    public NewsAdapter mNewsAdapter;
    private int page = 1;

    private List<NewsBean> newsNow = new ArrayList<>();//现在加载的新数据

    private Boolean isLoadMore = false;

    public static final String ARGUMENT = "type";

    private int mInt;
    private String mType;//文章标题
    private int type_id;

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
        initClickEvenet();
    }

    /**
     * 点击订阅按钮
     */
    private void initClickEvenet() {
        mNewsAdapter.setOnItemClickListener(new IAdapeter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Button btn_bookmark = (Button) view.findViewById(R.id.btn_bookmark);
                String str_book = btn_bookmark.getText().toString();
                int news_id = mNewsAdapter.newsBeen.get(position).getNews_id();

                if (str_book.equals(VariateName.BookMark)) {
                    BlankNetMehod.NewsClick_LIKE_OR_BOOKMARK(getContext(), news_id, VariateName.ADDBOOKMARK, "true");
                    bookmarkText = VariateName.BookMarked;
                    index = pressed;
                } else {
                    BlankNetMehod.NewsClick_LIKE_OR_BOOKMARK(getContext(), news_id, VariateName.ADDBOOKMARK, "false");
                    bookmarkText = VariateName.BookMark;
                    index = unpressed;
                }
                //btn_bookmark.setBackgroundResource(index);
                btn_bookmark.setBackgroundResource(pressed);
                btn_bookmark.setText(bookmarkText);


            }
        });
    }


    /**
     * 界面设置
     */
    private void initView() {

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        mNewsAdapter = new NewsAdapter(getActivity(), mInt);
        mRecyclerView.setAdapter(mNewsAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
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

    /**
     * 获取items内容
     */
    private void initData() {

        mType = this.getArguments().getString("type");
        type_id = this.getArguments().getInt("type_id");//news_type_id

        mInt = this.getArguments().getInt("id");
        Log.d(TAG, "initData: mType:" + mType + "ID:" + mInt);
    }

    /**
     * 下拉刷新
     *
     * @return
     */
    private Integer loadMore() {
        isLoadMore = true;
        page++;
        return 20 * (page - 1);
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
        params.put("type_id", String.valueOf(type_id));
        params.put("finder_id", String.valueOf(FinderData.FINDER_ID));

        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BlankAPI.GET_NEWS_URL,//相应标签 请求链接
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("news");

                            for (int j = 0; j < array.length(); j++) {
                                add(array.getJSONObject(j));
                            }
                            if (isLoadMore) {
                                mNewsAdapter.addMore(newsNow);
                            } else {
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
                Toast.makeText(getActivity(), "刷新出错" + error.toString(), Toast.LENGTH_SHORT).show();
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
            Integer news_id = jsonObject.getInt("news_id");
            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");
            String time = jsonObject.getString("time");
            String content_url = jsonObject.getString("content_url");
            String pic_url = jsonObject.getString("pic_url");
            String type = jsonObject.getString("type");
            String finder = jsonObject.getString("finder");
            Boolean isLike = jsonObject.getBoolean("isLike");
            Boolean isBook = jsonObject.getBoolean("isBooked");

            NewsBean data = new NewsBean();
            data.setNews_id(news_id);
            data.setTitle(title);
            data.setDesc(desc);
            data.setTime(time);
            data.setContent_url(content_url);
            data.setPic_url(pic_url);
            data.setType(type);
            data.setFinder(finder);
            data.setBook(isBook);
            data.setLike(isLike);

            newsNow.add(data);
            //mNewsAdapter.add(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
