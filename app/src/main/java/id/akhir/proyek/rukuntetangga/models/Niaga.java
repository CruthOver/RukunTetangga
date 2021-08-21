package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Niaga {
    @SerializedName("id")
    private int niagaId;
    @SerializedName("nama_niaga")
    private String niagaName;
    @SerializedName("nomor_telepon")
    private String phoneNumber;
    @SerializedName("keterangan")
    private String niagaDescription;
    @SerializedName("foto_niaga")
    private String imageUrl;
    @SerializedName("user")
    private User user;

    public Niaga(int niagaId, String niagaName, String phoneNumber, String niagaDescription, String imageUrl, User user) {
        this.niagaId = niagaId;
        this.niagaName = niagaName;
        this.phoneNumber = phoneNumber;
        this.niagaDescription = niagaDescription;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNiagaId() {
        return niagaId;
    }

    public void setNiagaId(int niagaId) {
        this.niagaId = niagaId;
    }

    public String getNiagaName() {
        return niagaName;
    }

    public void setNiagaName(String niagaName) {
        this.niagaName = niagaName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNiagaDescription() {
        return niagaDescription;
    }

    public void setNiagaDescription(String niagaDescription) {
        this.niagaDescription = niagaDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Niaga{" +
                "niagaId=" + niagaId +
                ", niagaName='" + niagaName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", niagaDescription='" + niagaDescription + '\'' +
                ", imageUrl=" + imageUrl +
                ", User=" + user +
                '}';
    }
}
