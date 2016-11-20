package orvnge.wwnje.com.fucknews.view.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.adapter.MasonryAdapter;
import orvnge.wwnje.com.fucknews.model.Product;
import orvnge.wwnje.com.fucknews.utils.SharedPreferencesUtils;
import orvnge.wwnje.com.fucknews.utils.SpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoveFragment extends Fragment {

    LinearLayoutManager manager;
    private List<Product> productList;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    private static Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplication().getApplicationContext();
    }

    public static void setShaBoolean(String FILE_NAME, Object object ){
       SharedPreferencesUtils.setParam(FILE_NAME, mContext,"Boolean", object);
        //Toast.makeText(mContext, "ddd", Toast.LENGTH_SHORT).show();
        Boolean ii = (Boolean) SharedPreferencesUtils.getParam(FILE_NAME, mContext,"Boolean", false);
//        Toast.makeText(mContext, "isFollowing" +ii, Toast.LENGTH_SHORT).show();
    }
    public static Boolean getShaBoolean(String FILE_NAME, Object object ){
        Boolean ii = (Boolean) SharedPreferencesUtils.getParam(FILE_NAME, mContext,"Boolean", false);
//        Toast.makeText(mContext, "isFollowing" +ii, Toast.LENGTH_SHORT).show();
        return ii;
    }


    public static void setShaString(String FILE_NAME, Object object ){
       SharedPreferencesUtils.setParam(FILE_NAME, mContext,"String", object);
        //Toast.makeText(mContext, "ddd", Toast.LENGTH_SHORT).show();
        String ii = (String) SharedPreferencesUtils.getParam(FILE_NAME, mContext,"String", object);
//        Toast.makeText(mContext, ii, Toast.LENGTH_SHORT).show();
    }
    public static String getShaString(String FILE_NAME, Object object ){
        String ii = (String) SharedPreferencesUtils.getParam(FILE_NAME, mContext,"String", object);
//        Toast.makeText(mContext, "isFollowing" +ii, Toast.LENGTH_SHORT).show();
        return ii;
    }

    public LoveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_love, container, false);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recycler);
        // use a linear layout manager
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(manager);
        //设置adapter
        initData();

        MasonryAdapter adapter=new MasonryAdapter(productList);
        recyclerView.setAdapter(adapter);

        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        //设置间隔，我们自定义了一个SpacesItemDecoration
        recyclerView.addItemDecoration(decoration);
        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    private void initData() {
        productList=new ArrayList<Product>();
        Product p1=new Product(R.drawable.ic_menu_camera,"Design");
        productList.add(p1);
        Product p2=new Product(R.drawable.ic_menu_gallery,"Food");
        productList.add(p2);
        Product p3=new Product(R.mipmap.ic_launcher,"Social");
        productList.add(p3);
        Product p4=new Product(R.drawable.ic_menu_manage,"Photography");
        productList.add(p4);
        Product p5=new Product(R.drawable.ic_menu_send,"Android");
        productList.add(p5);
        Product p6=new Product(R.mipmap.ic_launcher,"Game");
        productList.add(p6);
        Product p7=new Product(R.drawable.ic_menu_camera,"Technology");
        productList.add(p7);
        Product p8=new Product(R.mipmap.ic_launcher,"我是照片8");
        productList.add(p8);
        Product p9=new Product(R.drawable.ic_menu_slideshow,"Science");
        productList.add(p9);
        Product p10=new Product(R.mipmap.ic_launcher,"Love");
        productList.add(p10);
        Product p11=new Product(R.mipmap.ic_launcher,"Life");
        productList.add(p11);

    }

}
