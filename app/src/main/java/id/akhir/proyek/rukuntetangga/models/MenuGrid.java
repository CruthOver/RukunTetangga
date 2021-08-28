package id.akhir.proyek.rukuntetangga.models;

public class MenuGrid {
    String title;
    int imageIcon;
    Class<?> mClassDestination;

    public MenuGrid(String title, int imageIcon, Class<?> classDestination) {
        this.title = title;
        this.imageIcon = imageIcon;
        this.mClassDestination = classDestination;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getClassDestination() {
        return mClassDestination;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setClass(Class<?> classDestination) {
        this.mClassDestination = classDestination;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public String toString() {
        return title;
    }
}
