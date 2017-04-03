package orvnge.wwnje.com.fucknews.view.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import orvnge.wwnje.com.fucknews.LoginActivity;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.data.FinderData;

/**
 * 启动界面
 */
public class WelcomeActivity extends Activity implements View.OnClickListener {


    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(view);
        initView();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkIfFirstIn();
            }
        });
        thread.start();
    }

    //是否第一次进入app
    private void checkIfFirstIn() {
        if(FinderData.IsNotFirstOpenAPP(WelcomeActivity.this)){
            goTo();
        }else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void goTo() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                goTo();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

    }
}