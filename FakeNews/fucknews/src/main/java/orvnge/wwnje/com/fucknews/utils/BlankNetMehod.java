package orvnge.wwnje.com.fucknews.utils;

import android.content.Context;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import orvnge.wwnje.com.fucknews.data.FinderData;

/**
 * Created by Administrator on 2017/1/31.
 * 方法具体实现 和网络有关
 */

public class BlankNetMehod {

    private static final String TAG = "BlankNetMehod";
    //登录操作
    public static void Login(final Context context, final String name, final String password, final MenuItem menuItem) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.Login_Url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //成功时
                String finder_id = response.toString();//返回获得用户id
                if (finder_id.equals("0")) {
                    Toast.makeText(context, "没有此帐号", Toast.LENGTH_SHORT).show();
                } else if (finder_id.equals("-1")) {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                }
                else if (!finder_id.equals("0") && !finder_id.equals("-1")) {
                    Toast.makeText(context, "登录成功：finder:" + finder_id, Toast.LENGTH_SHORT).show();
                    //保存信息
                    FinderData.SetLoginData(context, name, password, Integer.parseInt(finder_id));
                    menuItem.setTitle(FinderData.FinderName);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //失败时
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("username", name);
                map.put("password", password);
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }

    //注册操作
    public static void Register(final Context context, final TextView show_if_success, final String name, final String password) {
        //判断注册信息是否正确
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.Register_Url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("username", name);
                map.put("password", password);
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }

    /**
     * 订阅tags 和type共用
     * news_type
     * @param context
     * @param _id
     */
    public static void Subscribe(final Context context, final int _id, final String subType, final String sub) {

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.ADD_ITEMS_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("finder_id", String.valueOf(FinderData.finder_id));
                map.put("items_id", String.valueOf(_id));
                map.put("subType", subType);//tags还是news
                map.put("sub", sub);//true订阅否则取消
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }

    //上传
    // 获取我的订阅数据更新版本号
    public static void GetMyTags(final Context context) {//传递进来
        Map<String, String> params = new HashMap<String, String>();

        params.put("limit", String.valueOf(1000));
        params.put("offset", String.valueOf(0));
        params.put("finder_id", String.valueOf(FinderData.finder_id));
        params.put("myTags_version", String.valueOf(FinderData.MyTagsVersion));

        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BlankAPI.GET_MY_TAGS_URL,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("tags");
                            for (int j = 0; j < array.length(); j++) {
                                int tags_id = Integer.parseInt(array.getJSONObject(j).getString("tags_id"));
                                int myTagsVersion = Integer.parseInt(array.getJSONObject(j).getString("myTags_version"));
                                //TODO 保存到数据库
                                Toast.makeText(context, "tags_id: " + tags_id + "版本" + myTagsVersion, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "获取出错", Toast.LENGTH_SHORT).show();
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
}
