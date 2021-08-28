package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class DetailVoting {
    @SerializedName("voting_id")
    private int voteId;
    @SerializedName("id")
    private int pilihanId;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("jumlah")
    private int jumlah;
    @SerializedName("pilihan")
    private String pilihan;

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getPilihanId() {
        return pilihanId;
    }

    public void setPilihanId(int pilihanId) {
        this.pilihanId = pilihanId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getPilihan() {
        return pilihan;
    }

    public void setPilihan(String pilihan) {
        this.pilihan = pilihan;
    }
}
