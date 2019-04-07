package com.orient.photopagerview.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.photopagerview.R;
import com.orient.photopagerview.adapter.PhotoPagerAdapter;
import com.orient.photopagerview.listener.SimpleAnimationListener;

import java.util.Locale;

/**
 * QQ Theme Dialog
 * <p>
 * Created by wangjie on 2019/3/31.
 */

public class QQPager extends BasePager {
    // TODO
    // 1. 控制头部位置显示的时间
    // 2. 防止Handler和属性动画发生的内存泄漏

    private static final String TAG = "QQPager";
    private static final int SCROLL_THRESHOlD = 100;
    private static final int MSG_UP = 0;
    //private static final int SPEED_THRESHOLD = 2000;

    private ImageView mBarrage;
    private MyViewPager mPhotoPager;
    private TextView mPosition;

    private PhotoPagerAdapter mAdapter;

    //private Scroller mScroller;

    private int touchSloop;
    private float lastX;
    private float lastY;
    private float deltaX;
    private float deltaY;
    private boolean isHorizontalMove = false;
    private boolean isVerticalMove = false;
    private boolean isMove = false;
    //private GestureDetector mGestureDetector;
    private long pressTime;
    private boolean isDoubleTap = false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_UP:
                    if(!isDoubleTap && !isMove)
                        dismiss();
                    else if(isDoubleTap){
                        isDoubleTap = false;
                        pressTime = 0;
                    }else
                        pressTime = 0;
                    break;
            }
        }
    };

    // no need to use
    //private VelocityTracker mVelocityTracker;


    public QQPager(@NonNull Context context) {
        super(context);
    }

    public QQPager(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        touchSloop =  ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_qq_pager, null);
        setContentView(root);
        initWidget(root);
        initListener();
        Window window = getWindow();
        if (window != null) {
            window.setDimAmount(1f);
            //window.setWindowAnimations(R.style.PhotoPagerScale);
        }
    }

    private void initListener() {
        //mGestureDetector = new GestureDetector(mContext, simpleOnGestureListener);

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
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (isHorizontalMove)
            return super.dispatchTouchEvent(ev);

        /*if (isVerticalMove)
            return false;*/

        float curX = ev.getX();
        float curY = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPosition.setVisibility(View.VISIBLE);
                isMove = false;
                if(pressTime != 0)
                    isDoubleTap = true;
                pressTime = System.currentTimeMillis();
                Log.e(TAG,"isDouble:"+isDoubleTap+",time:"+pressTime);
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = curX - lastX;
                deltaY = curY - lastY;
                if(Math.abs(deltaX)>touchSloop || Math.abs(deltaY)> touchSloop){
                    isMove = true;
                    isDoubleTap = false;
                }
                Log.e(TAG,"MOVE:"+isDoubleTap);
                if (Math.abs(deltaX) < Math.abs(deltaY)) {
                    isVerticalMove = true;
                    mPhotoPager.setIntercept(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                mPosition.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(new SimpleAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mPosition.setVisibility(View.GONE);
                            }
                        })
                        .start();
                if(!isDoubleTap)
                    mHandler.sendEmptyMessageDelayed(MSG_UP,200);
                else if(!isMove)
                    pressTime = 0;

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
                float offsetPercent = Math.abs(mPhotoPager.getTop() - 0f) / mPhotoPager.getMeasuredHeight();
                //Log.e(TAG, "v:" + mPhotoPager.getTop() + ",H:" + mPhotoPager.getMeasuredHeight());
                getWindow().setDimAmount(1f - offsetPercent);
                break;

            case MotionEvent.ACTION_UP:
                if (isVerticalMove) {
                    if (Math.abs(mPhotoPager.getTop() - 0f) > SCROLL_THRESHOlD) {
                        getWindow().setDimAmount(0f);
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
                    } else {
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
                }
                break;

        }

        return super.onTouchEvent(event);
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
    public void onPageSelected(int position) {
        mPosition.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, bitmaps.size()));
        curPosition = position;
    }

    @Override
    public void dismiss() {
        mHandler.removeCallbacksAndMessages(null);

        super.dismiss();
    }

    /*private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO exit animation
            dismiss();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //return super.onDoubleTap(e);

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO provider long prick
            super.onLongPress(e);
        }
    };*/


}
