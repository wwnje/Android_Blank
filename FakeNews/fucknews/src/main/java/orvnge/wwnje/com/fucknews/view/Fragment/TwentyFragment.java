package orvnge.wwnje.com.fucknews.view.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.adapter.TwentyViewAdapter;
import orvnge.wwnje.com.fucknews.bean.TwentyData;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwentyFragment extends BaseFragment {

    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private TwentyViewAdapter twentyViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twenty, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.twenty_recycleview);
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        twentyViewAdapter = new TwentyViewAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(twentyViewAdapter);

        TwentyData data =new TwentyData();
        for (int i = 0;i < 10; i++) {
            data.setDesc(i + "data");
            twentyViewAdapter.add(data);
        }
    }

}
