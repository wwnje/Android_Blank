package orvnge.wwnje.com.fucknews.view.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import orvnge.wwnje.com.fucknews.utils.API;
import orvnge.wwnje.com.fucknews.utils.MyApplication;

/**
 * 标签界面
 */
public class TagsActivity extends AppCompatActivity {

    private static final String TAG = "TagsActivity";

    @Bind(R.id.tags_recycleview) RecyclerView recycleView;

    private TagsAdapter tagsAdapter;

    private List<String> mdata = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        setTitle("所有标签");
        ButterKnife.bind(this);

        initView();
        setData();
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);

        tagsAdapter = new TagsAdapter(getApplicationContext(), mdata);
        recycleView.setAdapter(tagsAdapter);

        //点击进行订阅
        tagsAdapter.setOnItemClickListener(new TagsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //此处实现onItemClick的接口
                    TextView tvRecycleViewItemText = (TextView) view.findViewById(R.id.item_tags_name);
                    //如果字体本来是黑色就变成红色，反之就变为黑色
                    if (tvRecycleViewItemText.getCurrentTextColor() == Color.BLACK)
                        tvRecycleViewItemText.setTextColor(Color.RED);
                    else
                        tvRecycleViewItemText.setTextColor(Color.BLACK);
            }
        });
    }

    /**
     * 标签请求数据
     */
    private void setData() {
//        for (int i = 0; i < 3; i++) {
//            TagsBean tagsBean = new TagsBean();
//            tagsBean.setTags_name("hello" + i);
//            tagsAdapter.add(tagsBean);
//            //mdata.add(i + "fhas");
//        }
        get(0,100);
    }

    //Volley请求
    /*
    * POST
    * offset起始点
    * limit最多条数
     */
    public void get(int offset, int limit) {//传递进来
        Toast.makeText(this, "正在发起请求", Toast.LENGTH_SHORT).show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                API.GET_TAGS_URL,
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
     *Volley获取加入
     */
    public void add(JSONObject jsonObject) {
        try {
            String tags_name = jsonObject.getString("tags_name");
            TagsBean data = new TagsBean();
            data.setTags_name(tags_name);
            tagsAdapter.add(data);
            Toast.makeText(this, "正在加载数据..." + data.getTags_name(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
