package com.orvnge.xutils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 */
public class MyFragment extends Fragment {

    private String mText;

    public static MyFragment newInstance(String text) {
        MyFragment f = new MyFragment(text);
        return f;
    }

    public MyFragment() {
    }

    public MyFragment(String text) {
        this.mText = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment, container, false);

        ((TextView) root.findViewById(R.id.position)).setText(mText);

        return root;
    }

}