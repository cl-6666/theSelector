package com.cl.picture_selector.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cl.picture_selector.ImagePicker;
import com.cl.picture_selector.R;
import com.cl.picture_selector.manager.CameraConfig;
import com.cl.picture_selector.manager.SelectionManager;
import com.cl.picture_selector.provider.ImagePickerProvider;

import java.io.File;
import java.util.ArrayList;



/**
 * 拍照入口
 */
public class ImageCameraActivity extends AppCompatActivity {


    /**
     * 拍照相关
     */
    private String mFilePath;
    private static final int REQUEST_CODE_CAPTURE = 0x02;//点击拍照标识
    private static final int IMAGE_CROP_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    public static void startForResult(Activity activity, CameraConfig config, int requestCode) {
        Intent intent = new Intent(activity, ImageCameraActivity.class);
        intent.putExtra("config", config);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(Fragment fragment, CameraConfig config, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), ImageCameraActivity.class);
        intent.putExtra("config", config);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(android.app.Fragment fragment, CameraConfig config, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), ImageCameraActivity.class);
        intent.putExtra("config", config);
        fragment.startActivityForResult(intent, requestCode);
    }


    private CameraConfig config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = (CameraConfig) getIntent().getSerializableExtra("config");
        if (config == null)
            return;

        camera();
    }

    private void camera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
            return;
        }

        //拍照存放路径
        File fileDir = new File(Environment.getExternalStorageDirectory(), "Pictures");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        mFilePath = fileDir.getAbsolutePath() + "/IMG_" + System.currentTimeMillis() + ".jpg";

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, ImagePickerProvider.getFileProviderName(this), new File(mFilePath));
        } else {
            uri = Uri.fromFile(new File(mFilePath));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE);
    }

    private void crop(String imagePath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("scale", true);//缩放
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);//设置为不返回缩略图
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mFilePath)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);


        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("file://" + mFilePath);
            cursor.close();
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                if (cursor != null) {
                    cursor.close();
                }
                return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private void complete() {
        //通知媒体库刷新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + mFilePath)));
        //添加到选中集合
        SelectionManager.getInstance().addImageToSelectList(mFilePath);
        ArrayList<String> list = new ArrayList<>(SelectionManager.getInstance().getSelectPaths());
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES, list);
        setResult(RESULT_OK, intent);
        SelectionManager.getInstance().removeAll();//清空选中记录
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            complete();
        } else if (requestCode == REQUEST_CODE_CAPTURE) {
            if (config.needCrop) {
                crop(mFilePath);
            } else {
                complete();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    camera();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permission_camera_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
