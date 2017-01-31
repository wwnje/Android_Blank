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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import orvnge.wwnje.com.fucknews.view.Activity.HomeActivity;

/**
 * Created by Administrator on 2017/1/31.
 */

public class EventManager {


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
                    Toast.makeText(context, "finder:" + tip, Toast.LENGTH_SHORT).show();
                    //保存信息
                    SharedPreferencesUtils.setParam("me", context, "name", name);
                    SharedPreferencesUtils.setParam("me", context, "password", password);
                    menuItem.setTitle((String)SharedPreferencesUtils.getParam("me", context, "name", "Finder"));
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
    public static void Register(Context context, final TextView show_if_success, final String name, final String password) {
        //判断注册信息是否正确
        // Instantiate the RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = API.Register_Url;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                show_if_success.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                show_if_success.setText("error");
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
