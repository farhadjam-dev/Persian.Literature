package hafez;


import java.util.List;

public class Ghazal {
    private String title; // عنوان غزل
    private List<Verse> verses; // لیست ابیات

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
}
