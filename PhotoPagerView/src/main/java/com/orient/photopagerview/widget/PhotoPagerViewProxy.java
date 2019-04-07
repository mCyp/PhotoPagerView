package com.orient.photopagerview.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.orient.photopagerview.listener.DeleteListener;
import com.orient.photopagerview.R;

import java.util.List;

/**
 * Created by wangjie on 2019/4/2.
 */

public class PhotoPagerViewProxy implements IPhotoPager {
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_QQ = 2;
    public static final int TYPE_WE_CHAT = 3;

    public static final int ANIMATION_SCALE_ALPHA = 1;
    public static final int ANIMATION_TRANSLATION = 2;

    private IPhotoPager photoPageView;

    private PhotoPagerViewProxy(Context context, int type, Config config) {
        switch (type) {
            case TYPE_QQ:
                photoPageView = new QQPager(context,R.style.Dialog);
                break;
            case TYPE_WE_CHAT:
                break;
            default:
                photoPageView = new NormalPager(context, R.style.Dialog);
                break;
        }
        setConfig(config);
    }

    @Override
    public void show() {
        photoPageView.show();
    }

    @Override
    public void dismiss() {
        photoPageView.dismiss();
    }

    @Override
    public void setConfig(Config config) {
        photoPageView.setConfig(config);
    }

    public static class Builder {
        private Activity context;
        private IPhotoPager.Config config;
        private int type;

        public Builder(Activity context, int type) {
            this.context = context;
            this.config = new IPhotoPager.Config();
            this.type = type;
        }

        public Builder(Activity context) {
            // default type is TYPE_NORMAL
            this(context, TYPE_NORMAL);
        }

        /*
            bitmaps
        */
        public Builder addBitmaps(List<Bitmap> bitmaps) {
            config.bitmaps = bitmaps;
            return this;
        }

        /*
            show delete function, default is no
         */
        public Builder showDelete(boolean canDelete) {
            config.canDelete = canDelete;
            return this;
        }

        /*
            onDelete
         */
        public Builder setDeleteListener(DeleteListener deleteListener) {
            config.deleteListener = deleteListener;
            return this;
        }

        public Builder showAnimation(boolean isShowAnimation) {
            config.isShowAnimation = isShowAnimation;
            return this;
        }

        public Builder setAnimationType(int type) {
            config.animationType = type;
            return this;
        }

        public Builder setStartPosition(int startPosition) {
            config.startPosition = startPosition;
            return this;
        }

        public PhotoPagerViewProxy create() {
            return new PhotoPagerViewProxy(context, type, config);
        }
    }
}
