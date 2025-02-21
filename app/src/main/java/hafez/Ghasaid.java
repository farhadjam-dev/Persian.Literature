package hafez;

import java.util.List;

public class Ghasaid {
    private String title;
    private List<Verse> verses;

    public Ghasaid(String title, List<Verse> verses) {
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