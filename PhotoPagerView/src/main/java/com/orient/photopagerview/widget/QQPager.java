package com.orient.photopagerview.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.photopagerview.R;
import com.orient.photopagerview.adapter.PhotoPagerAdapter;
import com.orient.photopagerview.listener.SimpleAnimationListener;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * QQ Theme Dialog
 * <p>
 * Created by wangjie on 2019/3/31.
 */

public class QQPager extends BasePager {
    private static final String TAG = "QQPager";
    private static final int SCROLL_THRESHOlD = 100;
    private static final int MSG_UP = 0;
    //private static final int SPEED_THRESHOLD = 2000;

    private ImageView mBarrage;
    private MyViewPager mPhotoPager;
    private TextView mPosition;

    private PhotoPagerAdapter mAdapter;

    private int touchSloop;
    private float lastX;
    private float lastY;
    private float deltaY;
    private boolean isHorizontalMove = false;
    private boolean isVerticalMove = false;
    private boolean isMove = false;
    private int clickCount = 0;

    private Handler mHandler = new QQPagerHandler(this);

    // no need to use
    //private VelocityTracker mVelocityTracker;


    public QQPager(@NonNull Context context) {
        this(context, R.style.Dialog);
    }

    public QQPager(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        touchSloop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_qq_pager, null);
        setContentView(root);
        initWidget(root);
    }

    private void initWidget(View root) {
        mBarrage = root.findViewById(R.id.iv_barrage);
        mPosition = root.findViewById(R.id.tv_position);
        mPhotoPager = root.findViewById(R.id.pager);

        // set viewpager adapter
        mPhotoPager.addOnPageChangeListener(this);
        mPhotoPager.setAdapter(mAdapter = new PhotoPagerAdapter(mContext, bitmaps));
        mPhotoPager.setCurrentItem(curPosition);
        mPhotoPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPosition.setText(String.format(Locale.getDefault(), "%d/%d", curPosition + 1, bitmaps.size()));

    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (isHorizontalMove)
            return super.dispatchTouchEvent(ev);


        float curX = ev.getX();
        float curY = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosition.setAlpha(1f);
                mPosition.setVisibility(View.VISIBLE);
                isMove = false;
                clickCount++;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = curX - lastX;
                deltaY = curY - lastY;
                if (Math.abs(deltaX) > touchSloop || Math.abs(deltaY) > touchSloop) {
                    isMove = true;
                    //isDoubleTap = false;
                    clickCount = 0;
                }
                if (Math.abs(deltaX) < Math.abs(deltaY)) {
                    isVerticalMove = true;
                    mPhotoPager.setIntercept(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (clickCount == 1 && !isMove)
                    mHandler.sendEmptyMessageDelayed(MSG_UP, 400);
                else
                    clickCount = 0;
                break;

        }
        lastX = curX;
        lastY = curY;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mPhotoPager.offsetTopAndBottom((int) deltaY);

                // set dialog's background alpha
                float offsetPercent = Math.abs(mPhotoPager.getTop() - 0f) / mPhotoPager.getMeasuredHeight();
                if (getWindow() != null)
                    getWindow().setDimAmount(1f - offsetPercent);
                break;

            case MotionEvent.ACTION_UP:
                if (isVerticalMove) {
                    if (Math.abs(mPhotoPager.getTop() - 0f) > SCROLL_THRESHOlD) {
                        scrollCloseAnimation();
                    } else {
                        rollbackAnimation();
                    }
                }
                break;

        }

        return super.onTouchEvent(event);
    }

    /**
     * if scroll distance is less than SCROLL_THRESHOlD,
     * the ViewPager will scroll to initial position;
     */
    private void rollbackAnimation() {
        mPhotoPager.animate()
                .y(0)
                .setDuration(300)
                .setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        isVerticalMove = false;
                        mPhotoPager.setIntercept(false);
                    }
                })
                .start();
    }


    /**
     * ViewPager begin up or down ValueAnimation;
     */
    private void scrollCloseAnimation() {
        Window window = getWindow();
        if (window != null)
            window.setDimAmount(0f);
        if (deltaY > 0) {
            mPhotoPager.animate()
                    .y(mPhotoPager.getMeasuredHeight())
                    .setDuration(600)
                    .setListener(new SimpleAnimationListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //getWindow().setWindowAnimations(R.style.PhotoPagerAlpha);
                            dismiss();
                        }
                    })
                    .start();
        } else {
            mPhotoPager.animate()
                    .y(-mPhotoPager.getMeasuredHeight())
                    .setDuration(600)
                    .setListener(new SimpleAnimationListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //getWindow().setWindowAnimations(R.style.PhotoPagerAlpha);
                            dismiss();
                        }
                    })
                    .start();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);

        if (positionOffset > 0.1 && !isVerticalMove)
            isHorizontalMove = true;

        if (positionOffset == 0) {
            isHorizontalMove = false;
        }
    }

    @Override
    public void show() {
        super.show();

        positionTextAlphaAnimation();
    }

    @Override
    public void onPageSelected(int position) {
        mPosition.setAlpha(1f);
        mPosition.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, bitmaps.size()));
        positionTextAlphaAnimation();
        curPosition = position;
    }

    private void positionTextAlphaAnimation(){
        mPosition.animate()
                .alpha(0f)
                .setStartDelay(500)
                .setDuration(500)
                .setListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mPosition.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }

    @Override
    public void dismiss() {
        mHandler.removeCallbacksAndMessages(null);

        super.dismiss();
    }

    private static class QQPagerHandler extends Handler {
        private WeakReference<QQPager> mQQPagerReference;

        QQPagerHandler(QQPager qqPager) {
            this.mQQPagerReference = new WeakReference<QQPager>(qqPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_UP:
                    if (mQQPagerReference.get().clickCount == 1)
                        mQQPagerReference.get().dismiss();
                    else
                        mQQPagerReference.get().clickCount = 0;
                    break;
            }
        }
    }
}
