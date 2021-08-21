package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Letter {
    @SerializedName("id")
    private int letterId;
    @SerializedName("status_surat")
    private int statusLetter;
    @SerializedName("tipe_surat")
    private String letterType;
    @SerializedName("keterangan")
    private String description;
    @SerializedName("tanggal_dibutuhkan")
    private String dateNeeded;
    @SerializedName("user")
    private User user;

    public Letter(int letterId, int statusLetter, String letterType, String description, String dateNeeded, User user) {
        this.letterId = letterId;
        this.letterType = letterType;
        this.description = description;
        this.dateNeeded = dateNeeded;
        this.user = user;
        this.statusLetter = statusLetter;
    }

    public Letter() {
    }

    public int getStatusLetter() {
        return statusLetter;
    }

    public void setStatusLetter(int statusLetter) {
        this.statusLetter = statusLetter;
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public String getLetterType() {
        return letterType;
    }

    public void setLetterType(String letterType) {
        this.letterType = letterType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateNeeded() {
        return dateNeeded;
    }

    public void setDateNeeded(String dateNeeded) {
        this.dateNeeded = dateNeeded;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "letterId=" + letterId +
                ", letterType='" + letterType + '\'' +
                ", description='" + description + '\'' +
                ", dateNeeded='" + dateNeeded + '\'' +
                ", user=" + user +
                '}';
    }
}
