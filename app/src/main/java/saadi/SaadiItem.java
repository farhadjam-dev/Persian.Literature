package saadi;

public class SaadiItem {
    private String title;
    private int iconResId;

    public SaadiItem(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}