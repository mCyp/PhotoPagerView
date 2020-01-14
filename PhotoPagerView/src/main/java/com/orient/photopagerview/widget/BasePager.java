package com.orient.photopagerview.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.orient.photopagerview.barrage.BarrageData;
import com.orient.photopagerview.listener.DeleteListener;
import com.orient.photopagerview.R;

import java.util.ArrayList;
import java.util.List;

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
    protected boolean isShowBarrages;

    protected List<String> paths;
    protected List<BarrageData> barrages;

    public BasePager(@NonNull Context context) {
        this(context, R.style.Dialog);
    }

    public BasePager(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(1f);
        }
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
        this.paths = new ArrayList<>();
        this.paths.addAll(mConfig.paths);
        this.deleteListener = mConfig.deleteListener;
        this.barrages = mConfig.barrages;
        this.isShowBarrages = mConfig.isShowBarrage;
    }

    @Override
    public void show() {
        if(paths == null || paths.size() == 0){
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
