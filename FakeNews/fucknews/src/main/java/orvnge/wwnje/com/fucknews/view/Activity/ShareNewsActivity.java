package orvnge.wwnje.com.fucknews.view.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.R;
import orvnge.wwnje.com.fucknews.data.CheckString;
import orvnge.wwnje.com.fucknews.data.Finder_List_Data;
import orvnge.wwnje.com.fucknews.data.SPData;
import orvnge.wwnje.com.fucknews.utils.BlankNetMehod;
import orvnge.wwnje.com.fucknews.utils.SPUtils;


/*
  用户添加文章
 */
public class ShareNewsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ShareNewsActivity";

    @Bind(R.id.add_btn_pic) Button add_img_link;
    @Bind(R.id.add_btn_open) Button add_btn_open;
    @Bind(R.id.add_btn_shot) Button add_btn_shot;

    @Bind(R.id.add_title) TextView tv_title;
    @Bind(R.id.add_desc) TextView tv_desc;

    @Bind(R.id.add_show) TextView tv_show;
    @Bind(R.id.add_imageview) ImageView imageview;


    private ArrayAdapter<String> arrayAdapter;

    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;


    private String news_link;
    private String news_title;
    private String news_desc;
    private String news_tags;
    private String news_img_link;

    private String share_title;
    private String share_desc;
    private String share_link;
    private String share_img_link;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_news);

        setTitle("分享你喜欢的文章");
        ButterKnife.bind(this);
        context = ShareNewsActivity.this;

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action)&&type!=null){
            if ("text/plain".equals(type)){
                dealTextMessage(intent);
            }else if(type.startsWith("image/")){
                dealPicStream(intent);
            }
        }else if (Intent.ACTION_SEND_MULTIPLE.equals(action)&&type!=null){
            if (type.startsWith("image/")){
                dealMultiplePicStream(intent);
            }
        }
        initData();
        initView();
    }

    private void initData() {
        news_title = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_title,
                "");
        news_link = (String) SPUtils.getParam(
                SPData.ss_news_name,
                context,
                SPData.news_url,
                "");
        news_img_link = (String) SPUtils.getParam(
                SPData.ss_news_name, context, SPData.news_img_url, "");
        news_desc = (String) SPUtils.getParam(
                SPData.ss_news_name, context, SPData.news_desc, "");
    }

    void dealTextMessage(Intent intent){
        share_desc = intent.getStringExtra(Intent.EXTRA_TEXT);
        share_title = intent.getStringExtra(Intent.EXTRA_TITLE);
    }

    void dealPicStream(Intent intent){
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        Log.d(TAG, "dealPicStream: " + uri.toString());
    }

    void dealMultiplePicStream(Intent intent){
        ArrayList<Uri> arrayList = intent.getParcelableArrayListExtra(intent.EXTRA_STREAM);
    }

    private void initView() {

        add_btn_shot.setOnClickListener(this);
        add_btn_open.setOnClickListener(this);
        imageview.setOnClickListener(this);

        tv_title.setOnClickListener(this);
        tv_desc.setOnClickListener(this);
        add_img_link.setOnClickListener(this);

        tv_title.setText(news_title);
        tv_desc.setText(news_desc);

        tv_show.setText(news_link);
    }

    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/orvnge.wwnje.com.fucknews");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        File img = new File(tmpDir.getAbsolutePath() + "avater.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //content转为file
    private Uri convertUri(Uri uri) throws FileNotFoundException {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return saveBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //图像裁剪
    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");//数据和类型
        intent.putExtra("crop", "true");//设置显示可裁剪
        intent.putExtra("aspectX", 2);//裁剪比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);//最终宽高
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            //判断是否取消
            if (data == null) {
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);//转换
                    startImageZoom(uri);//必须是file类型的
                    //imageview.setImageBitmap(bm);
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            Uri uri;
            uri = data.getData();

            Uri fileUri = null;
            try {
                fileUri = convertUri(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            startImageZoom(fileUri);
            //Toast.makeText(ShareNewsActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
        } else if (requestCode == CROP_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            Bitmap bm = extras.getParcelable("data");
            imageview.setImageBitmap(bm);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_title:
                final View View_Title = getLayoutInflater().inflate(R.layout.dialog_add_news, null);
                final EditText edit_title = (EditText) View_Title.findViewById(R.id.add_news_dialog_edit);

                edit_title.setText(news_title);

                new AlertDialog.Builder(this).setTitle("标题")
                        .setView(View_Title)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                news_title = edit_title.getText().toString();

                                SPUtils.setParam(SPData.ss_news_name, context, SPData.news_title, news_title);

                                tv_title.setText(news_title);
                                Toast.makeText(ShareNewsActivity.this, "添加了标题" + news_title, Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;

            case R.id.add_desc:
                final View View_Desc = getLayoutInflater().inflate(R.layout.dialog_add_news, null);
                final EditText edit_desc = (EditText) View_Desc.findViewById(R.id.add_news_dialog_edit);

                edit_desc.setText(news_desc);

                new AlertDialog.Builder(this).setTitle("desc")
                        .setView(View_Desc)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                news_desc =  edit_desc.getText().toString();
                                SPUtils.setParam(SPData.ss_news_name, context,SPData.news_desc, news_desc);

                                tv_desc.setText(news_desc);
                                Toast.makeText(ShareNewsActivity.this, "添加描述" + news_desc, Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;
            case R.id.add_btn_pic://img link
                final View View_Publish2 = getLayoutInflater().inflate(R.layout.dialog_add_pic_link, null);
                final EditText edit_link2 = (EditText) View_Publish2.findViewById(R.id.add_img_link);
                if(share_img_link != null){
                    edit_link2.setText(share_img_link);
                }
                new AlertDialog.Builder(this).setTitle("Add a img Link")
                        .setView(View_Publish2)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ShareNewsActivity.this, edit_link2.getText(), Toast.LENGTH_SHORT).show();
                                news_img_link = edit_link2.getText().toString();

                                SPUtils.setParam(SPData.ss_news_name, context,SPData.news_img_url, news_img_link);
                            }
                        }).show();
                break;
         /*   case R.id.add_btn_shot:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                break;
            case R.id.add_btn_open:
                Intent intent_open = new Intent(Intent.ACTION_GET_CONTENT);
                intent_open.setType("image*//*");
                startActivityForResult(intent_open, GALLERY_REQUEST_CODE);
                break;*/
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share_news_menu, menu);//声明
        return true;
    }

    /**
     * 菜单按钮上传
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_publish://Publish

                Toast.makeText(context, CheckString.Share_4, Toast.LENGTH_SHORT).show();

                final View View_Publish = getLayoutInflater().inflate(R.layout.dialog_tag, null);

                final AutoCompleteTextView auto = (AutoCompleteTextView) View_Publish.findViewById(R.id.auto);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Finder_List_Data.NEWS_MY_TAGS);

                auto.setAdapter(adapter);
                auto.setThreshold(1);

                new AlertDialog.Builder(this).setTitle("Select a Tag")
                        .setView(View_Publish)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String news_tags = auto.getText().toString();
                                SPUtils.setParam(SPData.ss_news_name, context, SPData.news_tags_name, news_tags);

                                if(news_tags.isEmpty()){
                                    Toast.makeText(context, "新建tags不能为空", Toast.LENGTH_SHORT).show();
                                }else{
                                    BlankNetMehod.CHECK_TAGS(context, news_tags);
                                }
                            }
                        }).show();

                break;
            default:
                //对没有处理的事件，交给父类来处理
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
