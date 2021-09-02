package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class KasAdmin {
    @SerializedName("user_id")
    int userId;
    @SerializedName("name")
    String name;
    @SerializedName("tanggal_bayar")
    String datePay;
    @SerializedName("status")
    String status;
    @SerializedName("month")
    int month;
    @SerializedName("tahun")
    int year;

    public KasAdmin(int userId, String name, String datePay, String status, int month, int year) {
        this.userId = userId;
        this.name = name;
        this.datePay = datePay;
        this.status = status;
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
