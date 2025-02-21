package hafez;

import java.util.List;

public class Ghitaat {
    private String title;
    private List<Verse> verses;

    public Ghitaat(String title, List<Verse> verses) {
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