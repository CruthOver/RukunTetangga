package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class KasAdmin {
    @SerializedName("name")
    String name;
    @SerializedName("no_tagihan")
    String noTagihan;
    @SerializedName("tanggal_bayar")
    String datePay;
    @SerializedName("status")
    String status;

    public KasAdmin(String name, String noTagihan, String datePay, String status) {
        this.name = name;
        this.noTagihan = noTagihan;
        this.datePay = datePay;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoTagihan() {
        return noTagihan;
    }

    public void setNoTagihan(String noTagihan) {
        this.noTagihan = noTagihan;
    }

    public String getDatePay() {
        return datePay;
    }

    public void setDatePay(String datePay) {
        this.datePay = datePay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
