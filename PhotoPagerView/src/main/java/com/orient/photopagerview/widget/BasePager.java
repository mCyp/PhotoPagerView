package com.orient.photopagerview.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.orient.photopagerview.listener.DeleteListener;
import com.orient.photopagerview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.orient.photopagerview.widget.PhotoPagerViewProxy.ANIMATION_SCALE_ALPHA;
import static com.orient.photopagerview.widget.PhotoPagerViewProxy.ANIMATION_TRANSLATION;

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

    @Override
    public void show() {
        if(bitmaps == null || bitmaps.size() == 0){
            throw new RuntimeException("bitmaps can't be null");
        }

        super.show();

        // seting rect must be after dialog.showing(),otherwise dialog will show in initial size.
        Rect rect = new Rect();
        ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        // set position and size
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = rect.height();
        window.setAttributes(lp);
        if (isShowAnimation) {
            if (animationType == ANIMATION_SCALE_ALPHA) {
                window.setWindowAnimations(R.style.PhotoPagerScale);
            } else if (animationType == ANIMATION_TRANSLATION) {
                window.setWindowAnimations(R.style.PhotoPagerTranslation);
            } else {
                // default animaiont is translation
                window.setWindowAnimations(R.style.PhotoPagerAlpha);
            }
        }
    }
}
