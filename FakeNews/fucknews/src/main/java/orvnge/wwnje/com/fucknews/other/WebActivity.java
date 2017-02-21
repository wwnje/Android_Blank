package orvnge.wwnje.com.fucknews.other;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import orvnge.wwnje.com.fucknews.R;

public class WebActivity extends Activity {
    private WebView webView;
    private TextView textView;
    private static final int DIALOG_KEY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webView);
        textView = (TextView) findViewById(R.id.textView);
        try {
            ProgressAsyncTask asyncTask = new ProgressAsyncTask(webView, textView);
            asyncTask.execute(10000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String test() {
        StringBuffer buffer = new StringBuffer();
        Document doc;
        try {
            doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/").get();
            Elements ListDiv = doc.getElementsByAttributeValue("class", "postTitle");
            for (Element element : ListDiv) {
                Elements links = element.getElementsByTag("a");
                for (Element link : links) {
                    String linkHref = link.attr("href");
                    String linkText = link.text().trim();
                    buffer.append("linkHref==" + linkHref);
                    buffer.append("linkText==" + linkText);

                    System.out.println(linkHref);
                    System.out.println(linkText);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer.toString();

    }

    // 弹出"查看"对话框
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_KEY: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("获取数据中  请稍候...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                return dialog;
            }
        }
        return null;
    }

    public static String readHtml(String myurl) {
        StringBuffer sb = new StringBuffer("");
        URL url;
        try {
            url = new URL(myurl);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "gbk"));
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    class ProgressAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WebView webView;
        private TextView textView;

        public ProgressAsyncTask(WebView webView, TextView textView) {
            super();
            this.webView = webView;
            this.textView = textView;
        }

        /**
         * 这里的Integer参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
         * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
         * 但是可以调用publish Progress方法触发onProgressUpdate对UI进行操作
         */
        @Override
        protected String doInBackground(Integer... params) {
            String str = null;
            Document doc = null;
            try {
//                String url ="http://www.cnblogs.com/zyw-205520/p/3355681.html";
//
//                doc= Jsoup.parse(new URL(url).openStream(),"utf-8", url);
//                //doc = Jsoup.parse(readHtml(url));
//                //doc=Jsoup.connect(url).getMyTags();
//                str=doc.body().toString();
                doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/archive/2012/12/20/2826402.html").get();
                Elements ListDiv = doc.getElementsByAttributeValue("class", "postBody");
                for (Element element : ListDiv) {
                    str = element.html();
                    System.out.println(element.html());
                }
                Log.d("doInBackground", str.toString());
                System.out.println(str);
                //你可以试试GBK或UTF-8
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str.toString();
            //return test();
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            webView.loadData(result, "text/html;charset=utf-8", null);
            textView.setText(result);
            removeDialog(DIALOG_KEY);
        }

        // 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            showDialog(DIALOG_KEY);
        }

        /**
         * 这里的Intege参数对应AsyncTask中的第二个参数
         * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
         * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

}