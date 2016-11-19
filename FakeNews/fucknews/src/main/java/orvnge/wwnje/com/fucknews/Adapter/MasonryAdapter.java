package orvnge.wwnje.com.fucknews.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.View.Activity.LoveFragment;
import orvnge.wwnje.com.fucknews.Model.Product;
import orvnge.wwnje.com.fucknews.Utils.FileUtil;

/**
 * Created by wwnje on 2016/5/22.
 * 定制界面
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>{

    static boolean isFollowing = false;
    static String type = "";

    static int i,j = 0;

    static StringBuffer sBuffer = new StringBuffer();

    private List<Product> products;
    public MasonryAdapter(List<Product> list) {
        products=list;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.masonry_item, viewGroup, false);
        //获取Shap 启动时判断是否被Follow
        Boolean isFollow = LoveFragment.getShaBoolean(type, isFollowing);
        String type2 = LoveFragment.getShaString(type,type);

        Log.d("Log", "onCreateViewHolder: "+j+++isFollow+type2);

        return new MasonryView(view, isFollow, type2);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, int position) {
        //获取Shap 启动时判断是否被Follow
        Boolean isFollow = LoveFragment.getShaBoolean(type, isFollowing);
        String type2 = LoveFragment.getShaString(type,type);

        masonryView.imageView.setImageResource(products.get(position).getImg());
        masonryView.textView.setText(products.get(position).getTitle());
        if(masonryView.textView.getText().equals(type2)){
            if(isFollow == true){
                masonryView.btn_follow.setText(products.get(position).getTitle());
                masonryView.btn_follow.setEnabled(false);
                masonryView.btn_follow.setBackgroundColor(Color.red(R.color.white));
            }else {
                masonryView.btn_follow.setText("Following");
            }
        }
        Log.d("Log", "onBindViewHolder: "+i++ );

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public static class MasonryView extends  RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        TextView tv_show_tags;
        Button btn_follow;

        public MasonryView(final View itemView, final Boolean isFollow, final String type2){
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.masonry_item_img );
            textView= (TextView) itemView.findViewById(R.id.masonry_item_title);
            tv_show_tags= (TextView) itemView.findViewById(R.id.masonry_item_show_tags);
            btn_follow = (Button) itemView.findViewById(R.id.btn_follow);

            Log.d("Log", "MasonryView: "+i++);

            btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_follow.getText().equals("Follow")){
                    btn_follow.setText("Following");
                    isFollowing = false;
                    type = textView.getText().toString();
                    /*LoveFragment love = new LoveFragment();
                    love.getSha(isFollowing);*/
                    LoveFragment.setShaBoolean(type, isFollowing);
                    LoveFragment.setShaString(type, type);
                }else if((btn_follow.getText().equals("Following"))){
                    btn_follow.setText("Follow");
                    btn_follow.setEnabled(false);
                    btn_follow.setBackgroundColor(Color.red(R.color.white));
                    isFollowing = true;
                    type = textView.getText().toString();
                    LoveFragment.setShaBoolean(type, isFollowing);
                    LoveFragment.setShaString(type, type);
                }
                sBuffer.append(textView.getText().toString());
                sBuffer.append("/");

                String input = sBuffer.toString();
                FileUtil.writeString(android.os.Environment.getExternalStorageDirectory() + "/aa.txt", input, "UTF-8");

            }
    });
        }

    }

}