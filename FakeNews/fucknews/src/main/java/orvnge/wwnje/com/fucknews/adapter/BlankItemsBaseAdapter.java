package orvnge.wwnje.com.fucknews.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import orvnge.wwnje.com.fucknews.utils.DatabaseHelper;

/**
 * Created by wwnje on 2017/2/19.
 */

public class BlankItemsBaseAdapter extends RecyclerView.Adapter<BlankItemsBaseAdapter.ViewHolder>  {

    private static final String TAG = "BlankItemsBaseAdapter";

    public List<BlankBaseItemsBean> blankBaseItemsBeanList;
    private Context context;
    private DatabaseHelper dbHelper;

    /**
     * 初始化
     * @param context
     */
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

        //数据库名字，版本号
        dbHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE_LOCAL_MESSAGE, null, DatabaseHelper.DATABASE_LOCAL_MESSAGE_VERSION);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String type_name = blankBaseItemsBeanList.get(position).getTags_name();
        int type_id = blankBaseItemsBeanList.get(position).getTags_id();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询所有数据
        Cursor cursor = db.query(DatabaseHelper.DB_TABLE_NEWSTYPE_LOCAL,//表明
                null,//查询列名
                "type_id = ?",//where约束条件
                new String[]{String.valueOf(type_id)} ,//where具体值
                null ,//group by的列
                null,//进一步约束
                null);//order by排列方式

        /**
         * 订阅的显示红色
         * 否则为黑色
         */

        holder.tags_name.setText(type_name +
                "id:" + type_id);

        if(cursor.getCount() == 0){ //如果没有这个记录显示未订阅状态
            holder.tags_name.setTextColor(Color.BLACK);
        }else{
            holder.tags_name.setTextColor(Color.RED);
        }

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
