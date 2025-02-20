package favorits;

import java.util.List;

public class Ghazal {
    private String title;
    private List<Verse> verses;

    public Ghazal(String title, List<Verse> verses) {
        this.title = title;
        this.verses = verses;
    }

    public String getTitle() {
        return title;
    }

    public List<Verse> getVerses() {
        return verses;
    }

    public String getFirstVerse() {
        if (verses != null && !verses.isEmpty()) {
            return verses.get(0).getText(); // بیت اول
        }
        return "..."; // در صورت عدم وجود بیت
    }
}