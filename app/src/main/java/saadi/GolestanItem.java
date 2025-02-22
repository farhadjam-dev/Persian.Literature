package saadi;

public class GolestanItem {
    private String title;
    private int imageResId;

    public GolestanItem(String title, int imageResId) {
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