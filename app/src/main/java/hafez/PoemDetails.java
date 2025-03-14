package hafez;

import java.util.List;

public class PoemDetails {
    private List<Verse> verses;
    private PoemInfo poemInfo;

    public PoemDetails(List<Verse> verses, PoemInfo poemInfo) {
        this.verses = verses;
        this.poemInfo = poemInfo;
    }

    public List<Verse> getVerses() {
        return verses;
    }

    public PoemInfo getPoemInfo() {
        return poemInfo;
    }
}