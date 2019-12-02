package com.cl.picure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cl.picture_selector.ImagePicker;
import com.cl.picture_selector.manager.CameraConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoodsMainFigureAdapter
        .OnRecyclerViewItemClickListener {


    private TextView mTextView;
    private RecyclerView rvList;
    private GoodsMainFigureAdapter mainFigureAdapter;
    private int maxImgCount = 8;
    public static final int TAG_NEGATIVE = -1;
    List dataList = new ArrayList<String>();

    private List<String> listImg = new ArrayList<>();


    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private ArrayList<String> mImagePaths;
    private ImagePicker imagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tv_select_images);
        rvList = findViewById(R.id.rv_img);

        findViewById(R.id.bt_camera).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CameraConfig config = new CameraConfig.Builder()
                        .needCrop(false)
                        .cropSize(1, 1, 200, 200)
                        .build();
                imagePicker.getInstance().toCameraActivity(MainActivity.this, config, REQUEST_SELECT_IMAGES_CODE);

            }
        });


        rvList.setLayoutManager(new GridLayoutManager(this, 4));
        rvList.setHasFixedSize(true);
        mainFigureAdapter = new GoodsMainFigureAdapter(this, dataList, maxImgCount);
        rvList.setAdapter(mainFigureAdapter);
        mainFigureAdapter.setOnItemClickListener(this);

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
            if (mImagePaths.size() != 0) {
                listImg.addAll(mImagePaths);
                mainFigureAdapter.setImages(listImg);
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case TAG_NEGATIVE:
                imagePicker.getInstance()
                        .setTitle("标题")//设置标题
//                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .showLoading(true, "ssssssssssss")
                        .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setImagePaths(mImagePaths)//设置历史选择记录
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(MainActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                break;
            default:
                //打开预览
//                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
//                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>)
//                        adapter.getImages());
//                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
//                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
//                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
//                XLog.e(TAG, "查看图片");
                break;
        }
    }
}

