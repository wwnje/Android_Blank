package orvnge.wwnje.com.fucknews.view.Activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import orvnge.wwnje.com.fucknews.R;

import static orvnge.wwnje.com.fucknews.utils.API.SHARE_NEWS;

/*
  用户添加文章
 */
public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddNewsActivity";
    @Bind(R.id.add_btn_news_link) Button add_news_link;
    @Bind(R.id.add_btn_pic) Button add_img_link;

    private Button add_btn_shot;
    private Button add_btn_open;

    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;
    private ImageView add_imageview;
    private TextView add_title;
    private TextView add_desc;

    private String news_link;
    private String news_title;
    private String news_desc = "";
    private String news_tag;
    private String news_img_link;

    private String share_title;
    private String share_desc;
    private String share_link;
    private String share_img_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        setTitle("分享你喜欢的文章");
        ButterKnife.bind(this);

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

        initView();
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

        add_btn_shot = (Button) findViewById(R.id.add_btn_shot);
        add_btn_shot.setOnClickListener(this);
        add_btn_open = (Button) findViewById(R.id.add_btn_open);
        add_btn_open.setOnClickListener(this);
        add_imageview = (ImageView) findViewById(R.id.add_imageview);
        add_imageview.setOnClickListener(this);

        add_title = (TextView) findViewById(R.id.add_title);
        add_title.setOnClickListener(this);

        add_desc = (TextView) findViewById(R.id.add_desc);
        add_desc.setOnClickListener(this);

        add_news_link.setOnClickListener(this);
        add_img_link.setOnClickListener(this);
        add_title.setText(share_desc);
        add_desc.setText(share_title);
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
                    //add_imageview.setImageBitmap(bm);
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
            //Toast.makeText(AddNewsActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
        } else if (requestCode == CROP_REQUEST_CODE) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            Bitmap bm = extras.getParcelable("data");
            add_imageview.setImageBitmap(bm);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_title:
                final View View_Title = getLayoutInflater().inflate(R.layout.dialog_add_news, null);
                final EditText edit_title = (EditText) View_Title.findViewById(R.id.add_news_dialog_edit);

                new AlertDialog.Builder(this).setTitle("标题")
                        .setView(View_Title)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                add_title.setText(edit_title.getText());
                                news_title = edit_title.getText().toString();
                            }
                        }).show();
                break;

            case R.id.add_desc:
                final View View_Desc = getLayoutInflater().inflate(R.layout.dialog_add_news, null);
                final EditText edit_desc = (EditText) View_Desc.findViewById(R.id.add_news_dialog_edit);
                new AlertDialog.Builder(this).setTitle("desc")
                        .setView(View_Desc)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                add_desc.setText(edit_desc.getText());
                                news_desc =  edit_desc.getText().toString();
                            }
                        }).show();
                break;
            case R.id.add_btn_news_link://Link
                final View View_Publish = getLayoutInflater().inflate(R.layout.dialog_add_newslink, null);
                final EditText edit_link = (EditText) View_Publish.findViewById(R.id.add_news_link);
                if(share_link != null){
                    edit_link.setText(share_link);
                }
                new AlertDialog.Builder(this).setTitle("Add a Link")
                        .setView(View_Publish)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AddNewsActivity.this, edit_link.getText(), Toast.LENGTH_SHORT).show();
                                news_link = edit_link.getText().toString();
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
                                Toast.makeText(AddNewsActivity.this, edit_link2.getText(), Toast.LENGTH_SHORT).show();
                                news_img_link = edit_link2.getText().toString();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_publish://Publish
                //Tag
                final View View_Publish = getLayoutInflater().inflate(R.layout.dialog_tag, null);
                final Spinner edit_tag = (Spinner) View_Publish.findViewById(R.id.spinner_select_tag);
                new AlertDialog.Builder(this).setTitle("Select a Tag")
                        .setView(View_Publish)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("Publish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                news_tag = edit_tag.getSelectedItem().toString();

                                if(news_title ==null || news_link == null || news_img_link ==null){
                                    Toast.makeText(AddNewsActivity.this, "输入信息不完整" + news_title + news_link + news_img_link + news_tag, Toast.LENGTH_LONG).show();
                                }else {
                                    add_news("finder", news_tag, news_title, news_desc, news_link, news_img_link);
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

    private void add_news(final String finder, final String _news_tag, final String _news_title, final String _news_desc, final String _news_link, final String _news_img_link) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = SHARE_NEWS;//文章分享

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //成功时
                String tip = response.toString();
                if(tip.equals("200")){
                    Toast.makeText(AddNewsActivity.this, "200", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddNewsActivity.this, tip, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //失败时
                if(error != null)
                Toast.makeText(AddNewsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("finder", finder);
                map.put("tag", _news_tag);
                map.put("title", _news_title);
                map.put("desc", _news_desc);
                map.put("news_link", _news_link);
                map.put("news_img_link", _news_img_link);

                return map;
            }
        };
        //把StringRequest对象加到请求队列里来
        requestQueue.add(stringRequest);
    }
}
