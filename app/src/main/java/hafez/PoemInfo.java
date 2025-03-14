package hafez;

public class PoemInfo {
    private String meter;
    private String form;
    private int verseCount;

    public PoemInfo(String meter, String form, int verseCount) {
        this.meter = meter;
        this.form = form;
        this.verseCount = verseCount;
    }

    public String getMeter() {
        return meter;
    }

    public String getForm() {
        return form;
    }

    public int getVerseCount() {
        return verseCount;
    }
}