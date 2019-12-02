package com.cl.picture_selector;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.cl.picture_selector.activity.ImageCameraActivity;
import com.cl.picture_selector.activity.ImagePickerActivity;
import com.cl.picture_selector.manager.CameraConfig;
import com.cl.picture_selector.manager.ConfigManager;
import com.cl.picture_selector.utils.ImageLoader;

import java.util.ArrayList;

/**
 * 统一调用入口
 */
public class ImagePicker {

    public static final String EXTRA_SELECT_IMAGES = "selectItems";

    private static volatile ImagePicker mImagePicker;

    private ImagePicker() {
    }

    /**
     * 创建对象
     *
     * @return
     */
    public static ImagePicker getInstance() {
        if (mImagePicker == null) {
            synchronized (ImagePicker.class) {
                if (mImagePicker == null) {
                    mImagePicker = new ImagePicker();
                }
            }
        }
        return mImagePicker;
    }


    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public ImagePicker setTitle(String title) {
        ConfigManager.getInstance().setTitle(title);
        return mImagePicker;
    }

    /**
     * 是否支持相机
     *
     * @param showCamera
     * @return
     */
    public ImagePicker showCamera(boolean showCamera) {
        ConfigManager.getInstance().setShowCamera(showCamera);
        return mImagePicker;
    }

    /**
     * 是否展示图片
     *
     * @param showImage
     * @return
     */
    public ImagePicker showImage(boolean showImage) {
        ConfigManager.getInstance().setShowImage(showImage);
        return mImagePicker;
    }

    /**
     * 是否展示视频
     *
     * @param showVideo
     * @return
     */
    public ImagePicker showVideo(boolean showVideo) {
        ConfigManager.getInstance().setShowVideo(showVideo);
        return mImagePicker;
    }


    /**
     * 图片最大选择数
     *
     * @param maxCount
     * @return
     */
    public ImagePicker setMaxCount(int maxCount) {
        ConfigManager.getInstance().setMaxCount(maxCount);
        return mImagePicker;
    }

    /**
     * 设置单类型选择（只能选图片或者视频）
     *
     * @param isSingleType
     * @return
     */
    public ImagePicker setSingleType(boolean isSingleType) {
        ConfigManager.getInstance().setSingleType(isSingleType);
        return mImagePicker;
    }


    /**
     * 设置图片加载器
     *
     * @param imageLoader
     * @return
     */
    public ImagePicker setImageLoader(ImageLoader imageLoader) {
        ConfigManager.getInstance().setImageLoader(imageLoader);
        return mImagePicker;
    }

    /**
     * 设置图片选择历史记录
     *
     * @param imagePaths
     * @return
     */
    public ImagePicker setImagePaths(ArrayList<String> imagePaths) {
        ConfigManager.getInstance().setImagePaths(imagePaths);
        return mImagePicker;
    }


    /**
     * 是否显示加载框
     *
     * @param showLoading
     * @return
     */
    public ImagePicker showLoading(boolean showLoading) {
        ConfigManager.getInstance().setShowLoading(showLoading);
        return mImagePicker;
    }


    /**
     * 默认加载框标题
     *
     * @param showLoading
     * @param title
     * @return
     */
    public ImagePicker showLoading(boolean showLoading, String title) {
        ConfigManager.getInstance().setShowLoading(showLoading);
        ConfigManager.getInstance().setLoadingTitle(title);
        return mImagePicker;
    }


    /**
     * 跳转系统相机
     *
     * @param activity
     * @param requestCode
     */
    public void startCamera(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);   //拍照界面的隐式意图
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 启动
     *
     * @param activity
     */
    public void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 跳转系统相机
     *
     * @param source
     * @param config
     * @param reqCode
     */
    public void toCameraActivity(Object source, CameraConfig config, int reqCode) {
        if (source instanceof Activity) {
            ImageCameraActivity.startForResult((Activity) source, config, reqCode);
        } else if (source instanceof Fragment) {
            ImageCameraActivity.startForResult((Fragment) source, config, reqCode);
        } else if (source instanceof android.app.Fragment) {
            ImageCameraActivity.startForResult((Fragment) source, config, reqCode);
        }
    }


}
