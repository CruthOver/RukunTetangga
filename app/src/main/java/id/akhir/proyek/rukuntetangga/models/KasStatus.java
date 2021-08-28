package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class KasStatus {
    @SerializedName("bulan")
    String bulan;
    @SerializedName("status")
    boolean status;

    public String getBulan() {
        return bulan;
    }

    public boolean isStatus() {
        return status;
    }
}
