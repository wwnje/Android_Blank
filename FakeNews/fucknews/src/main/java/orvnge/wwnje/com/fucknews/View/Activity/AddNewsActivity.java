package orvnge.wwnje.com.fucknews.View.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import orvnge.wwnje.com.fucknews.R;


/*
  用户添加文章
 */
public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button add_btn_shot;
    private Button add_btn_open;

    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;
    private ImageView add_imageview;
    private Button add_btn_title;
    private TextView add_title;
    private TextView add_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        setTitle("分享你喜欢的文章");
        initView();
    }

    private void initView() {

        add_btn_shot = (Button) findViewById(R.id.add_btn_shot);
        add_btn_shot.setOnClickListener(this);
        add_btn_open = (Button) findViewById(R.id.add_btn_open);
        add_btn_open.setOnClickListener(this);
        add_imageview = (ImageView) findViewById(R.id.add_imageview);
        add_imageview.setOnClickListener(this);
        add_btn_title = (Button) findViewById(R.id.add_btn_title);
        add_btn_title.setOnClickListener(this);

        add_title = (TextView) findViewById(R.id.add_title);
        add_title.setOnClickListener(this);

        add_desc = (TextView) findViewById(R.id.add_desc);
        add_desc.setOnClickListener(this);
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
                            }
                        }).show();
                break;

            case R.id.add_btn_shot:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                break;
            case R.id.add_btn_open:
                Intent intent_open = new Intent(Intent.ACTION_GET_CONTENT);
                intent_open.setType("image/*");
                startActivityForResult(intent_open, GALLERY_REQUEST_CODE);
                break;

        }
    }
}
