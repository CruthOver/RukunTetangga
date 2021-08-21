package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Voting {
    @SerializedName("id")
    int voteId;
    @SerializedName("pertanyaan")
    String question;
    @SerializedName("batas_waktu")
    private String timeVote;
    @SerializedName("pilihan")
    private List<Choice> choice;
//    String choiceOne, choiceTwo, choiceThree;

    public Voting(int voteId, String question, String timeVote, List<Choice> choice) {
        this.voteId = voteId;
        this.question = question;
        this.timeVote = timeVote;
        this.choice = choice;
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
}
