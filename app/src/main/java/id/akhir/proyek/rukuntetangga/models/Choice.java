package id.akhir.proyek.rukuntetangga.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Choice implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.voteId);
        dest.writeString(this.pilihan);
    }

    protected Choice(Parcel in) {
        this.id = in.readInt();
        this.voteId = in.readInt();
        this.pilihan = in.readString();
    }

    // Cukup sesuaikan nama objeknya
    public static final Parcelable.Creator<Choice> CREATOR = new Parcelable.Creator<Choice>() {
        @Override
        public Choice createFromParcel(Parcel source) {
            return new Choice(source);
        }

        @Override
        public Choice[] newArray(int size) {
            return new Choice[size];
        }
    };

    public static Creator<Choice> getCREATOR() {
        return CREATOR;
    }
}
