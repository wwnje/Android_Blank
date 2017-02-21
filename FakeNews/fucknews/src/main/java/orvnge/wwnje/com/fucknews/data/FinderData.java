package orvnge.wwnje.com.fucknews.data;

import android.content.Context;

import orvnge.wwnje.com.fucknews.utils.SharedPreferencesUtils;

/**
 * Created by wwnje on 2017/2/20.
 */

public class FinderData {
    public static boolean isLogin;
    public static int TagsVersion;//标签版本
    public static int MyTagsVersion;//订阅标签版本

    public static boolean IsFirstOpenAPP;//是否是第一次打开软件

    /**
     * 用户信息
     * @param context
     */
    public static String FinderName;
    public static String Password;

    //初始化数据
    public FinderData(Context context){
        isLogin = (boolean) SharedPreferencesUtils.getParam("finder", context, "isLogin", false);
        TagsVersion = (int) SharedPreferencesUtils.getParam("finder", context, "TagsVersion", 0);
        MyTagsVersion = (int) SharedPreferencesUtils.getParam("finder", context, "MyTagsVersion", 0);

        FinderName = (String)SharedPreferencesUtils.getParam("finder", context, "name", "Finder未登录");
        Password = (String)SharedPreferencesUtils.getParam("finder", context, "password", "Finder未登录");
    }

    //第一次打开设置数据
    public static void FirstSetData(){

    }

    //设置登录数据
    public static void SetLoginData(Context context, String name, String password){
        SharedPreferencesUtils.setParam("finder", context, "name", name);
        SharedPreferencesUtils.setParam("finder", context, "password", password);
        SharedPreferencesUtils.setParam("finder", context, "isLogin", true);

        FinderName = (String)SharedPreferencesUtils.getParam("finder", context, "name", "Finder未登录");
        Password = (String)SharedPreferencesUtils.getParam("finder", context, "password", "Finder未登录");
        isLogin = (boolean) SharedPreferencesUtils.getParam("finder", context, "isLogin", false);
    }

    public static String GetALLData(){
        return "名字：" + FinderName +
                "\nisLogin:" + isLogin +
                "\nTagsVersion:" + TagsVersion +
                "\nMyTagsVersion: " + MyTagsVersion +
                "\nPassword:" + Password
                ;
    }
}
