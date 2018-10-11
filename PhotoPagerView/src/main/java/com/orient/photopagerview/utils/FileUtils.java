package com.orient.photopagerview.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.orient.photopagerview.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * File utils
 *
 * Author WangJie
 * Created on 2018/10/8.
 */
public class FileUtils {

    /*
        the uri is used by System camera
     */
    public static Uri getMediaUriFromFile(String path) {
        File mediaStorageDir = new File(path);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(path,  "IMG_" + timeStamp + ".jpg");
        return Uri.fromFile(mediaFile);
    }

    /*
        get all jpg in a directory
     */
    public static List<String> getPhotoPaths(String path){
        List<String> paths = new ArrayList<>();
        File photoDirectory = new File(path);
        if(photoDirectory.exists()){
            File[] files = photoDirectory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(Common.Constant.PHOTO_NAME_SUFFIX);
                }
            });
            for(File f:files){
                if(f.exists()){
                    paths.add(f.getAbsolutePath());
                }
            }
        }
        return paths;
    }

    /*
        delete
     */
    public static void deleteFile(String path){
        File outFile = new File(path);
        if(outFile.isDirectory()){
            File[] files = outFile.listFiles();
            for (File file:files) {
                file.delete();
            }
        }
        outFile.delete();
    }

    /**
        get bitmaps
     */
    public static List<Bitmap> getAlbumByPath(String path, String Extension, Activity activity) {
        List<Bitmap> bitmaps = new LinkedList<>();                //结果 List
        File[] files = new File(path).listFiles();
        if (files == null)
            return null;
        InputStream inputStream = null;

        try {
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        inputStream = new FileInputStream(f);
                        BitmapFactory.decodeStream(inputStream, null, options);
                        options.inSampleSize = calculateInSampleSize(options,activity.getWindow().getDecorView().getWidth(),activity.getWindow().getDecorView().getHeight());
                        options.inJustDecodeBounds = false;
                        inputStream = new FileInputStream(f);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                        if (bitmap != null) {
                            bitmaps.add(bitmap);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmaps;
    }

    /**
        get bitmaps
     */
    public static List<Bitmap> getAlbumByPaths(List<String> paths, String Extension, Activity activity) {
        List<Bitmap> bitmaps = new LinkedList<>();
        if(paths == null || paths.size() == 0)
            return null;

        InputStream inputStream = null;
        try {
            for (String p : paths) {
                File f = new File(p);
                if (f.isFile()) {
                    if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        inputStream = new FileInputStream(f);
                        BitmapFactory.decodeStream(inputStream, null, options);
                        options.inSampleSize = calculateInSampleSize(options,activity.getWindow().getDecorView().getWidth(),activity.getWindow().getDecorView().getHeight());
                        options.inJustDecodeBounds = false;
                        inputStream = new FileInputStream(f);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                        if (bitmap != null) {
                            bitmaps.add(bitmap);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmaps;
    }

    /*
        to prevent oom
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
