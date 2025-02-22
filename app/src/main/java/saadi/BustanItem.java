package saadi;

public class BustanItem {
    private String title;
    private int imageResId;

    public BustanItem(String title, int imageResId) {
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