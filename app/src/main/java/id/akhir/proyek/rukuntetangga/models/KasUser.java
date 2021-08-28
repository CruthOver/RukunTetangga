package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KasUser {
    @SerializedName("tagihan")
    int tagihan;
    @SerializedName("batas_bayar")
    String duDatePay;
    @SerializedName("bulan")
    List<KasStatus> dataBulan;
    @SerializedName("nomor_telepon")
    String noTelp;
    @SerializedName("nomor_rekening")
    String nomorRekening;
    @SerializedName("nama_bank")
    String bankName;

    public int getTagihan() {
        return tagihan;
    }

    public void setTagihan(int tagihan) {
        this.tagihan = tagihan;
    }

    public String getDuDatePay() {
        return duDatePay;
    }

    public void setDuDatePay(String duDatePay) {
        this.duDatePay = duDatePay;
    }

    public List<KasStatus> getDataBulan() {
        return dataBulan;
    }

    public void setDataBulan(List<KasStatus> dataBulan) {
        this.dataBulan = dataBulan;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNomorRekening() {
        return nomorRekening;
    }

    public void setNomorRekening(String nomorRekening) {
        this.nomorRekening = nomorRekening;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}