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
import orvnge.wwnje.com.fucknews.bean.TwentyData;

/**
 * Created by wwnje on 2016/11/24.
 */

public class TwentyViewAdapter extends RecyclerView.Adapter<TwentyViewAdapter.ViewHolder> {

    private List<TwentyData> list;
    Context context;

    public TwentyViewAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    public void add(TwentyData twentyData) {
        list.add(twentyData);
        notifyItemInserted(list.size() - 1);
    }

    public void clear() {
        list.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.twenty_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText("xx");

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "dddd", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        CardView card;


        public ViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.twenty_card);
            tv= (TextView) itemView.findViewById(R.id.twenty_type);
        }
    }
}
