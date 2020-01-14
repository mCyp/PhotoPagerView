package com.orient.photopagerview.barrage;

import androidx.annotation.DrawableRes;

import com.orient.tea.barragephoto.model.DataSource;

public class BarrageData implements DataSource {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMAGE = 2;

    private String content;
    private String path;
    private @DrawableRes int resource;
    private int type;

    public BarrageData(String content, String path) {
        this.content = content;
        this.path = path;
        this.type = TYPE_IMAGE;
    }

    public BarrageData(String content, int resource) {
        this.content = content;
        this.resource = resource;
        this.type = TYPE_IMAGE;
    }

    public BarrageData(String content) {
        this.content = content;
        this.type = TYPE_TEXT;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public long getShowTime() {
        return 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public void setType(int type) {
        this.type = type;
    }
}
