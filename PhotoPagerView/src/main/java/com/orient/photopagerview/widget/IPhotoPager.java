package com.orient.photopagerview.widget;

import android.graphics.Bitmap;

import com.orient.photopagerview.barrage.BarrageData;
import com.orient.photopagerview.listener.DeleteListener;

import java.util.List;

/**
 * common interface
 *
 * Created by wangjie on 2019/4/1.
 */

public interface IPhotoPager {
    void show();
    void dismiss();
    void setConfig(Config config);

    /*
        config
     */
    class Config {
        List<String> paths;
        List<Bitmap> bitmaps;
        boolean canDelete = true;
        boolean isShowAnimation = false;
        boolean isShowBarrage = true;
        int animationType;
        int startPosition = 0;
        DeleteListener deleteListener;
        List<BarrageData> barrages;
    }
}
