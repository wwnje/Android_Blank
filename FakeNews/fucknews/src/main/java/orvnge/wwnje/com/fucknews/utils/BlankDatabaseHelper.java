package orvnge.wwnje.com.fucknews.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wwnje on 2016/5/10.
 * 数据库操作
 * 用于保存
 * news缓存
 * 标签信息
 */
public class BlankDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "BlankDatabaseHelper";
    private static final String DB_TABLE_NEWS = "LocalNews";
    private static final String DB_TABLE_USER = "User";

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

    public BlankDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG, "BlankDatabaseHelper: ");
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);

        Log.d(TAG, "onCreate: ");
        Toast.makeText(mContext, "Create Succeed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+ DB_TABLE_NEWS);
        onCreate(db);

    }
}
