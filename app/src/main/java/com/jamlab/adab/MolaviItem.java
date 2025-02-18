package com.jamlab.adab;


public class MolaviItem {
    private String title; // عنوان بخش (مثل زندگی‌نامه، غزلیات و ...)
    private int iconResId; // آدرس تصویر مربوط به بخش

    public MolaviItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
