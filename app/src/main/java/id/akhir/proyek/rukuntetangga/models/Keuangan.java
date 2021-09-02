package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Keuangan {
    @SerializedName("id")
    int id;
    @SerializedName("bulan_id")
    int bulanId;
    @SerializedName("tahun")
    int year;
    @SerializedName("pemasukkan")
    int income;
    @SerializedName("pengeluaran")
    int expense;
    @SerializedName("created_at")
    String createdAt;
    @SerializedName("created_by")
    int createdBy;
    boolean isExpanded;

    public Keuangan(int id, int bulanId, int year, int income, int expense, String createdAt, int createdBy) {
        this.id = id;
        this.bulanId = bulanId;
        this.income = income;
        this.year = year;
        this.expense = expense;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBulanId() {
        return bulanId;
    }

    public void setBulanId(int bulanId) {
        this.bulanId = bulanId;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int pemasukkan) {
        this.income = pemasukkan;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
