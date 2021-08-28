package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class ComplaintAction {
    @SerializedName("id")
    private int id;
    @SerializedName("laporan_id")
    private int complaintId;
    @SerializedName("deskripsi")
    private String description;
    @SerializedName("bukti")
    private String imageAction;

    public ComplaintAction(int id, int complaintId, String description, String imageAction) {
        this.id = id;
        this.complaintId = complaintId;
        this.description = description;
        this.imageAction = imageAction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageAction() {
        return imageAction;
    }

    public void setImageAction(String imageAction) {
        this.imageAction = imageAction;
    }
}
