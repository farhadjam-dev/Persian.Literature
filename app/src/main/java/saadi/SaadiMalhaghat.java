package saadi;

import java.util.List;

import hafez.Verse;

public class SaadiMalhaghat {
    private String title;
    private List<Verse> verses;

    public SaadiMalhaghat(String title, List<Verse> verses) {
        this.title = title;
        this.verses = verses;
    }

    public String getTitle() {
        return title;
    }

    public List<Verse> getVerses() {
        return verses;
    }
}