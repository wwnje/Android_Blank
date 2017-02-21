package orvnge.wwnje.com.fucknews.view.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.data.FinderData;

public class DataActivity extends AppCompatActivity {

    @Bind(R.id.data_tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);
        tv.setText(FinderData.GetALLData());

    }
}
