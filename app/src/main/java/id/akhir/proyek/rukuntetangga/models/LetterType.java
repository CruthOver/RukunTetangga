package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class LetterType {
    @SerializedName("id")
    int id;
    @SerializedName("tipe_surat")
    String letterName;

    public LetterType(int id, String letterName) {
        this.id = id;
        this.letterName = letterName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLetterName() {
        return letterName;
    }

    public void setLetterName(String letterName) {
        this.letterName = letterName;
    }

    @Override
    public String toString() {
        return letterName;
    }
}
