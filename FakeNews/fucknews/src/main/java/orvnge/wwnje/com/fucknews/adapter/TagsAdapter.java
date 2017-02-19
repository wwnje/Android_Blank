package orvnge.wwnje.com.fucknews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.bean.NewsBean;
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
        holder.tags_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "hello you click me", Toast.LENGTH_SHORT).show();
            }
        });
        //holder.tags_name.setText(mData.get(position) + position);
    }

    @Override
    public int getItemCount() {
        return tagsBean.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tags_name;
        public ViewHolder(View itemView) {
            super(itemView);
            tags_name = (TextView) itemView.findViewById(R.id.item_tags_name);
        }
    }


}
