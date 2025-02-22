package saadi;

import java.util.List;
import hafez.Verse;

public class BustanBab6Poem {
    private String title;
    private List<Verse> verses;

    public BustanBab6Poem(String title, List<Verse> verses) {
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