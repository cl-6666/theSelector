package com.cl.picure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cl.picture_selector.ImagePicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private TextView mTextView;
    private ImageView tox;


    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private ArrayList<String> mImagePaths;

    private ImagePicker imagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tv_select_images);
        tox = findViewById(R.id.img_tox);
        findViewById(R.id.bt_select_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.getInstance()
                        .setTitle("标题")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .showLoading(true, "ssssssssssss")
                        .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setImagePaths(mImagePaths)//设置历史选择记录
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(MainActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
            }
        });


        findViewById(R.id.bt_camera).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //todo 一定要先做权限判断
                imagePicker.getInstance().startCamera(MainActivity.this, 200);//设置是否展示视频setSingleType(true).startCamera();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("当前选中图片路径：\n\n");
            for (int i = 0; i < mImagePaths.size(); i++) {
                stringBuffer.append(mImagePaths.get(i) + "\n\n");
            }
            mTextView.setText(stringBuffer.toString());
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bm = (Bitmap) bundle.get("data");
            if (bm != null) {
                tox.setImageBitmap(bm);
            }
        }
    }
}

