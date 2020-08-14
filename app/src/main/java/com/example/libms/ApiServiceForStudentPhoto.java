package com.example.libms;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServiceForStudentPhoto {
    String BASE_URL = "http://"+ConstantValues.ipaddress+"/LibMs/upload-stdphoto/";

    @Multipart
    @POST("upload.php")
    Call<ResponseBody> uploadMultiple(
            @Part("description") RequestBody description,
            @Part("size") RequestBody size,
            @Part("sidforimage") RequestBody uuid,
            @Part List<MultipartBody.Part> files);
}
