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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import orvnge.wwnje.com.fucknews.view.Activity.HomeActivity;

/**
 * Created by Administrator on 2017/1/31.
 */

public class EventManager {

    //登录操作
    public static void Login(final Context context, final String name, final String password, final MenuItem menuItem) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = API.Login_Url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //成功时
                String tip = response.toString();
                if (tip.equals("0")) {
                    Toast.makeText(context, "没有此帐号", Toast.LENGTH_SHORT).show();
                } else if (tip.equals("1")) {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                }
                else if (!tip.equals("0") && !tip.equals("1")) {
                    Toast.makeText(context, "登录成功：finder:" + tip, Toast.LENGTH_SHORT).show();

                    //保存信息
                    SharedPreferencesUtils.setParam("finder", context, "name", name);
                    SharedPreferencesUtils.setParam("finder", context, "password", password);
                    SharedPreferencesUtils.setParam("finder", context, "isLogin", true);
                    menuItem.setTitle((String)SharedPreferencesUtils.getParam("finder", context, "name", "Finder未登录"));
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
        String url = API.Register_Url;

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
}
