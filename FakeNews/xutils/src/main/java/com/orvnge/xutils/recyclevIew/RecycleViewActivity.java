package com.orvnge.xutils.recyclevIew;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.orvnge.xutils.R;

import java.util.ArrayList;
import java.util.List;

/*
* 学习RecycleView 模仿知乎日报*/
public class RecycleViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecycleViewBaseAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<String> mData = new ArrayList<String>();

    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        setData();
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.base_swipe_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        /**
         * 设置Spinner
         */
        mSpinner = (Spinner) findViewById(R.id.sp_main);
        List<String> mList = new ArrayList<String>();
        mList.add("LinearLayout");
        mList.add("GridLayout");

        mSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //设置为线性布局
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecycleViewActivity.this));
                        break;
                    case 1:
                        //设置为网格布局,3列
                        mRecyclerView.setLayoutManager(new GridLayoutManager(RecycleViewActivity.this, 3));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });

        mRecyclerAdapter = new RecycleViewBaseAdapter(mData);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnItemClickListener(new RecycleViewBaseAdapter.OnItemClickListener() {
            //此处实现onItemClick的接口
            @Override
            public void onItemClick(final View view, int position) {
                TextView tvRecycleViewItemText = (TextView) view.findViewById(R.id.base_swipe_item_title);
                //如果字体本来是黑色就变成红色，反之就变为黑色
                if (tvRecycleViewItemText.getCurrentTextColor() == Color.BLACK)
                    tvRecycleViewItemText.setTextColor(Color.RED);
                else
                    tvRecycleViewItemText.setTextColor(Color.BLACK);
            }
        });

        Button btnAdd = (Button) findViewById(R.id.btn_main_add);
        Button btnDel = (Button) findViewById(R.id.btn_main_del);

        /**
         * 添加
         */
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.add("Recycler");
                int position = mData.size();
                if (position > 0)
                    mRecyclerAdapter.notifyDataSetChanged();
            }
        });

        /**
         * 删除
         */
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mData.size();
                if (position > 0) {
                    mData.remove(position - 1);
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 增加数据
     */
    private void setData() {
        for (int i = 0; i < 3; i++) {
            mData.add("hello" + i);

        }
    }
}
