package orvnge.wwnje.com.fucknews.adapter;

import android.content.Context;
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
import orvnge.wwnje.com.fucknews.bean.TagsBean;

/**
 * Created by wwnje on 2017/2/19.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private static final String TAG = "TagsAdapter";
    private List<String> mData;

    private List<TagsBean> tagsBean;
    private Context context;

    public TagsAdapter(Context context, List<String> data){
        mData = data;

        tagsBean = new ArrayList<>();
        this.context = context;
    }

    public TagsAdapter(Context context){
        tagsBean = new ArrayList<>();
        this.context = context;
    }

    //定义一个监听对象，用来存储监听事件
    public TagsAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(TagsAdapter.OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    //定义OnItemClickListener的接口,便于在实例化的时候实现它的点击效果
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    /**
     * 添加
     * @param tag
     */

    public void add(TagsBean tag){
        tagsBean.add(tag);
        Log.d(TAG, "add: " + tag.getTags_name());

        notifyItemInserted(tagsBean.size() -1 );
    }

    public void clear(){
        tagsBean.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO 只出现第一条数据 改为null
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tags_name.setText(tagsBean.get(position).getTags_name());
    }

    @Override
    public int getItemCount() {
        return tagsBean.size();
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
            if(mOnItemClickListener!=null) {
                //此处调用的是onItemClick方法，而这个方法是会在RecyclerAdapter被实例化的时候实现
                mOnItemClickListener.onItemClick(v, getItemCount());
            }
        }
    }
}
