package orvnge.wwnje.com.fucknews.data;

import android.content.Context;

import orvnge.wwnje.com.fucknews.utils.SPUtils;

/**
 * Created by wwnje on 2017/2/20.
 */

public class FinderData {
    public static boolean isLogin;
    public static int TagsVersion;//标签版本
    public static int MyTagsVersion;//订阅标签版本
    public static int BookVersion;//待阅读

    public static boolean IsFirstOpenAPP;//是否是第一次打开软件
    public static boolean IsFirstSub;//是否是第一次订阅，以后就只显示订阅了的

    /**
     * 用户信息
     * @param context
     */
    public static String FinderName;
    public static String Password;
    public static int FINDER_ID;//用户id


    //初始化数据
    public FinderData(Context context){
        isLogin = (boolean) SPUtils.getParam("finder", context, "isLogin", false);
        TagsVersion = (int) SPUtils.getParam("finder", context, "TagsVersion", 0);
        MyTagsVersion = (int) SPUtils.getParam("finder", context, "MyTagsVersion", 0);
        BookVersion = (int) SPUtils.getParam("finder", context, "book_version", 0);


        FinderName = (String) SPUtils.getParam("finder", context, "name", "Finder未登录");
        Password = (String) SPUtils.getParam("finder", context, "password", "Finder未登录");
        FINDER_ID = (int) SPUtils.getParam("finder", context, "FINDER_ID", 0);
    }

    //第一次打开设置数据
    public static void FirstSetData(){

    }

    //设置登录数据
    public static void SetLoginData(Context context, String name, String password, int id){
        SPUtils.setParam("finder", context, "name", name);
        SPUtils.setParam("finder", context, "password", password);
        SPUtils.setParam("finder", context, "isLogin", true);
        SPUtils.setParam("finder", context, "FINDER_ID", id);

        FinderName = (String) SPUtils.getParam("finder", context, "name", "Finder未登录");
        Password = (String) SPUtils.getParam("finder", context, "password", "Finder未登录");
        isLogin = (boolean) SPUtils.getParam("finder", context, "isLogin", false);
        FINDER_ID = (int) SPUtils.getParam("finder", context, "FINDER_ID", id);
    }

    public static String GetALLData(){
        return "名字：" + FinderName +
                "id:" + FINDER_ID +
                "\nisLogin:" + isLogin +
                "\nTagsVersion:" + TagsVersion +
                "\nMyTagsVersion: " + MyTagsVersion +
                "\nPassword:" + Password
                ;
    }
}
