package orvnge.wwnje.com.fucknews.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wwnje on 2016/11/27.
 */

public class BlankUtils {

    private static final String TAG = "BlankUtils";
    private static ClipboardManager mClipboard = null;

    /**
     * 对网络连接状态进行判断
     * @return  true, 可用； false， 不可用
     */
    public static boolean isOpenNetwork(Context context) {
        if(context != null) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null) {
                return connManager.getActiveNetworkInfo().isAvailable();
            }
        }
        return false;
    }

    /**
     * 检测是否是可以识别的网站
     */

    public static String WhichUrl(String url){

        int index = url.indexOf(SubURL.ZHIHU);
        if(index != -1){
            //知乎
            String[] u = url.split(SubURL.ZHIHU);
            return  u[1];
        }else {
            return null;
        }
    }

    /**
     * 复制
     */

    public static void CopyForm(String content, Context context) {

        // Gets a handle to the clipboard service.
        if (null == mClipboard) {
            mClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }

        // Creates a new text clip to put on the clipboard
        ClipData clip = ClipData.newPlainText("simple text",
                content);

        // Set the clipboard's primary clip.
        mClipboard.setPrimaryClip(clip);
    }

    public static String Paste(Context context) {
        // Gets a handle to the clipboard service.
        if (null == mClipboard) {
            mClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }

        String resultString = "";

        // 检查剪贴板是否有内容
        if (!mClipboard.hasPrimaryClip()) {
            resultString = "";
//            Toast.makeText(context,
//                    "Clipboard is empty", Toast.LENGTH_SHORT).show();
        }
        else {
            ClipData clipData = mClipboard.getPrimaryClip();
            int count = clipData.getItemCount();

            for (int i = 0; i < count; ++i) {

                ClipData.Item item = clipData.getItemAt(i);
                CharSequence str = item
                        .coerceToText(context);
                Log.d(TAG, "item : " + i + ": " + str);
                resultString += str;
                //Toast.makeText(context, "内容为" + resultString, Toast.LENGTH_SHORT).show();
            }
        }
        return resultString;
    }

}


