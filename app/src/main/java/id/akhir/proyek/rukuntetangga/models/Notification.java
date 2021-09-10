package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")
    int notificationId;
    @SerializedName("log_code")
    int logCode;
    @SerializedName("reference_id")
    int referenceId;
    @SerializedName("reference_name")
    String referenceName;
    @SerializedName("content")
    String contentBody;
    @SerializedName("is_read")
    boolean isRead;

    String title;
    String body;

    public Notification(int notificationId, int logCode, int referenceId, String referenceName, boolean isRead, String contentBody) {
        this.notificationId = notificationId;
        this.logCode = logCode;
        this.referenceId = referenceId;
        this.referenceName = referenceName;
        this.isRead = isRead;
        this.contentBody = contentBody;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getLogCode() {
        return logCode;
    }

    public void setLogCode(int logCode) {
        this.logCode = logCode;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setNotification() {
        switch (logCode) {
            case 1:
                title = "Status Laporan";
                body = "Laporan " +contentBody+ " sedang diproses.";
                break;
            case 2:
                title = "Status Laporan";
                body = "Laporan " +contentBody+ " sudah ditindak lanjuti.";
                break;
            case 3:
                title = "Status Surat";
                body = contentBody + " anda sedang diproses.";
                break;
            case 4:
                title = "Status Surat";
                body = contentBody + " anda sudah selesai.";
                break;
            case 5:
                title = "Voting Baru";
                body = "Ada oting baru untuk semua warga. Ikut voting sekarang!";
                break;
        }
    }
}
