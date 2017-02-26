package orvnge.wwnje.com.fucknews.utils;

/**
 * Created by wwnje on 2016/9/4.
 * 存储php端信息
 */

public class BlankAPI {
    /**
     * 用户操作
     */
    public static  String Register_Url = "http://www.wwnje.com/FakeNews/Android_Register.php";//帐号注册
    public static  String Login_Url = "http://www.wwnje.com/FakeNews/Android_Login.php";//帐号注册
    public static  String SHARE_NEWS = "http://www.wwnje.com/FakeNews/Android_Share_News.php";//分享文章
    public static  String ADD_BOOKMARK = "http://www.wwnje.com/FakeNews/AndroidFinderAddBookmark.php";//添加书签

    /**
     * 订阅标签和新闻
     */
    public static  String ADD_ITEMS_URL = "http://www.wwnje.com/FakeNews/API_AddItems.php";//获取所有内容类别


    /**
     * 获取列表
     */
    public static  String GET_TYPE_URL = "http://www.wwnje.com/FakeNews/API_GetType.php";//获取所有内容类别
    public static  String GET_TAGS_URL = "http://www.wwnje.com/FakeNews/API_GetTags.php";//获取所有标签
    public static  String GET_MY_TAGS_URL = "http://www.wwnje.com/FakeNews/API_GetMyTags.php";//获取订阅标签
    public static  String GET_BOOKMARK_URL = "http://www.wwnje.com/FakeNews/API_GetBookMark.php";//获取所有书签

    /**
     * 获取新闻内容
     */
    public static  String GET_NEWS_URL = "http://www.wwnje.com/FakeNews/getNewsJSON.php";
    public static  String GET_ARTS_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Arts.php";
    public static  String GET_BOOK_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Book.php";
    public static  String GET_CODE_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Code.php";
    public static  String GET_DESIGN_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Design.php";
    public static  String GET_GAME_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Game.php";
    public static  String GET_LIFE_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Life.php";
    public static  String GET_MOVIE_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Movie.php";
    public static  String GET_TECHNOLOGY_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_Technology.php";
    public static  String GET_WORLD_URL = "http://www.wwnje.com/FakeNews/getNewsJSON_World.php";

    //知乎
    public static String BASE_URL = "https://zhuanlan.zhihu.com/api/columns/";
    public static String ZHUHU_POST_URL = "https://zhuanlan.zhihu.com/api/posts/";

    //豆瓣
    public static String DOUBAN_BOOK_URL = "https://api.douban.com/v2/book/1220562";
    public static String DOUBAN_NOTE_URL = "https://api.douban.com/v2/note/561055453\n";

}