package id.akhir.proyek.rukuntetangga.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Voting implements Parcelable {
    @SerializedName("id")
    int voteId;
    @SerializedName("pertanyaan")
    String question;
    @SerializedName("batas_waktu")
    private String timeVote;
    @SerializedName("batas_tanggal")
    private String dateVote;
    @SerializedName("due_date")
    private long dateTimeVote;
    @SerializedName("pilihan")
    private List<Choice> choice = new ArrayList<>();

//    String choiceOne, choiceTwo, choiceThree;

    public Voting(int voteId, String question, String timeVote, String dateVote, List<Choice> choice) {
        this.voteId = voteId;
        this.question = question;
        this.timeVote = timeVote;
        this.choice = choice;
        this.dateVote = dateVote;
    }

    public String getDateVote() {
        return dateVote;
    }

    public long getDateTimeVote() {
        return dateTimeVote;
    }

    public void setDateTimeVote(long dateTimeVote) {
        this.dateTimeVote = dateTimeVote;
    }

    public void setDateVote(String dateVote) {
        this.dateVote = dateVote;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTimeVote() {
        return timeVote;
    }

    public void setTimeVote(String timeVote) {
        this.timeVote = timeVote;
    }

    public List<Choice> getChoice() {
        return choice;
    }

    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.voteId);
        dest.writeString(this.question);
        dest.writeString(this.dateVote);
        dest.writeString(this.timeVote);
        dest.writeLong(this.dateTimeVote);
        dest.writeTypedList(this.choice);
    }

    protected Voting(Parcel in) {
        this.voteId = in.readInt();
        this.question = in.readString();
        this.dateVote = in.readString();
        this.timeVote = in.readString();
        this.dateTimeVote = in.readLong();
        in.readTypedList(choice, Choice.getCREATOR());
    }

    // Cukup sesuaikan nama objeknya
    public static final Parcelable.Creator<Voting> CREATOR = new Parcelable.Creator<Voting>() {
        @Override
        public Voting createFromParcel(Parcel source) {
            return new Voting(source);
        }

        @Override
        public Voting[] newArray(int size) {
            return new Voting[size];
        }
    };

    public static Creator<Voting> getCREATOR() {
        return CREATOR;
    }
}
