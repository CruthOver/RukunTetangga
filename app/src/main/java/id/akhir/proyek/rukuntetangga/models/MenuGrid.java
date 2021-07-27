package id.akhir.proyek.rukuntetangga.models;

public class MenuGrid {
    int title;
    int imageIcon;

    public MenuGrid(int title, int imageIcon) {
        this.title = title;
        this.imageIcon = imageIcon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(int imageIcon) {
        this.imageIcon = imageIcon;
    }
}
