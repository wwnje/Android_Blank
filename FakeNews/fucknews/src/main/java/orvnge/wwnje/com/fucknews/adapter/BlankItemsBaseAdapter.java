package orvnge.wwnje.com.fucknews.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.bean.BlankBaseItemsBean;

/**
 * Created by wwnje on 2017/2/19.
 */

public class BlankItemsBaseAdapter extends RecyclerView.Adapter<BlankItemsBaseAdapter.ViewHolder>  {

    private static final String TAG = "BlankItemsBaseAdapter";

    public List<BlankBaseItemsBean> blankBaseItemsBeanList;
    private Context context;

    public BlankItemsBaseAdapter(Context context) {
        blankBaseItemsBeanList = new ArrayList<>();
        this.context = context;
    }


    //定义一个监听对象，用来存储监听事件
    public BlankItemsBaseAdapter.OnItemClickListener mOnItemClickListener;
    public BlankItemsBaseAdapter.OnLongItemClickListener mOnLongItemClickListener;


    public void setOnItemClickListener(BlankItemsBaseAdapter.OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }
    public void setOnLongItemClickListener(BlankItemsBaseAdapter.OnLongItemClickListener itemLongClickListener) {
        mOnLongItemClickListener = itemLongClickListener;
    }



    //定义OnItemClickListener的接口,便于在实例化的时候实现它的点击效果
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //长按
    public interface OnLongItemClickListener {
        void onLongItemClick(View view, int position);
    }

    /**
     * 添加
     *
     * @param tag
     */

    public void add(BlankBaseItemsBean tag) {
        blankBaseItemsBeanList.add(tag);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addAll(List<BlankBaseItemsBean> tags){
        blankBaseItemsBeanList.clear();
        blankBaseItemsBeanList.addAll(tags);
    }

    public void clear() {
        blankBaseItemsBeanList.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO 只出现第一条数据 改为null
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tags_name.setText(blankBaseItemsBeanList.get(position).getTags_name() + "id:" + blankBaseItemsBeanList.get(position).getTags_id());

        //通过接口回调来实现RecyclerView的点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, pos);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnLongItemClickListener.onLongItemClick(holder.itemView, pos);
                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return blankBaseItemsBeanList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tags_name;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tags_name = (TextView) itemView.findViewById(R.id.item_tags_name);
            cardView = (CardView) itemView.findViewById(R.id.item_tags_cardView);
        }
    }
}
