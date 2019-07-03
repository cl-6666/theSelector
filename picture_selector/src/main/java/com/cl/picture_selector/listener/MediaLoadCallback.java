package com.cl.picture_selector.listener;

import com.cl.picture_selector.data.MediaFolder;

import java.util.List;

/**
 * 图片扫描数据回调接口
 */
public interface MediaLoadCallback {

    void loadMediaSuccess(List<MediaFolder> mediaFolderList);
}
