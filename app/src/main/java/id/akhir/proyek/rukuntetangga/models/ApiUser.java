package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiUser<t> {
    @SerializedName("data")
    public t data;

    public t getData() {
        return data;
    }

    public void setData(t data) {
        this.data = data;
    }
}