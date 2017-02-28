package orvnge.wwnje.com.fucknews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orvnge.xutils.MyFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContentTestragment extends Fragment {


    public ContentTestragment() {
        // Required empty public constructor
    }

    private String mText;

    public static ContentTestragment newInstance(String text) {
        ContentTestragment f = new ContentTestragment(text);
        return f;
    }


    public ContentTestragment(String text) {
        this.mText = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_content_testragment, container, false);

        ((TextView) root.findViewById(R.id.test_position)).setText(mText);

        return root;
    }

}
