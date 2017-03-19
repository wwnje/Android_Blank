package orvnge.wwnje.com.fucknews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.bean.BlankBaseItemsBean;
import orvnge.wwnje.com.fucknews.bean.BookMarkBean;
import orvnge.wwnje.com.fucknews.utils.MyUtils;
import orvnge.wwnje.com.fucknews.view.Activity.BrowseActivity;
import orvnge.wwnje.com.fucknews.view.Activity.ZhiHuActivity;

/**
 * Created by wwnje on 2017/2/19.
 * Like && BookMark
 */

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {

    private static final String TAG = "BookMarkAdapter";

    private List<BookMarkBean> bookMarkBeenList;
    private Context context;

    public BookMarkAdapter(Context context) {
        bookMarkBeenList = new ArrayList<>();
        this.context = context;
    }

    //定义一个监听对象，用来存储监听事件
    public BookMarkAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(BookMarkAdapter.OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    //定义OnItemClickListener的接口,便于在实例化的时候实现它的点击效果
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    /**
     * 添加
     */

    public void addAll(List<BookMarkBean> bookmarks){
        bookMarkBeenList.clear();
        bookMarkBeenList.addAll(bookmarks);
    }

    public void add(BookMarkBean bookmark) {
        bookMarkBeenList.add(bookmark);

        notifyItemInserted(getItemCount() - 1);
    }

    public void clear() {
        bookMarkBeenList.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO 只出现第一条数据 改为null
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + bookMarkBeenList.get(position).getNews_title());

        holder.tags_name.setText(bookMarkBeenList.get(position).getBookmark_id() +
                bookMarkBeenList.get(position).getFinder_id() +
                bookMarkBeenList.get(position).getFinder_name() +
                bookMarkBeenList.get(position).getNews_title() +
                bookMarkBeenList.get(position).getNews_content_url() +
                bookMarkBeenList.get(position).getType()
        );

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, BrowseActivity.class);
                    intent.putExtra("content_url", bookMarkBeenList.get(position).getNews_content_url());//参数给下一个activity
                    intent.putExtra("img", bookMarkBeenList.get(position).getNews_pic_url());//参数给下一个activity
                    intent.putExtra("title", bookMarkBeenList.get(position).getNews_title());//参数给下一个activity
                    intent.putExtra("news_id", bookMarkBeenList.get(position).getNew_id());//参数给下一个activity
                    intent.putExtra("position", position);//参数给下一个activity
                    intent.putExtra(" bool_show_like", false);
                    //intent.putExtra("frag_id", frag_id);//参数给下一个activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookMarkBeenList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tags_name;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tags_name = (TextView) itemView.findViewById(R.id.item_tags_name);
            cardView = (CardView) itemView.findViewById(R.id.item_tags_cardView);

            cardView.setOnClickListener(this);
        }

        //通过接口回调来实现RecyclerView的点击事件
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //此处调用的是onItemClick方法，而这个方法是会在RecyclerAdapter被实例化的时候实现
                mOnItemClickListener.onItemClick(v, getItemCount());
            }
        }
    }
}
