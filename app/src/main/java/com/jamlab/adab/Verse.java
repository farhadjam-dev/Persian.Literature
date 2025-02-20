package com.jamlab.adab;

public class Verse {
    private String text;
    private String explanation;

    public Verse(String text, String explanation) {
        this.text = text;
        this.explanation = explanation;
    }

    public String getText() {
        return text;
    }

    public String getExplanation() {
        return explanation;
    }
}