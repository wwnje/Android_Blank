package orvnge.wwnje.com.fakenews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
	工具类
 */
public class HttpUtils {
	
	public static void getNewsJSON(final String url, final Handler handler){
		
		new Thread(new Runnable() {	//访问网络开启线程
			@Override
			public void run() {
				HttpURLConnection conn;
				InputStream is;
				try {
					conn = (HttpURLConnection) new URL(url).openConnection();//获得conn对象
					conn.setRequestMethod("GET");

					is = conn.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					String line = "";
					StringBuilder result = new StringBuilder();//字符串拼接
					while ( (line = reader.readLine()) != null ){//读取数据
						result.append(line);//追加
					}
					Message msg = new Message();
					msg.obj = result.toString();//jason
					handler.sendMessage(msg);//传递
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}).start();
	}
	
	public static void setPicBitmap(final ImageView ivPic, final String pic_url){//图片
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpURLConnection conn = (HttpURLConnection) new URL(pic_url).openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					ivPic.setImageBitmap(bitmap);//设置图片
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}).start();
	}
	
}












