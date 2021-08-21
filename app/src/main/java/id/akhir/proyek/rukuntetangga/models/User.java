package id.akhir.proyek.rukuntetangga.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    @SerializedName("id")
    private int userId;

    @SerializedName("nik")
    private String nikWarga;

    @SerializedName("nomor_telepon")
    private String phoneNumber;

    @SerializedName("alamat_domisili")
    private String currentAddress;

    @SerializedName("alamat")
    private String address;

    @SerializedName("email")
    private String email;

    @SerializedName("jenis_kelamin_id")
    private String gender;

    @SerializedName("tanggal_lahir")
    private String dateBirth;

    @SerializedName("tempat_lahir")
    private String birthPlace;

    @SerializedName("token")
    private String authToken;

    @SerializedName("nama_lengkap")
    private String fullName;

    @SerializedName("agama")
    private String agama;

    @SerializedName("pendidikan")
    private String pendidikan;

    @SerializedName("status_perkawinan")
    private String statusPerkawinan;

    @SerializedName("pekerjaan")
    private String pekerjaan;

    @SerializedName("is_admin")
    private boolean isAdmin;

    public User(int userId, String fullName, String phoneNumber, String currentAddress, String email, String gender,
                String dateBirth, String authToken, boolean isAdmin, String agama, String pendidikan, String statusPerkawinan,
                String nikWarga, String pekerjaan, String address, String birthPlace) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.currentAddress = currentAddress;
        this.email = email;
        this.gender = gender;
        this.dateBirth = dateBirth;
        this.authToken = authToken;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
        this.agama = agama;
        this.pendidikan = pendidikan;
        this.statusPerkawinan = statusPerkawinan;
        this.nikWarga = nikWarga;
        this.pekerjaan = pekerjaan;
        this.address = address;
        this.birthPlace = birthPlace;
    }

    public User(String fullName, String phoneNumber, String email, String gender,
                String authToken, boolean isAdmin) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.authToken = authToken;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }

    public User() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getStatusPerkawinan() {
        return statusPerkawinan;
    }

    public void setStatusPerkawinan(String statusPerkawinan) {
        this.statusPerkawinan = statusPerkawinan;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNikWarga() {
        return nikWarga;
    }

    public void setNikWarga(String nikWarga) {
        this.nikWarga = nikWarga;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.fullName);
        dest.writeString(this.currentAddress);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.dateBirth);
        dest.writeString(this.agama);
        dest.writeString(this.pekerjaan);
        dest.writeString(this.statusPerkawinan);
        dest.writeString(this.nikWarga);
        dest.writeString(this.birthPlace);
        dest.writeString(this.pendidikan);
    }

    protected User(Parcel in) {
        this.userId = in.readInt();
        this.fullName = in.readString();
        this.currentAddress = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.dateBirth = in.readString();
        this.agama = in.readString();
        this.pekerjaan = in.readString();
        this.statusPerkawinan = in.readString();
        this.nikWarga = in.readString();
        this.birthPlace = in.readString();
        this.pendidikan = in.readString();
    }

    // Cukup sesuaikan nama objeknya
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }
}
