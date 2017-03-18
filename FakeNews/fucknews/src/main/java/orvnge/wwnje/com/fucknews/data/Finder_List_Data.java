package orvnge.wwnje.com.fucknews.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.bean.NewsBean;
import orvnge.wwnje.com.fucknews.utils.BlankAPI;
import orvnge.wwnje.com.fucknews.utils.DatabaseHelper;
import orvnge.wwnje.com.fucknews.view.Fragment.BlankFragment;
import orvnge.wwnje.com.fucknews.view.Fragment.ContentFragment;

/**
 * Created by wwnje on 2017/3/8.
 * 用户列表 比如news_type
 */

public class Finder_List_Data {
    private static final String TAG = "Finder_List_Data";
    private static DatabaseHelper dbHelper;

    /**
     * 我订阅的类型名字和Fragment
     * TODOID还没做
     */
    public static List<String> NEWS_TYPE_NAME = new ArrayList<>();
    public static ArrayList<ContentFragment> Fragments = new ArrayList<>();
    public static List<Integer> NEWS_TYPE_ID = new ArrayList<>();


    /**
     * 我的新闻列表
     */
    //public  static List<NewsBean> newsBeen;


    /**
     * 我订阅的TAGS
     */
    public static List<String> NEWS_MY_TAGS = new ArrayList<>();
    public static List<Integer> NEWS_MY_TAGS_ID = new ArrayList<>();


    /**
     * 用户登陆后获取加入新闻订阅列表
     */
    public static void ADD_ITEM(String type_name, int type_id){
        NEWS_TYPE_ID.add(type_id);
        BlankFragment.AddNewItem(type_name);
    }

    /**
     * 用户登陆后获取加入TAGS订阅列表
     */

    public static void INIT_MY_TAGS(){
        NEWS_MY_TAGS = new ArrayList<>();
        NEWS_MY_TAGS_ID = new ArrayList<>();
    }

    public static void ADD_MY_TAGS(String tags_name, int tags_id){
        NEWS_MY_TAGS_ID.add(tags_id);
        NEWS_MY_TAGS.add(tags_name);
    }


    /**
     * TODO
     * 本地加载
     * @param context
     */
    public static void Finder_List_Get(Context context){
        NEWS_TYPE_NAME = new ArrayList<>();
        NEWS_TYPE_ID = new ArrayList<>();

        dbHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_LOCAL_MESSAGE, null, DatabaseHelper.DATABASE_LOCAL_MESSAGE_VERSION);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        NEWS_TYPE_NAME.add("Blank");

        //查询所有数据
        Cursor cursor = db.query(DatabaseHelper.DB_TABLE_NEWSTYPE_LOCAL, null, null, null ,null ,null, null);
        if(cursor.moveToFirst()){//移到第一条数据
            do{
                //遍历cursor对象
                String type_name = cursor.getString(cursor.getColumnIndex("type_name"));
                int type_id = cursor.getInt(cursor.getColumnIndex("type_id"));

                NEWS_TYPE_NAME.add(type_name);
                NEWS_TYPE_ID.add(type_id);
                Log.d(TAG, "初始化获得类型名+id: " + type_name + type_id);

            }while (cursor.moveToNext());
        }
        Toast.makeText(context, "获取新闻订阅列表完成", Toast.LENGTH_SHORT).show();
        cursor.close();
    }
}
