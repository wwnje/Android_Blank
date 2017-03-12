package orvnge.wwnje.com.fucknews;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.orvnge.xutils.ViewPagerActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.bean.DouBanNote;
import orvnge.wwnje.com.fucknews.utils.BlankAPI;
import orvnge.wwnje.com.fucknews.utils.DatabaseHelper;
import orvnge.wwnje.com.fucknews.view.Fragment.BlankFragment;

/**
 * 测试
 * 数据库
 * jsoup
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper dbHelper;


    @Bind(R.id.testBtn)
    Button btn;

    @Bind(R.id.testBtnQuery)
    Button testBtnQuery;

    @Bind(R.id.tv)
    TextView tv;

    @Bind(R.id.testBtnCreate)
    Button testBtnCreate;

    @Bind(R.id.testBtnADD)
    Button testBtnADD;

    @Bind(R.id.testBtnDelete)
    Button testBtnDelete;

    @Bind(R.id.testBtnUpdate)
    Button testBtnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        //数据库名字，版本号
        dbHelper = new DatabaseHelper(this, "test.db", null, 2);

        testBtnCreate.setOnClickListener(this);
        testBtnADD.setOnClickListener(this);
        testBtnDelete.setOnClickListener(this);
        testBtnUpdate.setOnClickListener(this);
        testBtnQuery.setOnClickListener(this);
        btn.setOnClickListener(this);
    }


    /**
     * 点击测试
     *
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testBtnCreate://创建数据表
                dbHelper.getWritableDatabase();
                break;
            case R.id.testBtnADD://添加数据
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("type_name", "科技");
                values.put("type_id", 1);
                db.insert("NEWS_TYPE_LOCAL", null, values);//插入第一条数据
                values.clear();

                //开始组装第2条数据
                values.put("type_name", "艺术");
                values.put("type_id", 2);
                db.insert("NEWS_TYPE_LOCAL", null, values);//插入第2条数据
                break;

            case R.id.testBtnDelete://删除数据
                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                db2.delete("NEWS_TYPE_LOCAL", "type_id = ?", new String[]{
                        "1"
                });

                break;
            case R.id.testBtnQuery://查询数据
                SQLiteDatabase db4 = dbHelper.getWritableDatabase();
                //查询所有数据
                Cursor cursor = db4.query("NEWS_TYPE_LOCAL", null, null, null ,null ,null, null);
                if(cursor.moveToFirst()){//移到第一条数据
                    do{
                        //遍历cursor对象
                        String type_name = cursor.getString(cursor.getColumnIndex("type_name"));
                        int type_id = cursor.getInt(cursor.getColumnIndex("type_id"));
                        Toast.makeText(this, type_name + type_id , Toast.LENGTH_SHORT).show();
                    }while (cursor.moveToNext());
                }
                cursor.close();
                break;

            case R.id.testBtnUpdate://更新数据

                //DatabaseHelper.UpdateData(dbHelper);
                SQLiteDatabase db3 = dbHelper.getWritableDatabase();
                ContentValues values3 = new ContentValues();
                values3.put("type_id", 4);
                db3.update("NEWS_TYPE_LOCAL", values3, "type_name = ?", new String[]{
                        "艺术"
                });

                break;
            case R.id.testBtn:
                startActivity(new Intent(TestActivity.this, ViewPagerActivity.class));

//                getData();


                //                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
////                            //从一个URL加载一个Document对象。
////                            Document doc = Jsoup.connect("http://home.meishichina.com/show-top-type-recipe.html").getMyTags();
////                            //选择“美食天下”所在节点
////                            Elements elements = doc.select("div.top-bar");
////                            //打印 <a>标签里面的title
////                            Log.i("mytag",elements.select("a").attr("title"));
////
////                            //“椒麻鸡”和它对应的图片都在<div class="pic">中
////                            Elements titleAndPic = doc.select("div.pic");
////                            //使用Element.select(String selector)查找元素，使用Node.attr(String key)方法取得一个属性的值
////                            Log.i("mytag", "title:" + titleAndPic.getMyTags(1).select("a").attr("title") + "pic:" + titleAndPic.getMyTags(1).select("a").select("img").attr("data-src"));
////
////                            //原料在<p class="subcontent">中
////                            Elements burden = doc.select("p.subcontent");
////                            //对于一个元素中的文本，可以使用Element.text()方法
////                            Log.i("mytag", "burden:" + burden.getMyTags(1).text());
//                            //从一个URL加载一个Document对象。
//                            Document doc = Jsoup.connect("http://www.vice.cn/").getMyTags();
//
//                            //“椒麻鸡”和它对应的图片都在<div class="pic">中
//                            Elements titleAndPic = doc.select("div.swiper-slide");
//                            //使用Element.select(String selector)查找元素，使用Node.attr(String key)方法取得一个属性的值
//                            Log.i("mytag", "title:" + titleAndPic.getMyTags(1).select("a").attr("title") + "pic:" + titleAndPic.getMyTags(1).select("a").select("img").attr("src"));
//
//                            //原料在<p class="subcontent">中
//                            Elements burden = doc.select("p.subcontent");
//                            //对于一个元素中的文本，可以使用Element.text()方法
//                            Log.i("mytag", "burden:" + burden.getMyTags(1).text());
//
//                        }catch(Exception e) {
//                            Log.i("mytag", e.toString());
//                        }
//                    }
//                }).start();
                break;
        }
    }

    private void getData() {
        /**
         * 有提供的
         */
        StringRequest request = new StringRequest(BlankAPI.DOUBAN_NOTE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("info", response);
                dealData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //加入请求队列
        new Volley().newRequestQueue(getApplicationContext()).add(request);
    }

    private void dealData(String response) {
        Gson gson = new Gson();
        DouBanNote note = gson.fromJson(response, DouBanNote.class);
        Log.d("BOOK", "dealData: " + note.getAuthor() + note.getSummary() + note.getContent());
        tv.setText(note.getContent());
    }


}
