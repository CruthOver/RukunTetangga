package id.akhir.proyek.rukuntetangga.models;

public class MenuGrid {
    int title;
    int imageIcon;
    Class<?> mClassDestination;

    public MenuGrid(int title, int imageIcon, Class<?> classDestination) {
        this.title = title;
        this.imageIcon = imageIcon;
        this.mClassDestination = classDestination;
    }

    public int getTitle() {
        return title;
    }

    public Class<?> getClassDestination() {
        return mClassDestination;
    }

    public void setTitle(int title) {
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
}
