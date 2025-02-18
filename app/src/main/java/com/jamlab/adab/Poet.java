package com.jamlab.adab;



public class Poet {
    private String name;
    private int imageResId;

    public Poet(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
