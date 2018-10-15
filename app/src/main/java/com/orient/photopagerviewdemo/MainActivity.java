package com.orient.photopagerviewdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orient.photopagerview.utils.FileUtils;
import com.orient.photopagerview.widget.PhotoPageView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // Android 7.0因为相机的问题没有适配 只是针对本程序 对库没有影响
    // Tips : 如果运行在6.0之上需要动态的申请权限，本程序会因为没有权限退出
    // 或者在安装完成之后在设置中手动打开权限
    public static final int TAKE_PHOTO = 1;

    // 路径
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPhotoCacheDir();
        Button btnCamera = findViewById(R.id.btn_Camera);
        Button btnShow = findViewById(R.id.btn_Show);

        // 设置点击事件
        btnCamera.setOnClickListener(this);
        btnShow.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_Camera:{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = FileUtils.getMediaUriFromFile(getPhotoCacheDir().getAbsolutePath());
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,TAKE_PHOTO);
                break;
            }
            case R.id.btn_Show:{
                List<String> paths = FileUtils.getPhotoPaths(path);
                if(paths == null || paths.size() == 0){
                    Toast.makeText(MainActivity.this,"照片的数量为0",Toast.LENGTH_SHORT).show();
                    break;
                }

                // 显示表格
                PhotoPageView pageView = new PhotoPageView.Builder(MainActivity.this)
                        .addPaths(paths)
                        .showDelete(false)
                        .showAnimation(true)
                        .setAnimationType(PhotoPageView.ANIMATION_TRANSLATION)
                        .setStartPosition(0)
                        .create();
                pageView.show();
                break;
            }
        }
    }

    /*
        得到存储照片的父文件夹
     */
    public File getPhotoCacheDir(){
        File file = new File(getApplication().getExternalCacheDir(),"photo");
        path = file.getAbsolutePath();
        return file;
    }
}
