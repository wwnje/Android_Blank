package orvnge.wwnje.com.fucknews.utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
 * Created by wwnje on 2016/5/21.
 */

//全局
public class MyApplication extends Application {
    private static RequestQueue requestQueue;
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);
        super.onCreate();

        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static Context getContext() {
        return context;
    }
}
