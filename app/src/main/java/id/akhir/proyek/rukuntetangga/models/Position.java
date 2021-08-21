package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Position {
    @SerializedName("id")
    int id;
    @SerializedName("nama_posisi")
    String positionName;

    public Position(int id, String positionName) {
        this.id = id;
        this.positionName = positionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    @Override
    public String toString() {
        return positionName;
    }
}
