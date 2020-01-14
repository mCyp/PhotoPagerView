package com.orient.photopagerview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;



/**
 * Author WangJie
 * Created on 2018/10/10.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private SparseArray<PhotoView> mViews;
    private List<String> paths;

    public PhotoPagerAdapter(Context context, List<String> paths) {
        this.mContext = context;
        this.paths = paths;
        mViews = new SparseArray<>(paths.size());
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getCount() {
        return paths.size();
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
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            String p = paths.get(position);
            int[] size = PhotoUtils.getScreenSize(mContext);
            Bitmap bitmap = PhotoUtils.getSingleBitmapByPath(p,size[0],size[1]);
            view.setImageBitmap(bitmap);
            mViews.put(position,view);
        }
        container.addView(view,0);
        return view;
    }
}
