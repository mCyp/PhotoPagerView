package com.orient.photopagerview.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * QQ Theme Dialog
 *
 * Created by wangjie on 2019/3/31.
 */

public class QQPager extends BasePager {


    public QQPager(@NonNull Context context) {
        super(context);
    }

    public QQPager(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
