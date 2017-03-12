package orvnge.wwnje.com.fucknews.ibean;

import android.view.View;

/**
 * Created by wwnje on 2017/3/12.
 */

public class IAdapeter {

    //定义OnItemClickListener的接口,便于在实例化的时候实现它的点击效果
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //长按
    public interface OnLongItemClickListener {
        void onLongItemClick(View view, int position);
    }


}
