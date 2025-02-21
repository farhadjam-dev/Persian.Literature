package hafez;

import java.util.List;

public class Rubaiyat {
    private String title;
    private List<Verse> verses;

    public Rubaiyat(String title, List<Verse> verses) {
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