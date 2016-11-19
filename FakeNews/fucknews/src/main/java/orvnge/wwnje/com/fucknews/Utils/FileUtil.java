package orvnge.wwnje.com.fucknews.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtil {

	//创建文件夹
	public static String createIfNotExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return path;
	}

	public static void writeString(String path, String content, String charset){
		try {
			FileOutputStream fop = new FileOutputStream(path);
			// 构建FileOutputStream对象,文件不存在会自动新建

			OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
			// 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk

			writer.append(content);
			// 写入到缓冲区
			// 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入

			writer.close();
			//关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉

			fop.close();
			// 关闭输出流,释放系统资源

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static String readString(String path, String charset){
		StringBuffer sb = new StringBuffer();

		try {
			FileInputStream fip = new FileInputStream(path);
			// 构建FileInputStream对象

			InputStreamReader reader = new InputStreamReader(fip, charset);
			// 构建InputStreamReader对象,编码与写入相同

			while (reader.ready()) {
				sb.append((char) reader.read());
				// 转成char加到StringBuffer对象中
			}
			System.out.println(sb.toString());
			reader.close();
			// 关闭读取流

			fip.close();
			// 关闭输入流,释放系统资源

		} catch (Exception e) {
		}
		return sb.toString();
	}
}
