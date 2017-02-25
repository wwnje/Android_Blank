package orvnge.wwnje.com.fucknews.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.bean.BookMarkBean;

/**
 * Created by wwnje on 2017/2/19.
 */

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {

    private static final String TAG = "BookMarkAdapter";
    private List<String> mData;

    private List<BookMarkBean> bookMarkBeen;
    private Context context;

    public BookMarkAdapter(Context context, List<String> data) {
        mData = data;

        bookMarkBeen = new ArrayList<>();
        this.context = context;
    }

    public BookMarkAdapter(Context context) {
        bookMarkBeen = new ArrayList<>();
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
     *
     * @param bookmark
     */

    public void add(BookMarkBean bookmark) {
        bookMarkBeen.add(bookmark);

        notifyItemInserted(bookMarkBeen.size() - 1);
    }

    public void clear() {
        bookMarkBeen.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO 只出现第一条数据 改为null
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tags_name.setText(bookMarkBeen.get(position).getBookmark_id() +
                bookMarkBeen.get(position).getFinder_id() +
                bookMarkBeen.get(position).getFinder_name() +
                bookMarkBeen.get(position).getNews_title() +
                bookMarkBeen.get(position).getNews_content_url() +
                bookMarkBeen.get(position).getType()
        );
    }

    @Override
    public int getItemCount() {
        return bookMarkBeen.size();
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
