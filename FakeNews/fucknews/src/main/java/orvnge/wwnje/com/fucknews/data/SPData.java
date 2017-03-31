package orvnge.wwnje.com.fucknews.data;

import android.content.Context;

import orvnge.wwnje.com.fucknews.utils.SPUtils;

/**
 * Created by wwnje on 2017/3/30.
 */

public class SPData {
    /**
     * 用户分享时输入存储
     * 可用于分享终端 下次继续使用
     */
    public static String ss_news_name = "finder_news_save";

    public static String news_url = "News_URL";
    public static String news_img_url = "Img_URL";
    public static String news_title = "Title";
    public static String news_desc = "Desc";
    public static String news_id = "type_id";
    public static String news_tags_name = "Tags_Name";
    public static String news_tags_id= "Tags_ID";

    public static void Clear_SS_NEWS_NAME(Context context){
        SPUtils.setParam(SPData.ss_news_name, context, SPData.news_title, "");
        SPUtils.setParam(SPData.ss_news_name, context, SPData.news_url, "");
        SPUtils.setParam(SPData.ss_news_name, context, SPData.news_img_url, "");
        SPUtils.setParam(SPData.ss_news_name, context, SPData.news_desc, "");
        SPUtils.setParam(SPData.ss_news_name, context, SPData.news_tags_name, "");
    }
}
