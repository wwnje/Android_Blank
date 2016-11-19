package orvnge.wwnje.com.fakenews;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowseNewsActivity extends Activity {
	
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivy_browse_news);
		
		webView = (WebView) findViewById(R.id.webView);

		String pic_url = getIntent().getStringExtra("content_url");

		webView.loadUrl(pic_url);
		WebSettings wSet = webView.getSettings();

		wSet.setJavaScriptEnabled(true);//js
		wSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没有网络读取缓存

		webView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{ //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
	}
}
