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
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseBody> signInRequest(@Field("no_telp") String phoneNumber,
                                     @Field("password") String password);

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

    @GET("report/tindakan")
    Call<ResponseBody> getComplaintAction(@Header("Authorization") String authToken,
                                          @Query("complaint_id") int complaintId);

    @GET("jobs")
    Call<ResponseBody> getJobs(@Header("Authorization") String authToken);

    @GET("niaga")
    Call<ResponseBody> getNiaga(@Header("Authorization") String authToken);

    @GET("voting")
    Call<ResponseBody> getVoting(@Header("Authorization") String authToken);

    @GET("users")
    Call<ResponseBody> getWarga(@Header("Authorization") String authToken);

    @GET("users")
    Call<ResponseBody> getWargaById(@Header("Authorization") String authToken,
                                    @Query("userId") int userId);

    @GET("voting/already_vote")
    Call<ResponseBody> isAlreadyVote(@Header("Authorization") String authToken,
                                     @Query("user_id") int userId,
                                     @Query("vote_id") int voteId);

    @GET("voting/detail")
    Call<ResponseBody> getDetailVoting(@Header("Authorization") String authToken,
                                       @Query("vote_id") int voteId);

    @GET("voting/pilihan")
    Call<ResponseBody> getResultVoting(@Header("Authorization") String authToken,
                                       @Query("voting_id") int voteId);

    @GET("kas")
    Call<ResponseBody> getInfoKeuangan(@Header("Authorization") String authToken);

    @GET("kas/user")
    Call<ResponseBody> getKasUser(@Header("Authorization") String authToken,
                                  @Query("user_id") int userId);

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
                                @Field("pendidikan") String pendidikan,
                                @Field("pekerjaan") String job);

    @FormUrlEncoded
    @POST("voting")
    Call<ResponseBody> addVoting(@Header("Authorization") String authToken,
                                 @Field("pertanyaan") String question,
                                 @Field("batas_waktu") String dueDate,
                                 @Field("pilihan[]") List<String> choices);

    @FormUrlEncoded
    @POST("voting/select")
    Call<ResponseBody> addVoted(@Header("Authorization") String authToken,
                                 @Field("voting_id") int votinngId,
                                 @Field("pilihan_id") int pilihanId,
                                 @Field("user_id") int userId);

    @FormUrlEncoded
    @POST("kas/info_keuangan")
    Call<ResponseBody> addInfoKeuangan(@Header("Authorization") String authToken,
                                       @Field("saldo") String saldo,
                                       @Field("bulan_id") int bulanId,
                                       @Field("kebutuhan") String kebutuhan,
                                       @Field("pemasukan") String income,
                                       @Field("pengeluaran") String expense);

    @Multipart
    @POST("activities")
    Call<ResponseBody> addKegiatan(@Header("Authorization") String authToken,
                                   @Part("nama_kegiatan") RequestBody descriptionKegiatan,
                                   @Part("tanggal_kegiatan") RequestBody dateKegiatan,
                                   @Part("jam_kegiatan") RequestBody timeKegiatan,
                                   @Part("lokasi") RequestBody location,
                                   @Part("ditujukan") RequestBody ditujukan,
                                   @Part MultipartBody.Part fotoKegiatan);

    @Multipart
    @POST("service")
    Call<ResponseBody> addService(@Header("Authorization") String authToken,
                                   @Part("nama_jasa") RequestBody serviceName,
                                   @Part("nomor_telepon") RequestBody phoneNumber,
                                   @Part MultipartBody.Part fotoKegiatan);

    @Multipart
    @POST("activities/update")
    Call<ResponseBody> updateKegiatan(@Header("Authorization") String authToken,
                                      @Part("activity_id") RequestBody activityId,
                                      @Part("nama_kegiatan") RequestBody descriptionKegiatan,
                                      @Part("tanggal_kegiatan") RequestBody dateKegiatan,
                                      @Part("jam_kegiatan") RequestBody timeKegiatan,
                                      @Part("lokasi") RequestBody location,
                                      @Part("ditujukan") RequestBody ditujukan,
                                      @Part MultipartBody.Part fotoKegiatan);

    @Multipart
    @POST("structure")
    Call<ResponseBody> addStructure(@Header("Authorization") String authToken,
                                   @Part("user_id") RequestBody userId,
                                   @Part("position_id") RequestBody positionId,
                                   @Part MultipartBody.Part fotoUser);

    @Multipart
    @POST("jobs")
    Call<ResponseBody> addJob(@Header("Authorization") String authToken,
                                    @Part("user_id") RequestBody userId,
                                    @Part("job_name") RequestBody jobName,
                                    @Part MultipartBody.Part fotoPekerjaan);

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

    @Multipart
    @POST("report/tindakan")
    Call<ResponseBody> tindakLanjut(@Header("Authorization") String authToken,
                                     @Part("laporan_id") RequestBody complaintId,
                                     @Part("description") RequestBody description,
                                     @Part MultipartBody.Part imageAction);

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
    @PUT("informasi")
    Call<ResponseBody> updateInformation(@Header("Authorization") String authToken,
                                         @Field("id") int informationId,
                                         @Field("information") String information);

    @FormUrlEncoded
    @PUT("surat")
    Call<ResponseBody> updateStatusLetter(@Header("Authorization") String authToken,
                                           @Field("letter_id") int letterId,
                                           @Field("status_id") int statusId);

    @Multipart
    @POST("users/user_data")
    Call<ResponseBody> uploadFotoKtp(@Header("Authorization") String authToken,
                                     @Part("user_id") RequestBody userId,
                                     @Part("type") RequestBody type,
                                     @Part MultipartBody.Part imageKTP);

    @Multipart
    @POST("users/user_data")
    Call<ResponseBody> uploadFotoKk(@Header("Authorization") String authToken,
                                    @Part("user_id") RequestBody userId,
                                    @Part("type") RequestBody type,
                                    @Part MultipartBody.Part imageKK);

    @FormUrlEncoded
    @PUT("users")
    Call<ResponseBody> updateProfile(@Header("Authorization") String authToken,
                                     @Field("userId") int userId,
                                     @Field("nama_lengkap") String fullName,
                                     @Field("email") String email,
                                     @Field("tanggal_lahir") String birthDate,
                                     @Field("tempat_lahir") String birthPlace,
                                     @Field("nik") String nik,
                                     @Field("alamat") String address,
                                     @Field("no_telepon") String phoneNumber,
                                     @Field("jenis_kelamin_id") int gender,
                                     @Field("agama") String religion,
                                     @Field("pendidikan") String pendidikan,
                                     @Field("status_perkawinan") String statusMarried,
                                     @Field("pekerjaan") String job);

    @FormUrlEncoded
    @PUT("voting")
    Call<ResponseBody> updateVoting(@Header("Authorization") String authToken,
                                    @Field("vote_id") int voteId,
                                    @Field("pertanyaan") String question,
                                    @Field("batas_waktu") String dueDate,
                                    @Field("pilihan[]") List<String> choices);

    @DELETE("users/{id}")
    Call<ResponseBody> deleteUser(@Header("Authorization") String authToken,
                                  @Path("id") int id);

    @DELETE("surat/{id}")
    Call<ResponseBody> deleteSurat(@Header("Authorization") String authToken,
                                  @Path("id") int id);

    @DELETE("voting/{id}")
    Call<ResponseBody> deleteVoting(@Header("Authorization") String authToken,
                                    @Path("id") int id);

    @DELETE("activities/{id}")
    Call<ResponseBody> deleteActivity(@Header("Authorization") String authToken,
                                    @Path("id") int id);

    @DELETE("informasi/{id}")
    Call<ResponseBody> deleteInformasi(@Header("Authorization") String authToken,
                                      @Path("id") int id);
}
