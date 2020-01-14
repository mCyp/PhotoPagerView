package com.orient.photopagerview.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wangjie on 2018/10/15.
 */

public class MyViewPager extends ViewPager {

    private boolean isIntercept = false;

    public MyViewPager(@NonNull Context context) {
        super(context);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIntercept(boolean isIntercept){
        this.isIntercept = isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isIntercept)
            return false;
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isIntercept)
            return true;
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
