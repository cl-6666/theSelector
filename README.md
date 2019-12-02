### 介绍
我看了有很多人写类似这种图片选择库，都是千篇一律，我也是取其中的代码进行重构，因为项目经常用到  
效果图:  
![图1](https://img-blog.csdnimg.cn/20190709181621899.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2EyMTQwMjQ0NzU=,size_16,color_FFFFFF,t_70)  
![图2](https://img-blog.csdnimg.cn/20190709181654566.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2EyMTQwMjQ0NzU=,size_16,color_FFFFFF,t_70)  

版本更新历史：  
[![](https://jitpack.io/v/cl-6666/theSelector.svg)](https://jitpack.io/#cl-6666/theSelector)  
Version1.2.20：   
1、放出系统相机拍照入口  
2、加载框可配置

Version1.1.0：    
1、可预览各文件夹下的图片  
2、可配置是否支持相机拍照  
3、可配置选择图片模式（单选/多选）  
4、可配置选择图片数量  
5、可配置图片加载框架  

### 使用方式:
```java
1、如何在项目中引入该图片加载库：
Android x用户引入---
implementation 'com.github.cl-6666:theSelector:v1.2.21'

Android v7引入---
implementation 'com.github.cl-6666:theSelector:v1.2.20'





2、如何自定义图片加载器（不定死框架，让框架更加灵活，需要去实现ImageLoader接口即可，如果需要显示视频，优先推荐Glide加载框架，可以参考Demo实现）：
/**
 * 实现自定义图片加载
 */
public class GlideLoader implements ImageLoader {

    private RequestOptions mOptions = new RequestOptions()
            .centerCrop()
            .dontAnimate()
            .format(DecodeFormat.PREFER_RGB_565)
            .placeholder(R.mipmap.icon_image_default)
            .error(R.mipmap.icon_image_error);

    private RequestOptions mPreOptions = new RequestOptions()
            .skipMemoryCache(true)
            .error(R.mipmap.icon_image_error);

    @Override
    public void loadImage(ImageView imageView, String imagePath) {
        //小图加载
        Glide.with(imageView.getContext()).load(imagePath).apply(mOptions).into(imageView);
    }

    @Override
    public void loadPreImage(ImageView imageView, String imagePath) {
        //大图加载
        Glide.with(imageView.getContext()).load(imagePath).apply(mPreOptions).into(imageView);

    }

    @Override
    public void clearMemoryCache() {
        //清理缓存
        Glide.get(MApplication.getContext()).clearMemory();
    }
}
          
3、一行代码调用：
     ImagePicker.getInstance()
                        .setTitle("标题")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .showLoading(true, "ssssssssssss")   //是否显示首次加载框
                        .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setImagePaths(mImagePaths)//设置历史选择记录
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(MainActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                        
                        
4、调用系统相机
                 CameraConfig config = new CameraConfig.Builder()
                        .needCrop(false)
                        .cropSize(1, 1, 200, 200)
                        .build();
                imagePicker.getInstance().toCameraActivity(MainActivity.this, config, REQUEST_SELECT_IMAGES_CODE);

              
5、如何获取选中的图片集合：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            List<String> imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
        }
    }
```             
                
