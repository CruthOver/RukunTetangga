package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Complaint {
    @SerializedName("id")
    private int complaintId;
    @SerializedName("gambar_laporan")
    private String image;
    @SerializedName("keterangan")
    private String titleComplaint;
    @SerializedName("created_at")
    private String dateComplaint;
    @SerializedName("status_laporan")
    private int statusComplaint;
    @SerializedName("user")
    private User userData;

    public Complaint(int complaintId, User userData, String image, String titleComplaint, int statusComplaint) {
        this.complaintId = complaintId;
        this.image = image;
        this.titleComplaint = titleComplaint;
        this.statusComplaint = statusComplaint;
        this.userData = userData;
    }

    public String getDateComplaint() {
        return dateComplaint;
    }

    public void setDateComplaint(String dateComplaint) {
        this.dateComplaint = dateComplaint;
    }

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitleComplaint() {
        return titleComplaint;
    }

    public void setTitleComplaint(String titleComplaint) {
        this.titleComplaint = titleComplaint;
    }

    public int getStatusComplaint() {
        return statusComplaint;
    }

    public void setStatusComplaint(int statusComplaint) {
        this.statusComplaint = statusComplaint;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "complaintId=" + complaintId +
                ", image='" + image + '\'' +
                ", titleComplaint='" + titleComplaint + '\'' +
                ", statusComplaint='" + statusComplaint + '\'' +
                ", userData='" + userData + '\'' +
                ", dateComplaint='" + dateComplaint + '\'' +
                '}';
    }
}
