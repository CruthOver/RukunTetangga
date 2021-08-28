package id.akhir.proyek.rukuntetangga.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Information implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("informasi")
    private String information;

    public Information(int id, String information) {
        this.id = id;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.information);
    }

    protected Information(Parcel in) {
        this.id = in.readInt();
        this.information = in.readString();
    }

    public static final Parcelable.Creator<Information> CREATOR = new Parcelable.Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel source) {
            return new Information(source);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    public static Creator<Information> getCREATOR() {
        return CREATOR;
    }
}
