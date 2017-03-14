package orvnge.wwnje.com.fucknews.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
import orvnge.wwnje.com.fucknews.data.Finder_List_Data;

/**
 * Created by Administrator on 2017/1/31.
 * 方法具体实现 和网络有关
 */

public class BlankNetMehod {

    private static DatabaseHelper dbHelper;
    private static final String TAG = "BlankNetMehod";


    /**
     * 登录操作
     * @param context
     * @param name
     * @param password
     * @param menuItem
     */
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

                    Toast.makeText(context, "开始初始化用户信息", Toast.LENGTH_SHORT).show();
                    GetMyTypes(context);
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

    /**
     * 注册操作
     * @param context
     * @param show_if_success
     * @param name
     * @param password
     */
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
     * @param context
     * @param items_id
     * @param subType
     * @param isAdd
     */
    public static void Subscribe(final Context context, final int items_id, final String subType, final String isAdd) {

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
                map.put("finder_id", String.valueOf(FinderData.FINDER_ID));
                map.put("items_id", String.valueOf(items_id));//后台判定是tags_id还是type_id
                map.put("subType", subType);//tags还是news
                map.put("sub", isAdd);//true订阅否则取消
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }


    /**
     * 用户加入书签或者喜欢
     * @param context
     * @param news_id
     * @param subType
     * @param isAdd
     */
    public static void NewsClick(final Context context, final int news_id, final String subType, final String isAdd) {

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.ADD_NEWS_ITEM_URL;

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
                map.put("finder_id", String.valueOf(FinderData.FINDER_ID));
                map.put("news_id", String.valueOf(news_id));//后台判定是tags_id还是type_id
                map.put("subType", subType);//tags还是news
                map.put("sub", isAdd);//true订阅否则取消
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }

    /**
     * 分享内容
     */
    public static void Share_NEWS(final Context context, final String _news_tag, final String _news_title, final String _news_desc, final String _news_link, final String _news_img_link) {

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.SHARE_NEWS;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //成功时
                String tip = response.toString();
                if(tip.equals("200")){
                    Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
                }
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
                map.put("finder_id", String.valueOf(FinderData.FINDER_ID));
                map.put("tag", _news_tag);
                map.put("title", _news_title);
                map.put("desc", _news_desc);
                map.put("news_link", _news_link);

                //TODO 有问题 图片URL要判断
//                if(_news_link != null){
//                    map.put("news_img_link", _news_img_link);
//                }
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }

    /**
     * 获取我的订阅数据更新版本号
     * @param context
     */
    public static void GetMyTags(final Context context, int offset, int limit) {//传递进来
        Map<String, String> params = new HashMap<String, String>();

        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        params.put("finder_id", String.valueOf(FinderData.FINDER_ID));
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

    /**
     * 获取我的新闻订阅标签
     * @param context
     */
    public static void GetMyTypes(final Context context) {//传递进来
        Map<String, String> params = new HashMap<String, String>();

        params.put("finder_id", String.valueOf(FinderData.FINDER_ID));

        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BlankAPI.GET_MY_NEWS_TYPE_URL,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("myTypes");
                            for (int j = 0; j < array.length(); j++) {
                                int type_id = Integer.parseInt(array.getJSONObject(j).getString("type_id"));
                                String type_name = array.getJSONObject(j).getString("type_name");
                                //TODO 保存到数据库
                                Finder_List_Data.ADD_ITEM(type_name, type_id);

                                /*   SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                //开始组装第一条数据
                                values.put("type_name", type_name);
                                values.put("type_id", type_id);
                                db.insert(DatabaseHelper.DB_TABLE_NEWSTYPE_LOCAL, null, values);//插入第一条数据
                                values.clear();*/
                                Toast.makeText(context, "tags_id: " + type_id + "版本" + type_name, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "获取新闻订阅标签出错:" + error.toString());
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
