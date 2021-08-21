package id.akhir.proyek.rukuntetangga.models;

import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("id")
    int id;
    @SerializedName("nama_jasa")
    String serviceName;
    @SerializedName("gambar_jasa")
    String serviceImage;
    @SerializedName("nomor_telepon")
    String phoneNumber;

    public Service(int id, String serviceName, String serviceImage, String phoneNumber) {
        this.id = id;
        this.serviceName = serviceName;
        this.phoneNumber = phoneNumber;
        this.serviceImage = serviceImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", serviceImage=" + serviceImage +
                ", phoneNumber=" + phoneNumber +
                '}';
    }


}
