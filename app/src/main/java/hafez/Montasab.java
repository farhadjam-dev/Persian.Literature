package hafez;

import java.util.List;

public class Montasab {
    private String title;
    private List<Verse> verses;

    public Montasab(String title, List<Verse> verses) {
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