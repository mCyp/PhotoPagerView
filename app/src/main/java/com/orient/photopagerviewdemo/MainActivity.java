package com.orient.photopagerviewdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orient.photopagerview.barrage.BarrageData;
import com.orient.photopagerview.listener.DeleteListener;
import com.orient.photopagerview.utils.FileUtils;
import com.orient.photopagerview.widget.IPhotoPager;
import com.orient.photopagerview.widget.PhotoPagerViewProxy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.orient.photopagerview.widget.PhotoPagerViewProxy.TYPE_QQ;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String SEED[] = {"景色还不错啊", "小姐姐真好看！，", "又去哪里玩了？我也要去！", "门票多少啊？", "厉害啦！","666666"};
    private final int ICON_RESOURCES[] = {R.drawable.cat, R.drawable.corgi, R.drawable.lovelycat, R.drawable.boy, R.drawable.girl,R.drawable.samoyed};

    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<BarrageData> barrages = new LinkedList<>();

    // 路径
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initBarrageData();
    }

    private void initBarrageData() {
        int strLength = SEED.length;
        for (int i = 0; i < 10; i++) {
            int pos = i % strLength;
            barrages.add(new BarrageData(SEED[pos],ICON_RESOURCES[pos]));
        }
    }

    private void initData() {
        int[] v = FileUtils.getScreenSize(this);
        Bitmap b1 = getBitmap(R.drawable.d1,v);
        Bitmap b2 = getBitmap(R.drawable.d2,v);
        Bitmap b3 = getBitmap(R.drawable.d3,v);
        Bitmap b4 = getBitmap(R.drawable.d4,v);
        bitmaps.add(b1);
        bitmaps.add(b2);
        bitmaps.add(b3);
        bitmaps.add(b4);
    }

    public Bitmap getBitmap(@DrawableRes Integer drawable,int[] v){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeResource(getResources(),drawable,options);
        options.inSampleSize = FileUtils.calculateInSampleSize(options,v[0]
                ,v[1]);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(),drawable,options);
    }

    private void initView() {
        Button btnShow = findViewById(R.id.btn_normal);
        Button btnQQ = findViewById(R.id.btn_qq);
        // 设置点击事件
        btnShow.setOnClickListener(this);
        btnQQ.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(bitmaps == null || bitmaps.size() == 0){
            Toast.makeText(MainActivity.this,"照片的数量为0",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()){
            case R.id.btn_normal:{
                // 显示表格
                IPhotoPager pageView = new PhotoPagerViewProxy.Builder(MainActivity.this)
                        .addBitmaps(bitmaps)
                        .showDelete(true)
                        // 普通主题特有 删除事件
                        .setDeleteListener(new DeleteListener() {
                            @Override
                            public void onDelete(int position) {
                                // TODO 删除指定位置之后的回调
                                int p = position + 1;
                                Toast.makeText(MainActivity.this,"删除的位置是："+p,Toast.LENGTH_SHORT).show();
                            }
                        })
                        .showAnimation(true)
                        .setAnimationType(PhotoPagerViewProxy.ANIMATION_SCALE_ALPHA)
                        .setStartPosition(0)
                        .create();
                pageView.show();
                break;
            }
            case R.id.btn_qq:{
                // 显示表格
                IPhotoPager pageView = new PhotoPagerViewProxy.Builder(MainActivity.this,TYPE_QQ)
                        .addBitmaps(bitmaps)
                        .showAnimation(true)
                        .setAnimationType(PhotoPagerViewProxy.ANIMATION_ALPHA)
                        .setStartPosition(0)
                        // QQ主题特有
                        .setBarrages(barrages)
                        .showBarrages(true)
                        .create();
                pageView.show();
            }
        }
    }


}
