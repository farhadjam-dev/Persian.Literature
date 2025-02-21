package saadi;

public class SaadiDivanItem {
    private String title;
    private int iconResId;

    public SaadiDivanItem(String title, int iconResId) {
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