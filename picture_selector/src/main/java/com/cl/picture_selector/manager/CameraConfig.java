package com.cl.picture_selector.manager;

import java.io.Serializable;

/**
 * 项目：theSelector
 * 版权：蒲公英公司 版权所有
 * 作者：Arry
 * 版本：1.0
 * 创建日期：2019-12-02
 * 描述：
 * 修订历史：
 */
public class CameraConfig implements Serializable{


    /**
     * 是否需要裁剪
     */
    public boolean needCrop;

    /**
     * 拍照存储路径
     */
    public String filePath;

    /**
     * 裁剪输出大小
     */
    public int aspectX = 1;
    public int aspectY = 1;
    public int outputX = 500;
    public int outputY = 500;

    public CameraConfig(Builder builder) {
        this.needCrop = builder.needCrop;
        this.filePath = builder.filePath;
        this.aspectX = builder.aspectX;
        this.aspectY = builder.aspectY;
        this.outputX = builder.outputX;
        this.outputY = builder.outputY;
    }

    public static class Builder implements Serializable {

        private boolean needCrop = false;
        private String filePath;

        private int aspectX = 1;
        private int aspectY = 1;
        private int outputX = 400;
        private int outputY = 400;

        public Builder() {

        }

        public Builder needCrop(boolean needCrop) {
            this.needCrop = needCrop;
            return this;
        }

        private Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder cropSize(int aspectX, int aspectY, int outputX, int outputY) {
            this.aspectX = aspectX;
            this.aspectY = aspectY;
            this.outputX = outputX;
            this.outputY = outputY;
            return this;
        }

        public CameraConfig build() {
            return new CameraConfig(this);
        }
    }
}
