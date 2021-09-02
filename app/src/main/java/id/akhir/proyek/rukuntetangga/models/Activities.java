package id.akhir.proyek.rukuntetangga.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Activities implements Parcelable {
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
    @SerializedName("lokasi")
    private String location;

    public Activities(int activityId, String imageActivity, String titleActivity, String dateActivity, String hour, String location) {
        this.activityId = activityId;
        this.imageActivity = imageActivity;
        this.titleActivity = titleActivity;
        this.dateActivity = dateActivity;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.activityId);
        dest.writeString(this.titleActivity);
        dest.writeString(this.dateActivity);
        dest.writeString(this.hour);
        dest.writeString(this.imageActivity);
        dest.writeString(this.location);
    }

    protected Activities(Parcel in) {
        this.activityId = in.readInt();
        this.titleActivity = in.readString();
        this.dateActivity = in.readString();
        this.hour = in.readString();
        this.imageActivity = in.readString();
        this.location = in.readString();
    }

    // Cukup sesuaikan nama objeknya
    public static final Parcelable.Creator<Activities> CREATOR = new Parcelable.Creator<Activities>() {
        @Override
        public Activities createFromParcel(Parcel source) {
            return new Activities(source);
        }

        @Override
        public Activities[] newArray(int size) {
            return new Activities[size];
        }
    };

    public static Creator<Activities> getCREATOR() {
        return CREATOR;
    }
}
