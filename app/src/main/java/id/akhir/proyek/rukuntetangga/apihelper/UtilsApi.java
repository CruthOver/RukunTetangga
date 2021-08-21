package id.akhir.proyek.rukuntetangga.apihelper;

public class UtilsApi {
    public static final String BASE_URL = "http://rukuntetangga.pendudukmiskinpegadentengah.com/api/";

    public static BaseApiService getApiService(){
        return RetrofitClient.getClient(BASE_URL).create(BaseApiService.class);
    }
}
