package orvnge.wwnje.com.fucknews.View.Fragment;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.SQLite.MyDatabaseHelper;
import orvnge.wwnje.com.fucknews.Adapter.HomeTagsAdapter;
import orvnge.wwnje.com.fucknews.Model.Tags;
import orvnge.wwnje.com.fucknews.Utils.FileUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourFragment extends Fragment {

    private boolean mIsRefreshing = true;

    String mTitle;
    View view;

    private static final String TAG = "You";

    private static final String DB_NAME = "News.db";
    private static final int DB_VERSION = 1;
    private MyDatabaseHelper dbHelper;

    private RecyclerView recyclerView;
    private HomeTagsAdapter adapter;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉

    public YourFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public YourFragment(String title) {
        mTitle = title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blank, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.white));

        recyclerView = (RecyclerView) view.findViewById(R.id.lvNews);
        manager = new LinearLayoutManager(getActivity());
        adapter = new HomeTagsAdapter(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        dbHelper = new MyDatabaseHelper(getActivity(), DB_NAME, null, DB_VERSION);//版本号1
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String str = FileUtil.readString(android.os.Environment.getExternalStorageDirectory() + "/aa.txt", "utf-8");
        //定义字符串
        String[] array = new String[10];
        array = str.split("/");
        for (int i = 0;i < array.length; i++) {
            Log.d(TAG, "onCreateView: "+array[i]);
            Cursor cursor = db.query("News", null, "type = ?", new String[]{array[i]}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    //查询Book所有数据-要放在内部
                    Tags data = new Tags();
                    //遍历cursor 取出数据打印
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String desc = cursor.getString(cursor.getColumnIndex("desc"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String content_url = cursor.getString(cursor.getColumnIndex("content_url"));
                    String pic_url = cursor.getString(cursor.getColumnIndex("pic_url"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    data.setTitle(title);
                    data.setDesc(desc);
                    data.setTime(time);
                    data.setContent_url(content_url);
                    data.setPic_url(pic_url);
                    Log.d(TAG, "Book name is:"+ title);
                    adapter.add(data);
                } while (cursor.moveToNext());
//                Toast.makeText(getActivity(), "query succeed", Toast.LENGTH_SHORT).show();
            }

        }


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        // //设置刷新时动画的颜色，可以设置4个
        //swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_red, R.color.google_green, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

            return view;
        }


}

