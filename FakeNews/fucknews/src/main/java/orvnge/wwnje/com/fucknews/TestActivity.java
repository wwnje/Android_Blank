package orvnge.wwnje.com.fucknews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.bean.DouBanNote;
import orvnge.wwnje.com.fucknews.utils.BlankAPI;

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.testBtn)
    Button btn;

    @Bind(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                getData();
            }
        });
    }

    private void getData() {
        StringRequest request = new StringRequest(BlankAPI.DOUBAN_NOTE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("info", response);
                dealData(response);
            }
        }, new Response.ErrorListener(){
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
