package com.orient.photopagerview.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orient.photopagerview.Common;
import com.orient.photopagerview.R;
import com.orient.photopagerview.adapter.PhotoPagerAdapter;
import com.orient.photopagerview.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Widget
 * <p>
 * Author WangJie
 * Created on 2018/10/9.
 */
@SuppressWarnings("ALL")
public class PhotoPageView implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private String TAG = "PhotoPageView";
    public static final int ANIMATION_SCALE_ALPHA = 1;
    public static final int ANIMATION_TRANSLATION = 2;

    private Activity mContext;
    private ImageView mBack;
    // show currentPosition
    private TextView mPosition;
    private ImageView mDelete;
    private MyViewPager mPhotoPager;

    // all base info
    private Config mConfig;

    // basic info
    private int curPosition;
    private Dialog dialog;
    private boolean isCanDelete;
    private boolean isShowAnimation;
    private int animationType;
    private DeleteListener deleteListener;

    private List<Bitmap> bitmaps;
    private PhotoPagerAdapter mAdapter;

    private PhotoPageView(Activity context, Config config) {
        this.mContext = context;
        this.mConfig = config;
        initParams();
        initView();
    }

    /*
        init parameter
     */
    private void initParams() {
        this.isCanDelete = mConfig.canDelete;
        this.isShowAnimation = mConfig.isShowAnimation;
        this.animationType = mConfig.animationType;
        this.curPosition = mConfig.startPosition;
        this.deleteListener = mConfig.deleteListener;

        // init bitmaps
        bitmaps = new ArrayList<>();
        bitmaps = mConfig.bitmaps;
    }

    private void initView() {
        dialog = new Dialog(mContext, R.style.Dialog);
        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_photo_pager, null);
        dialog.setContentView(root);

        mBack = root.findViewById(R.id.iv_back);
        mPosition = root.findViewById(R.id.tv_position);
        mDelete = root.findViewById(R.id.iv_close);
        mPhotoPager = root.findViewById(R.id.pager);

        mBack.setOnClickListener(this);
        mDelete.setOnClickListener(this);

        if (!isCanDelete) {
            mDelete.setVisibility(View.INVISIBLE);
        }

        // set viewpager adapter
        mPhotoPager.addOnPageChangeListener(this);
        mPhotoPager.setAdapter(mAdapter = new PhotoPagerAdapter(mContext, bitmaps));
        mPhotoPager.setCurrentItem(curPosition);
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        if(bitmaps == null || bitmaps.size() == 0){
            Log.e(TAG,"Bitmaps is null or empty");
            return;
        }
        dialog.show();

        mPosition.setText(String.format(Locale.getDefault(), "%d/%d", curPosition + 1, bitmaps.size()));
        // seting rect must be after dialog.showing(),otherwise dialog will show in initial size.
        Rect rect = new Rect();
        ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        // set position and size
        Window window = (dialog).getWindow();
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
                window.setWindowAnimations(R.style.PhotoPagerTranslation);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            dismiss();
        } else {
            deleteCurrentPosition();
        }
    }

    private void deleteCurrentPosition() {
        if (bitmaps.size() == 1) {
            if(deleteListener != null)
                deleteListener.ondelete(curPosition);
            bitmaps.remove(curPosition);
            dismiss();
            return;
        }
        if (deleteListener != null)
            deleteListener.ondelete(curPosition);
        if (curPosition != 0) {
            mPhotoPager.setCurrentItem(curPosition - 1);
            bitmaps.remove(curPosition + 1);
            mPhotoPager.setAdapter(mAdapter = new PhotoPagerAdapter(mContext, bitmaps));
        } else {
             mPhotoPager.setCurrentItem(curPosition + 1);
            bitmaps.remove(curPosition - 1);
            --curPosition;
            mPhotoPager.setAdapter(mAdapter = new PhotoPagerAdapter(mContext, bitmaps));
        }
        mPosition.setText(String.format(Locale.getDefault(), "%d/%d", curPosition + 1, bitmaps.size()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mPosition.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, bitmaps.size()));
        curPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /*
        config
     */
    static class Config {
        List<String> paths;
        List<Bitmap> bitmaps;
        boolean canDelete = true;
        boolean isShowAnimation = false;
        int animationType;
        int startPosition = 0;
        DeleteListener deleteListener;
    }

    public static class Builder {
        private Activity context;
        private Config config;

        public Builder(Activity context) {
            this.context = context;
            config = new Config();
        }

        /*
            bitmaps
         */
        public Builder addBitmaps(List<Bitmap> bitmaps){
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

        public PhotoPageView create() {
            return new PhotoPageView(context, config);
        }
    }

    public interface DeleteListener {
        void ondelete(int position);
    }
}
