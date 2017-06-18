package orvnge.wwnje.com.fucknews.view.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.data.FinderData;
import orvnge.wwnje.com.fucknews.data.Finder_List_Data;

public class DataActivity extends AppCompatActivity {

    @Bind(R.id.data_tv)
    TextView tv;

    @Bind(R.id.test_spinner_select_tag)
    Spinner test_spinner_select_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);
        tv.setText(FinderData.GetALLData());

//        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, Finder_List_Data.NEWS_TYPE_NAME);
//        test_spinner_select_tag.setAdapter(arrayAdapter);
    }
}
