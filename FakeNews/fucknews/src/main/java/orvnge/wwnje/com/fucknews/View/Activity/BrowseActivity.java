package orvnge.wwnje.com.fucknews.View.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import orvnge.wwnje.com.fucknews.R;

public class BrowseActivity extends AppCompatActivity {

    private WebView webView;
    String title;
    String content_url;
    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivy_browse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);

        //设置是否有返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //进度条
        final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);
        webView = (WebView) findViewById(R.id.webView);
        WebSettings wSet = webView.getSettings();

        wSet.setJavaScriptEnabled(true);//js
        wSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没有网络读取缓存

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

        content_url = getIntent().getStringExtra("content_url");
        webView.loadUrl(content_url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //重写ToolBar返回按钮的行为，防止重新打开父Activity重走生命周期方法

            case  R.id.action_share://分享
                Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                String shareText = title + "链接" + content_url;
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                startActivity(Intent.createChooser(shareIntent,getString(R.string.share_to)));
                break;
            case R.id.error_article:
                snackbar =
                        Snackbar.make(webView, R.string.what_1, Snackbar.LENGTH_LONG)//文章丢失
                                .setAction("提交反馈", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {//提交Bug
                                        try {
                                            Uri uri = Uri.parse(getString(R.string.mail_account));
                                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri); //邮箱账号
                                            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.what_1)); //主题
                                            intent.putExtra(Intent.EXTRA_TEXT,
                                                    getString(R.string.article) + title   //文章标题:
                                                            + "\n" + getString(R.string.title)    //内容
                                                            + content_url + "\n");
                                            startActivity(intent);
                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Snackbar.make(webView, R.string.no_mail_app, Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                snackbar.show();
                snackbar.setActionTextColor(getResources().getColor(R.color.green));
                break;
            case R.id.error_type:
                snackbar =
                        Snackbar.make(webView, R.string.what_2, Snackbar.LENGTH_LONG)//文章丢失
                                .setAction("提交反馈", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {//提交Bug
                                        try {
                                            Uri uri = Uri.parse(getString(R.string.mail_account));
                                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri); //邮箱账号
                                            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.what_2)); //主题
                                            intent.putExtra(Intent.EXTRA_TEXT,
                                                    getString(R.string.article) + title   //文章标题:
                                                            + "\n" + getString(R.string.title)    //内容
                                                            + content_url + "\n");
                                            startActivity(intent);
                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Snackbar.make(webView, R.string.no_mail_app, Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                snackbar.show();
                snackbar.setActionTextColor(getResources().getColor(R.color.green));
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
