package orvnge.wwnje.com.fucknews.View.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import orvnge.wwnje.com.fucknews.Adapter.HomeTagsAdapter;
import orvnge.wwnje.com.fucknews.Model.Tags;
import orvnge.wwnje.com.fucknews.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagsFragment extends Fragment {

    private static final String TAG = "TagsFragment";
    private RecyclerView recyclerView;
    private HomeTagsAdapter adapter;
    private LinearLayoutManager manager;

    /*服务器地址*/
    public static final String GET_NEWS_URL = "http://www.wwnje.com/FakeNews/getNewsJSON.php";

    public TagsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.lvNews);
        manager = new LinearLayoutManager(getActivity());
        adapter = new HomeTagsAdapter(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        get();
        return view;
    }

    //Volley请求
    public void get() {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_NEWS_URL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                adapter.notifyDataSetChanged();
//                try {
//                    JSONArray array = response.getJSONArray("user");
//                    for (int j = 0; j < array.length(); j++) {
//                        add(array.getJSONObject(j));
//                        Log.d("Log", "onResponse: "+array.getJSONObject(j).toString());
//                        Log.d("Log", "1: ");
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Log.d("Log", "2: ");
//
////                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), "刷新出错", Toast.LENGTH_SHORT).show();
//            }
//        });
//        MyApplication.getRequestQueue().add(jsonObjectRequest);
    }


    private void add(JSONObject jsonObject) {
        Log.d("Log", "3: ");
        Tags data = new Tags();

        try {
            // JSONArray jsonArray = new JSONArray(jsonObject);

            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");
            String time = jsonObject.getString("time");
            String content_url = jsonObject.getString("content_url");
            String pic_url = jsonObject.getString("pic_url");
            data.setTitle(title);
            data.setDesc(desc);
            data.setTime(time);
            data.setContent_url(content_url);
            data.setPic_url(pic_url);
            Log.d("Log", "4: ");

            adapter.add(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}