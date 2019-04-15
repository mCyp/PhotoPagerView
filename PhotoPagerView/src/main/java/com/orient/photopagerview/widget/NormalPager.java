package com.orient.photopagerview.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.orient.photopagerview.R;
import com.orient.photopagerview.adapter.PhotoPagerAdapter;

import java.util.Locale;

import static com.orient.photopagerview.widget.PhotoPagerViewProxy.ANIMATION_SCALE_ALPHA;
import static com.orient.photopagerview.widget.PhotoPagerViewProxy.ANIMATION_TRANSLATION;

/**
 * Normal style
 *
 * Created by wangjie on 2019/4/1.
 */

public class NormalPager extends BasePager
    implements View.OnClickListener{
    private String TAG = "NormalPager";

    private MyViewPager mPhotoPager;
    private ImageView mDelete;
    private TextView mPosition;
    private ImageView mBack;

    private PhotoPagerAdapter mAdapter;

    public NormalPager(@NonNull Context context) {
        super(context);
    }

    public NormalPager(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_normal_pager, null);
        setContentView(root);
        initWidget(root);
    }

    private void initWidget(View root){
        mBack = root.findViewById(R.id.iv_back);
        mPosition = root.findViewById(R.id.tv_position);
        mDelete = root.findViewById(R.id.iv_close);
        mPhotoPager = root.findViewById(R.id.pager);

        mBack.setOnClickListener(this);
        mDelete.setOnClickListener(this);

        if(!isCanDelete)
            mDelete.setVisibility(View.GONE);

        // set viewpager adapter
        mPhotoPager.addOnPageChangeListener(this);
        mPhotoPager.setAdapter(mAdapter = new PhotoPagerAdapter(mContext, bitmaps));
        mPhotoPager.setCurrentItem(curPosition);

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
                deleteListener.onDelete(curPosition);
            bitmaps.remove(curPosition);
            dismiss();
            return;
        }
        if (deleteListener != null)
            deleteListener.onDelete(curPosition);
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
        Log.e(TAG,"pos:"+position+",offset:"+positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        mPosition.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, bitmaps.size()));
        curPosition = position;
    }
}
