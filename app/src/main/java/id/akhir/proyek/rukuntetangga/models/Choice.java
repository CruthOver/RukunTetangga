package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Choice {
    @SerializedName("id")
    private int id;
    @SerializedName("voting_Id")
    private int voteId;
    @SerializedName("pilihan")
    private String pilihan;


    public Choice(int id, int voteId, String pilihan) {
        this.id = id;
        this.voteId = voteId;
        this.pilihan = pilihan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getPilihan() {
        return pilihan;
    }

    public void setPilihan(String pilihan) {
        this.pilihan = pilihan;
    }
}
