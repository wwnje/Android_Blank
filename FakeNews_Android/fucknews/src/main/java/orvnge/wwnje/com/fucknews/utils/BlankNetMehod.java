package orvnge.wwnje.com.fucknews.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
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

import orvnge.wwnje.com.fucknews.data.CheckString;
import orvnge.wwnje.com.fucknews.data.FinderData;
import orvnge.wwnje.com.fucknews.data.Finder_List_Data;
import orvnge.wwnje.com.fucknews.data.SPData;
import orvnge.wwnje.com.fucknews.view.Activity.BlankNewsTypeActivity;
import orvnge.wwnje.com.fucknews.view.Activity.FirstChooseTypeActivity;
import orvnge.wwnje.com.fucknews.view.Activity.NewTagsActivity;

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

                    //初始化我订阅的标签
                    BlankNetMehod.GetMyTags(context, 0, 1000);
                    BlankNetMehod.GetMyTypes(context);//网络获取订阅数据
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
     * @param name
     * @param password
     */
    public static void Register(final Context context, final String name, final String password) {
        //判断注册信息是否正确
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.Register_Url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Register onResponse: " + response.toString());
                String finder_id = response.toString();

                //保存信息
                FinderData.SetLoginData(context, name, password, Integer.parseInt(finder_id));

                //注册完进入选择类型界面
                Intent intent = new Intent(context, FirstChooseTypeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
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

        if(!FinderData.isLogin){
            Toast.makeText(context, "Sorry, u don;t Login in...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.ADD_ITEMS_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
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
     * 1-：用户操作
     * 用户加入书签或者喜欢
     */
    public static void NewsClick_LIKE_OR_BOOKMARK(final Context context, final int news_id, final int tags_id, final String subType, final String isAdd) {

        if(!FinderData.isLogin){
            Toast.makeText(context, "Sorry, u don;t Login in...", Toast.LENGTH_SHORT).show();
            return;
        }


        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = BlankAPI.ADD_NEWS_ITEM_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
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
                map.put("news_id", String.valueOf(news_id));
                map.put("tags_id", String.valueOf(tags_id));//
                map.put("subType", subType);//tags还是news
                map.put("sub", isAdd);//true订阅否则取消
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }


    /**
     * 1-0: 用户操作
     * 分享内容
     * 新建或者查看是否有此标签
     * @param context
     */
    public static void CHECK_TAGS(final Context context, final String tags_name) {

        if(!FinderData.isLogin){
            Toast.makeText(context, "Sorry, u don;t Login in...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BlankAPI.URL_CHECK_OR_NEW_TAGS;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String r = response.toString();

                SPUtils.setParam(SPData.ss_news_name, context, SPData.news_tags_name, tags_name);

                if(r.equals(CheckString.NOT_FOUND_404)){

                    //没有进行添加
//                    Toast.makeText(context, CheckString.Share_6 + tags_name, Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, NewTagsActivity.class));

                }else{
                    //存在就直接分享和订阅
//                    Toast.makeText(context, CheckString.Share_5, Toast.LENGTH_SHORT).show();

                    Subscribe(context, Integer.parseInt(r), "tags", "true");
                    Share_NEWS(context);
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
                map.put("item_name", tags_name);//上传输入的tags名字 检测
                map.put("isCheck", "true");//先进行检测

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * 1-1:用户操作
     * 是否添加标签成功
     * 新建或者查看是否有此标签
     */
    public static boolean isAddTagYes;

    public static boolean ADD_TAGS(final Context context,final String type_id){

        if(!FinderData.isLogin){
            Toast.makeText(context, "Sorry, u don;t Login in...", Toast.LENGTH_SHORT).show();
            return false;
        }

        final String _news_tag = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_tags_name,
                "");

        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = BlankAPI.URL_CHECK_OR_NEW_TAGS;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String r = response.toString();

                if(r.equals(CheckString.INSERT_ERROR_201)){
//                    Toast.makeText(context, "插入失败", Toast.LENGTH_SHORT).show();
                    isAddTagYes = false;
                }else{
//                    Toast.makeText(context, "插入成功, 准备直接上传", Toast.LENGTH_SHORT).show();
                    isAddTagYes = true;
                    //顺便直接订阅了
                    Subscribe(context, Integer.parseInt(r), "tags", "true");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                isAddTagYes = false;
            }
        }) {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("item_name", _news_tag);//上传输入的tags名字 检测
                map.put("isCheck", "false");//先进行检测
                map.put("type_id", type_id);//news_id
                return map;
            }
        };
        requestQueue.add(stringRequest);

        return isAddTagYes;
    }
    /**
     * 1-2:分享内容
     * 分享上传
     */
    public static void Share_NEWS(final Context context) {

        final String _news_title = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_title,
                "");
        final String _news_tag = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_tags_name,
                "");
        final String _news_link = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_url,
                "");
        final String _news_img_link = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_img_url, "");
        final String _news_desc = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_desc, "");


        if(!FinderData.isLogin){
            Toast.makeText(context, "Sorry, u don;t Login in...", Toast.LENGTH_SHORT).show();
            return;
        }

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
//                    Toast.makeText(context, CheckString.Share_7, Toast.LENGTH_SHORT).show();
                    SPData.Clear_SS_NEWS_NAME(context);
                }else {
//                    Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
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
                map.put("finder_id", String.valueOf(FinderData.FINDER_ID));//用户id
                map.put("tags_name", _news_tag);//标签名字
                map.put("title", _news_title);//
                map.put("desc", _news_desc);//
                map.put("news_link", _news_link);
                map.put("news_img_link", _news_img_link);
                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }

    /**
     * 获取我的订阅Tags
     * @param context
     */
    public static void GetMyTags(final Context context, int offset, int limit) {//传递进来
        Map<String, String> params = new HashMap<String, String>();

        if(!FinderData.isLogin){
            Toast.makeText(context, "Sorry, u don;t Login in...", Toast.LENGTH_SHORT).show();
            return;
        }

        Finder_List_Data.INIT_MY_TAGS();

//        Toast.makeText(context, "正在获取我订阅的标签", Toast.LENGTH_SHORT).show();

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
                                String tags_name = array.getJSONObject(j).getString("tags_name");

                                Finder_List_Data.ADD_MY_TAGS(tags_name,tags_id);

                                //TODO 保存到数据库
                            }
//                            Toast.makeText(context, "获取订阅标签完成", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "获取订阅标签出错" + error.toString(), Toast.LENGTH_SHORT).show();
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
     * 退出时执行 下次登录获得推荐信息
     * @param context
     */
    public static void GET_SYS(final Context context) {//传递进来
        Map<String, String> params = new HashMap<String, String>();

        if(!FinderData.isLogin){
            return;
        }
        params.put("finder_id", String.valueOf(FinderData.FINDER_ID));

        JSONObject paramJsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                BlankAPI.GET_SYS,
                paramJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(context, "SYS Success", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "SYS Error" + error.toString(), Toast.LENGTH_SHORT).show();
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

    public static void GetMyTypes(final Context context) {//传递进来
        Map<String, String> params = new HashMap<String, String>();

        params.put("finder_id", String.valueOf(FinderData.FINDER_ID));
//        Toast.makeText(context, "正在获取我的订阅新闻类型", Toast.LENGTH_SHORT).show();

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
                            }
//                            Toast.makeText(context, "获取新闻订阅标签完成", Toast.LENGTH_SHORT).show();
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
