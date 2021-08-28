package id.akhir.proyek.rukuntetangga.models;

public class Month {
    String monthName;
    int id;
    int colorBackground;

    public Month(int id, String monthName, int colorBackground) {
        this.id = id;
        this.monthName = monthName;
        this.colorBackground = colorBackground;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    @Override
    public String toString() {
        return monthName;
    }
}
