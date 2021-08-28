package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Jobs {
    @SerializedName("id")
    private int id;
    @SerializedName("nama_pekerjaan")
    private String jobName;
    @SerializedName("foto_pekerjaan")
    private String image;
    private boolean isExpand;

    public Jobs(int id, String jobName, String image, boolean isExpand) {
        this.id = id;
        this.jobName = jobName;
        this.image = image;
        this.isExpand = isExpand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
