package hafez;


public class HafezItem {
    private String title;
    private int iconResId;

    public HafezItem(String title, int iconResId) {
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
