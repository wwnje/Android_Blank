package orvnge.wwnje.com.fucknews.other;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.orvnge.xutils.MyFragment;
import com.orvnge.xutils.TextProvider;
import com.orvnge.xutils.ViewPagerActivity;

import java.util.ArrayList;

import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.utils.DatabaseHelper;
import orvnge.wwnje.com.fucknews.view.Activity.HomeActivity;
import orvnge.wwnje.com.fucknews.view.Activity.ShareNewsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerTestFragment extends Fragment   implements TextProvider {

    private Toolbar mToolbar;

    private Button mAdd;
    private Button mRemove;
    private static ViewPager mPager;
    private static MyPagerAdapter mAdapter;

    private static ArrayList<String> mEntries = new ArrayList<String>();

    public ViewPagerTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = (Toolbar) view.findViewById(R.id.test_toolbar);
        mToolbar.setTitle(R.string.app_name);

        mEntries.add("pos 1");
        mEntries.add("pos 2");


        mAdd = (Button) view.findViewById(R.id.add);
        mRemove = (Button) view.findViewById(R.id.remove);
        mPager = (ViewPager) view.findViewById(R.id.pager);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });

        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCurrentItem();
            }
        });

        mAdapter = new MyPagerAdapter(getChildFragmentManager(), this);

        mPager.setAdapter(mAdapter);
    }


    public void addNewItem() {
        mEntries.add("new item");
        mAdapter.notifyDataSetChanged();
        mAdapter.notifyChangeInPosition(1);
    }

    public static void removeCurrentItem() {
        int position = mPager.getCurrentItem();
        mEntries.remove(position);
        mAdapter.notifyDataSetChanged();
        mAdapter.notifyChangeInPosition(1);
    }

    @Override
    public String getTextForPosition(int position) {
        return mEntries.get(position);
    }
    @Override
    public int getCount() {
        return mEntries.size();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private TextProvider mProvider;
        private long baseId = 0;

        public MyPagerAdapter(FragmentManager fm, TextProvider provider) {
            super(fm);
            this.mProvider = provider;
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.newInstance(mProvider.getTextForPosition(position));
        }

        @Override
        public int getCount() {
            return mProvider.getCount();
        }


        //this is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }


        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }
    }
}
