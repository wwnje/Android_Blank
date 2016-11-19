package orvnge.wwnje.com.fucknews.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.View.Activity.BrowseActivity;
import orvnge.wwnje.com.fucknews.Model.Tags;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.Utils.MyApplication;

/*
*
 */
public class HomeTagsAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {

	private List<Tags> list;
	LayoutInflater inflater;
	Context context;


	public HomeTagsAdapter(Context context){

		list = new ArrayList<>();
		inflater = LayoutInflater.from(MyApplication.getContext());
		this.context = context;
	}

	public void add(Tags Tags) {

		list.add(Tags);
		notifyItemInserted(list.size() - 1);
	}

	public void clear() {
		list.clear();
	}

	@Override
	public HomeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new HomeRecyclerViewHolder(inflater.inflate(R.layout.tags_item, parent, false));
	}

	@Override
	public void onBindViewHolder(HomeRecyclerViewHolder holder, final int position) {

		//init image
		ImageLoader imageLoader = new ImageLoader(MyApplication.getRequestQueue(), new MovieImageCache());
		holder.ivPic.setDefaultImageResId(R.drawable.img_loading);//loading图片
		holder.ivPic.setErrorImageResId(R.drawable.img_error);//错误图片
		holder.ivPic.setImageUrl(list.get(position).getPic_url(), imageLoader);

		//init others
		holder.tvType.setText(list.get(position).getType());
		holder.tvTitle.setText(list.get(position).getTitle());
		holder.tvDesc.setText(list.get(position).getDesc());
		holder.tvTime.setText(list.get(position).getTime());

		//set card view
		// 进入内容浏览器事件
		holder.cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, BrowseActivity.class);

				intent.putExtra("content_url", list.get(position).getContent_url());//参数给下一个activity
				intent.putExtra("title", list.get(position).getTitle());//参数给下一个activity

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	class MovieImageCache implements ImageLoader.ImageCache {

		private LruCache<String, Bitmap> cache;

		public MovieImageCache() {
			int maxSize = 10 * 1024 * 1024;
			cache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return cache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			cache.put(url, bitmap);
		}
	}

}

class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

	NetworkImageView ivPic;
	TextView tvTitle;
	TextView tvType;
	TextView tvDesc;
	TextView tvTime;
	CardView cardView;

	public HomeRecyclerViewHolder(View itemView) {
		super(itemView);

		tvType = (TextView) itemView.findViewById(R.id.tv_type);
		ivPic = (NetworkImageView) itemView.findViewById(R.id.ivPic);
		tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
		tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
		tvTime = (TextView) itemView.findViewById(R.id.tvTime);
		cardView = (CardView) itemView.findViewById(R.id.fragment_movie_item);
	}
}

