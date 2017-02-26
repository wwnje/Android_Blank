package orvnge.wwnje.com.fucknews.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.List;

/**
 * Created by wwnje on 2016/5/10.
 * 数据库操作
 * 用于保存
 * news缓存
 * 标签信息
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DB_TABLE_NEWS = "LocalNews";
    private static final String DB_TABLE_USER = "User";

    //数据表名字
    public static final String DB_TABLE_NEWSTYPE_LOCAL = "NEWS_TYPE_LOCAL";//订阅新闻类型
    public static final String DB_TABLE_NEWSTAGS_LOCAL = "TAGS_LOCAL";//订阅内容标签

    /**
     * 本地数据库信息
     */
    public static String DATABASE_LOCAL_MESSAGE = "LOCAL_MESSAGE.db";//本地新闻等内容数据库
    public static String DATABASE_LOCAL_FINDER = "LOCAL_FINDER.db";

    /**
     * 版本号
     */
    public static int DATABASE_LOCAL_MESSAGE_VERSION = 1;
    public static int DATABASE_LOCAL_FINDER_VERSION = 1;


    /**
     * 保存用户订阅newtype
     */
    public static final String CREATE_NEWS_TYPE_LOCAL= "create table " + // id title  score
            DB_TABLE_NEWSTYPE_LOCAL +
            "("
            + "id integer primary key autoincrement, "//
            + "type_name text, "
            + "type_id integer)";

    /**
     * 保存用户订阅tags
     */
    public static final String CREATE_TAGS_LOCAL= "create table " + // id title  score
            DB_TABLE_NEWSTAGS_LOCAL +
            "("
            + "id integer primary key autoincrement, "//
            + "type_id text, "
            + "tags_name text, "
            + "tags_id integer)";

    //语句
    public static final String CREATE_NEWS= "create table " + // id title  score
            DB_TABLE_NEWS +
            "("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "desc text, "
            + "content_url text unique not null, "
            + "pic_url text, "
            + "time timestamp,"
            + "type text)";

    //用户信息 用于查询是否登陆 name + 其他信息
    public static final String CREATE_SCORE = "create table " + // id title  score
            DB_TABLE_USER +
            "("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "desc text, "
            + "type text)";

    private Context mContext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    /**
     * 创建
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_NEWS_TYPE_LOCAL);//本地新闻类型表
        db.execSQL(CREATE_TAGS_LOCAL);//本地标签表

        Toast.makeText(mContext, "Create Succeed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 升级数据库 +比如增加表操作
     * version 增加就可以执行
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+ DB_TABLE_NEWSTYPE_LOCAL);
        db.execSQL("drop table if exists "+ DB_TABLE_NEWSTAGS_LOCAL);
        onCreate(db);

//        switch (oldVersion){
//            case 1:
//                db.execSQL(DB_TABLE_NEWSTYPE_LOCAL);//版本号为1就会创建防止丢失老版本 造成老用户数据丢失
//            case 2:
//                db.execSQL("alter table LocalNews add column category_id integer");//修改
//            default:
//        }

    }

    /**
     * 删除数据
     */
    public static void DeleteData(DatabaseHelper dbHelper, String DataBaseUrl, String delete_language,String delete_mes){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataBaseUrl, delete_language, new String[]{
                delete_mes
        });
    }

    /**
     * 更新数据
     */

    public static void UpdateData(DatabaseHelper dbHelper, List list ){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values =new ContentValues();
    }

}
