package orvnge.wwnje.com.fucknews.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by wwnje on 2016/11/27.
 */

public class MyUtils {
    /**
     * 对网络连接状态进行判断
     * @return  true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        if(context != null) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null) {
                return connManager.getActiveNetworkInfo().isAvailable();
            }
        }
        return false;
    }

    /**
     * 检测是否是可以识别的网站
     */

    public static String WhichUrl(String url){

        int index = url.indexOf(SubURL.ZHIHU);
        if(index != -1){
            //知乎
            String[] u = url.split(SubURL.ZHIHU);
            return  u[1];
        }else {
            return null;
        }
    }

}


