package com.orient.photopagerview.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import com.orient.photopagerview.listener.DeleteListener;
import com.orient.photopagerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Pager
 *
 * Created by wangjie on 2019/4/2.
 */

@SuppressWarnings("WeakerAccess")
public abstract class BasePager extends Dialog
        implements ViewPager.OnPageChangeListener,IPhotoPager {

    protected Context mContext;
    // all base info
    private IPhotoPager.Config mConfig;

    // basic info
    protected int curPosition;
    protected boolean isCanDelete;
    protected boolean isShowAnimation;
    protected int animationType;
    protected DeleteListener deleteListener;

    protected List<Bitmap> bitmaps;

    public BasePager(@NonNull Context context) {
        this(context, R.style.Dialog);
    }

    public BasePager(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        mContext = context;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void setConfig(Config config) {
        this.mConfig = config;
        initParams();
    }

    /*
        init parameter
     */
    private void initParams() {
        this.isCanDelete = mConfig.canDelete;
        this.isShowAnimation = mConfig.isShowAnimation;
        this.animationType = mConfig.animationType;
        this.curPosition = mConfig.startPosition;

        // init bitmaps
        this.bitmaps = new ArrayList<>();
        this.bitmaps.addAll(mConfig.bitmaps);
        this.deleteListener = mConfig.deleteListener;
    }
}
