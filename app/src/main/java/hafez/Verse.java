package hafez;




public class Verse {
    private String text; // متن بیت
    private String explanation; // تفسیر بیت

    public Verse(String text, String explanation) {
        this.text = text;
        this.explanation = explanation;
    }

    public String getText() {
        return text;
    }

    public String getExplanation() {
        return explanation;
    }
}