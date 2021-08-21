package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Information {
    @SerializedName("id")
    private int id;
    @SerializedName("informasi")
    private String information;

    public Information(int id, String information) {
        this.id = id;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
