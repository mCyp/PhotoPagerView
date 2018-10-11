package com.orient.photopagerview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import uk.co.senab.photoview.PhotoView;


/**
 *
 * Author WangJie
 * Created on 2018/10/10.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private SparseArray<PhotoView> mViews;
    private List<Bitmap> bitmaps;

    public PhotoPagerAdapter(Context context, List<Bitmap> bitmaps) {
        this.mContext = context;
        this.bitmaps = bitmaps;
        mViews = new SparseArray<>(bitmaps.size());
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView view = mViews.get(position);
        if(view == null){
            view = new PhotoView(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setScaleType(ImageView.ScaleType.CENTER);
            view.setImageBitmap(bitmaps.get(position));
            mViews.put(position,view);
        }
        container.addView(view,0);
        return view;
    }
}
