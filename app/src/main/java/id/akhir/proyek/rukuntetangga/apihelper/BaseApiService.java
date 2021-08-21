package id.akhir.proyek.rukuntetangga.apihelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseBody> signInRequest(@Field("no_telp") String phoneNumber,
                                     @Field("password") String passwor);

    @GET("surat/tipe")
    Call<ResponseBody> getLetterType(@Header("Authorization") String authToken);

    @GET("surat")
    Call<ResponseBody> getLetter(@Header("Authorization") String authToken);

    @GET("service")
    Call<ResponseBody> getService(@Header("Authorization") String authToken,
                                     @Query("tipe_id") int serviceType);

    @GET("structure/position")
    Call<ResponseBody> getPosition(@Header("Authorization") String authToken);

    @GET("structure")
    Call<ResponseBody> getStructure(@Header("Authorization") String authToken);

    @GET("musrenbang")
    Call<ResponseBody> getMusrenbang(@Header("Authorization") String authToken);

    @GET("activities")
    Call<ResponseBody> getActivity(@Header("Authorization") String authToken);

    @GET("informasi")
    Call<ResponseBody> getInformation(@Header("Authorization") String authToken);

    @GET("report")
    Call<ResponseBody> getComplaint(@Header("Authorization") String authToken);

    @GET("jobs")
    Call<ResponseBody> getJobsUser(@Query("id") int userId,
                                   @Header("Authorization") String authToken);

    @GET("niaga")
    Call<ResponseBody> getNiaga(@Header("Authorization") String authToken);

    @GET("voting")
    Call<ResponseBody> getVoting(@Header("Authorization") String authToken);

    @GET("users")
    Call<ResponseBody> getWarga(@Header("Authorization") String authToken);

    @GET("voting/already_vote")
    Call<ResponseBody> isAlreadyVote(@Header("Authorization") String authToken,
                                     @Query("user_id") int userId,
                                     @Query("vote_id") int voteId);

    @FormUrlEncoded
    @POST("informasi")
    Call<ResponseBody> addInformation(@Header("Authorization") String authToken,
                                      @Field("information") String information);

    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> addWarga(@Header("Authorization") String authToken,
                                @Field("nama_lengkap") String name,
                                @Field("alamat") String address,
                                @Field("no_telepon") String phoneNumber,
                                @Field("nik") String nik,
                                @Field("tanggal_lahir") String birthDate,
                                @Field("tempat_lahir") String birthPlace,
                                @Field("email") String email,
                                @Field("jenis_kelamin") int gender,
                                @Field("agama") String religion,
                                @Field("status_perkawinan") String statusMarried,
                                @Field("pendidikan") String pendidikan);

    @FormUrlEncoded
    @POST("voting")
    Call<ResponseBody> addVoting(@Header("Authorization") String authToken,
                                 @Field("pertanyaan") String question,
                                 @Field("batas_waktu") String dueDate,
                                     @Field("pilihan[]") List<String> choices);

    @Multipart
    @POST("activities")
    Call<ResponseBody> addKegiatan(@Header("Authorization") String authToken,
                                   @Part("nama_kegiatan") RequestBody descriptionKegiatan,
                                   @Part("tanggal_kegiatan") RequestBody dateKegiatan,
                                   @Part("jam_kegiatan") RequestBody timeKegiatan,
                                   @Part MultipartBody.Part fotoKegiatan);

    @Multipart
    @POST("structure")
    Call<ResponseBody> addStructure(@Header("Authorization") String authToken,
                                   @Part("user_id") RequestBody userId,
                                   @Part("position_id") RequestBody positionId,
                                   @Part MultipartBody.Part fotoUser);

    @Multipart
    @POST("niaga")
    Call<ResponseBody> addNiaga(@Header("Authorization") String authToken,
                                @Part("user_id") RequestBody userId,
                                @Part("nama_niaga") RequestBody niagaName,
                                @Part("no_telepon") RequestBody phoneNumber,
                                @Part("keterangan") RequestBody niagaDescription,
                                @Part MultipartBody.Part niagaPhoto);

    @Multipart
    @POST("musrenbang")
    Call<ResponseBody> addMusrenbang(@Header("Authorization") String authToken,
                                @Part("nama") RequestBody musrenbangName,
                                @Part MultipartBody.Part niagaPhoto);

    @Multipart
    @POST("report")
    Call<ResponseBody> addLaporan(@Header("Authorization") String authToken,
                                     @Part("complaint") RequestBody complaintDescription,
                                     @Part("user_id") RequestBody userId,
                                     @Part MultipartBody.Part complaintFoto);

    @FormUrlEncoded
    @POST("surat")
    Call<ResponseBody> addSurat(@Header("Authorization") String authToken,
                                @Field("user_id") int userId,
                                @Field("date_need") String dateNeed,
                                @Field("tipe_surat_id") int typeSurat,
                                @Field("description") String description);

    @FormUrlEncoded
    @PUT("report")
    Call<ResponseBody> updateStatusLaporan(@Header("Authorization") String authToken,
                                           @Field("complaint_id") int complaintId,
                                           @Field("status_id") int statusId);

    @FormUrlEncoded
    @PUT("surat")
    Call<ResponseBody> updateStatusLetter(@Header("Authorization") String authToken,
                                           @Field("letter_id") int letterId,
                                           @Field("status_id") int statusId);
}
