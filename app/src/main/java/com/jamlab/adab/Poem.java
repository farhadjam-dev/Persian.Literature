package com.jamlab.adab;

import android.os.Parcel;
import android.os.Parcelable;

public class Poem implements Parcelable {
    private String title;
    private String text;

    public Poem(String title, String text) {
        this.title = title;
        this.text = text;
    }

    protected Poem(Parcel in) {
        title = in.readString();
        text = in.readString();
    }

    public static final Creator<Poem> CREATOR = new Creator<Poem>() {
        @Override
        public Poem createFromParcel(Parcel in) {
            return new Poem(in);
        }

        @Override
        public Poem[] newArray(int size) {
            return new Poem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
    }
}