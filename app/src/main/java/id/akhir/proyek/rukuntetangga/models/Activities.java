package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Activities {
    @SerializedName("id")
    private int activityId;
    @SerializedName("foto_kegiatan")
    private String imageActivity;
    @SerializedName("nama_kegiatan")
    private String titleActivity;
    @SerializedName("tanggal_kegiatan")
    private String dateActivity;
    @SerializedName("jam_kegiatan")
    private String hour;

    public Activities(int activityId, String imageActivity, String titleActivity, String dateActivity, String hour) {
        this.activityId = activityId;
        this.imageActivity = imageActivity;
        this.titleActivity = titleActivity;
        this.dateActivity = dateActivity;
        this.hour = hour;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getImageActivity() {
        return imageActivity;
    }

    public void setImageActivity(String imageActivity) {
        this.imageActivity = imageActivity;
    }

    public String getTitleActivity() {
        return titleActivity;
    }

    public void setTitleActivity(String titleActivity) {
        this.titleActivity = titleActivity;
    }

    public String getDateActivity() {
        return dateActivity;
    }

    public void setDateActivity(String dateActivity) {
        this.dateActivity = dateActivity;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "Activities{" +
                "activityId=" + activityId +
                ", imageActivity='" + imageActivity + '\'' +
                ", titleActivity='" + titleActivity + '\'' +
                ", dateActivity=" + dateActivity +
                ", hour=" + hour +
                '}';
    }
}
