package saadi;

public class MavaezItem {
    private String title;
    private int imageResId;

    public MavaezItem(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
}