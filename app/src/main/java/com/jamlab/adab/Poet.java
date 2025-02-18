package com.jamlab.adab;


import java.io.Serializable;

public class Poet implements Serializable {
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
