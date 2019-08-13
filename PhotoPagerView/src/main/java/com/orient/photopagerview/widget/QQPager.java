package com.orient.photopagerview.widget;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orient.photopagerview.R;
import com.orient.photopagerview.adapter.PhotoPagerAdapter;
import com.orient.photopagerview.barrage.BarrageData;
import com.orient.photopagerview.listener.SimpleAnimationListener;
import com.orient.tea.barragephoto.adapter.BarrageAdapter;
import com.orient.tea.barragephoto.ui.BarrageView;

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

    private ImageView mBarrage;
    private MyViewPager mPhotoPager;
    private TextView mPosition;
    private PhotoPagerAdapter mAdapter;

    private BarrageView mBarrageView;
    private BarrageAdapter<BarrageData> mBarrageAdapter;
    private boolean isInitBarrage;


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
        initBarrages();
    }

    private void initBarrages() {
        BarrageView.Options options = new BarrageView.Options()
                .setGravity(BarrageView.GRAVITY_TOP)                // 设置弹幕的位置
                .setInterval(50)                                     // 设置弹幕的发送间隔
                .setSpeed(200, 29)                   // 设置速度和波动值
                .setModel(BarrageView.MODEL_COLLISION_DETECTION)     // 设置弹幕生成模式
                .setClick(false);                                    // 设置弹幕是否可以点击
        mBarrageView.setOptions(options);
        // 设置适配器 第一个参数是点击事件的监听器
        mBarrageView.setAdapter(mBarrageAdapter = new BarrageAdapter<BarrageData>(null, mContext) {
            @Override
            public BarrageViewHolder<BarrageData> onCreateViewHolder(View root, int type) {
                if (type == R.layout.barrage_item_normal)
                    return new ViewHolder(root);
                else
                    return new TextViewHolder(root);
            }

            @Override
            public int getItemLayout(BarrageData barrageData) {
                switch (barrageData.getType()) {
                    case BarrageData.TYPE_IMAGE:
                        return R.layout.barrage_item_normal;
                    default:
                        return R.layout.barrage_item_text;
                }

            }
        });

        mBarrage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowBarrages) {
                    // close barrage
                    mBarrageView.destroy();
                    mBarrage.setImageResource(R.drawable.ic_barrage_close);
                    isShowBarrages = false;
                } else {
                    mBarrage.setImageResource(R.drawable.ic_barrage_open);
                    if (!isInitBarrage)
                        initBarrageData();
                    isShowBarrages = true;
                }
            }
        });

        if (isShowBarrages)
            mBarrage.setImageResource(R.drawable.ic_barrage_open);
        else
            mBarrage.setImageResource(R.drawable.ic_barrage_close);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (isShowBarrages) {
            if (!isInitBarrage) {
                initBarrageData();
                isInitBarrage = true;
            }
        }
    }

    private void initBarrageData() {
        if (barrages.size() != 0) {
            for (BarrageData barrage : barrages) {
                mBarrageAdapter.add(barrage);
            }
        }
    }

    private void initWidget(View root) {
        mBarrage = root.findViewById(R.id.iv_barrage);
        mPosition = root.findViewById(R.id.tv_position);
        mPhotoPager = root.findViewById(R.id.pager);
        mBarrageView = root.findViewById(R.id.barrage);

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
                if (clickCount == 1 && !isMove &&
                        !isTouchPointInView(mBarrage,(int) ev.getRawX(),(int) ev.getRawY()))
                    mHandler.sendEmptyMessageDelayed(MSG_UP, 400);
                else
                    clickCount = 0;
                break;

        }
        lastX = curX;
        lastY = curY;
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchPointInView(View targetView, int xAxis, int yAxis) {
        if (targetView == null) {
            return false;
        }
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + targetView.getMeasuredWidth();
        int bottom = top + targetView.getMeasuredHeight();
        if (yAxis >= top && yAxis <= bottom && xAxis >= left
                && xAxis <= right) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mPhotoPager.scrollBy(0, (int) -deltaY);
                //Log.e(TAG, "dis move:" + deltaY + ",top" + mPhotoPager.getTop());

                // set dialog's background alpha
                float offsetPercent = Math.abs(mPhotoPager.getScrollY() - 0f) / mPhotoPager.getMeasuredHeight();
                Log.e(TAG,"offset:"+offsetPercent);
                if (getWindow() != null)
                    getWindow().setDimAmount(1f - offsetPercent);
                break;

            case MotionEvent.ACTION_UP:
                //Log.e(TAG, "dis up!");
                if (isVerticalMove) {
                    if (Math.abs(mPhotoPager.getScrollY() - 0f) > SCROLL_THRESHOlD) {
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

    private void positionTextAlphaAnimation() {
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
        if (mBarrageView != null)
            mBarrageView.destroy();

        super.dismiss();
    }

    class TextViewHolder extends BarrageAdapter.BarrageViewHolder<BarrageData> {

        private TextView mContent;

        TextViewHolder(View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(BarrageData data) {
            mContent.setText(data.getContent());
        }
    }

    class ViewHolder extends BarrageAdapter.BarrageViewHolder<BarrageData> {

        private ImageView mHeadView;
        private TextView mContent;

        ViewHolder(View itemView) {
            super(itemView);

            mHeadView = itemView.findViewById(R.id.image);
            mContent = itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(BarrageData data) {
            if (!TextUtils.isEmpty(data.getPath()))
                Glide.with(mContext).load(data.getPath())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mHeadView);
            else
                Glide.with(mContext).load(data.getResource())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mHeadView);
            mContent.setText(data.getContent());
        }
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
