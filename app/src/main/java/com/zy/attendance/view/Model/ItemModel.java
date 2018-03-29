package com.zy.attendance.view.Model;

/**
 * Created by lenovo on 2018/3/27.
 */

public class ItemModel {

    private int iconId;
    private String title;
    private String content;

    public ItemModel(int icon, String title, String content) {
        this.iconId = icon;
        this.title = title;
        this.content = content;
    }

    public ItemModel(int iconId, String title) {
        this.iconId = iconId;
        this.title = title;
        this.content = "";
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconid) {
        this.iconId = iconid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
