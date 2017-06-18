package orvnge.wwnje.com.fucknews.data;

import android.content.Context;

import orvnge.wwnje.com.fucknews.utils.SPUtils;
import orvnge.wwnje.com.fucknews.view.Fragment.BlankFragment;

import static orvnge.wwnje.com.fucknews.data.Finder_List_Data.NEWS_TYPE_NAME;

/**
 * Created by wwnje on 2017/2/20.
 */

public class FinderData {

    public static boolean isLogin;
    public static int TagsVersion;//标签版本
    public static int MyTagsVersion;//订阅标签版本
    public static int BookVersion;//待阅读

    public static boolean IsFirstSub;//是否是第一次订阅，以后就只显示订阅了的

    /**
     * 用户信息
     * @param context
     */
    public static String FinderName;
    public static String Password;
    public static int FINDER_ID;//用户id

    //是否不是第一次打开app 用于登录项的提示
    public static boolean IsNotFirstOpenAPP(Context context){
        return  (boolean) SPUtils.getParam("finder", context, "isNotFirstOpenApp", false);
    }

    /**
     *  初始化数据
     *  1.用户是否第一次打开app
     *  2.用户是否登录
     *  3.用户名字
     *  4.用户的ID
     */
    public FinderData(Context context){

        isLogin = (boolean) SPUtils.getParam("finder", context, "isLogin", false);

        FinderName = (String) SPUtils.getParam("finder", context, "name", "Finder未登录");
        FINDER_ID = (int) SPUtils.getParam("finder", context, "FINDER_ID", 0);
    }

    //第一次打开设置数据
    public static void Finder_Login_Out(Context context){
        SPUtils.setParam("finder", context, "name", "");
        SPUtils.setParam("finder", context, "isLogin", false);
        SPUtils.setParam("finder", context, "FINDER_ID", 0);

        FinderName = "";
        isLogin = false;
        FINDER_ID = -1;

        for (int i= 0;i < NEWS_TYPE_NAME.size(); i++){
            String check = NEWS_TYPE_NAME.get(i);
            if(check.equals("Blank") ||check.equals("今日推荐") )
            {
                continue;
            }else {
                BlankFragment.DeleteItem(i);
                break;
            }
        }
    }

    /**
     * 设置登录数据
     *
     */
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
                "\nMyTagsVersion: " + MyTagsVersion
                ;
    }
}
