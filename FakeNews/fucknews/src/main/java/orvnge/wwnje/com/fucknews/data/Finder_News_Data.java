package orvnge.wwnje.com.fucknews.data;

import android.content.Context;

import orvnge.wwnje.com.fucknews.utils.SharedPreferencesUtils;

/**
 * Created by wwnje on 2017/3/8.
 * 保存未上传的内容
 */

public class Finder_News_Data {

    /**
     * 标题
     * 新闻链接
     * 图片链接
     * 描述
     * 是否上传，作为判断
     */
    public static String Title;
    public static String News_URL;
    public static String IMG_URL;
    public static String DESC;
    public static boolean IF_SHARE;

    /**
     * 系统启动 初始化之前保存数据
     */
    public static void Finder_News_NEW(Context context){
        IF_SHARE = (boolean) SharedPreferencesUtils.getParam("finder_news_save", context, "IF_SHARE", false);
        //上次的内容已经分享
        if(IF_SHARE){
            Title = "请输入标题";
            News_URL = "请输入新闻链接";
            IMG_URL = "img_url";
            DESC = "请输入描述";
        }else{
            Title = (String)SharedPreferencesUtils.getParam("finder_news_save", context, "Title", "上次没有保存标题");
            News_URL = (String)SharedPreferencesUtils.getParam("finder_news_save", context, "News_URL", "上次没有保存新闻链接");
            IMG_URL = (String)SharedPreferencesUtils.getParam("finder_news_save", context, "Img_URL", "上次没有保存链接");
            DESC = (String)SharedPreferencesUtils.getParam("finder_news_save", context, "Desc", "上次没有保存内容");
        }
    }
}
