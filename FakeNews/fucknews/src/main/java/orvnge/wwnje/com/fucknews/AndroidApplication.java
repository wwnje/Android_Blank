package orvnge.wwnje.com.fucknews;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;


public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isNight = SharedPreferencesUtil.getBoolean(this, AppConstants.ISNIGHT, false);
        LogUtil.d("isNight=" + isNight);
        if (isNight) {
            //使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            //不使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}