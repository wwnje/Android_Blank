package com.orvnge.xutils.recyclevIew;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.orvnge.xutils.R;

import java.util.List;


/**
 * Created by wwnje on 2017/2/18.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    private List<String> mData;

    public RecycleViewAdapter(List<String> data) {
        mData = data;
    }


    //定义一个监听对象，用来存储监听事件
    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    //定义OnItemClickListener的接口,便于在实例化的时候实现它的点击效果
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    /**
     * 创建
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View views= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_base_swipe_list,parent,false);
        return new ViewHolder(views);
    }

    /**
     * 绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //建立起ViewHolder中试图与数据的关联
        holder.newsTitle.setText(mData.get(position)+position);
    }

    /**
     * 返回Size
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView newsTitle;
        ImageView newsIcon;
        CardView cardView;

        //初始化viewHolder，此处绑定后在onBindViewHolder中可以直接使用
        public ViewHolder(View itemView) {

            super(itemView);

            newsTitle = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
            newsIcon = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

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
