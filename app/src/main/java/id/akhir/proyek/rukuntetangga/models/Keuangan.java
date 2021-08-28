package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Keuangan {
    @SerializedName("id")
    int id;
    @SerializedName("saldo")
    int saldo;
    @SerializedName("bulan_id")
    int bulanId;
    @SerializedName("kebutuhan")
    String kebutuhan;
    @SerializedName("pemasukkan")
    int income;
    @SerializedName("pengeluaran")
    int expense;
    @SerializedName("created_at")
    String createdAt;
    @SerializedName("created_by")
    int createdBy;
    boolean isExpanded;

    public Keuangan(int id, int saldo, int bulanId, String kebutuhan, int income, int expense, String createdAt, int createdBy) {
        this.id = id;
        this.saldo = saldo;
        this.bulanId = bulanId;
        this.kebutuhan = kebutuhan;
        this.income = income;
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

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getBulanId() {
        return bulanId;
    }

    public void setBulanId(int bulanId) {
        this.bulanId = bulanId;
    }

    public String getKebutuhan() {
        return kebutuhan;
    }

    public void setKebutuhan(String kebutuhan) {
        this.kebutuhan = kebutuhan;
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
}
