package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Musrenbang {
    @SerializedName("nama")
    String fileName;
    @SerializedName("ukuran_berkas")
    int fileSize;
    @SerializedName("url_berkas")
    String urlBerkas;
    @SerializedName("format_berkas")
    String fileExtension;

    public Musrenbang(String fileName, int fileSize, String urlBerkas, String fileExtension) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.urlBerkas = urlBerkas;
        this.fileExtension = fileExtension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrlBerkas() {
        return urlBerkas;
    }

    public void setUrlBerkas(String urlBerkas) {
        this.urlBerkas = urlBerkas;
    }

    public String getFileExtention() {
        return fileExtension;
    }

    public void setFileExtention(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
