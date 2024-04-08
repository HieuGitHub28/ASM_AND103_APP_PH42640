package com.example.asm_ph42640.Api;

import com.example.asm_ph42640.Modal.Response;
import com.example.asm_ph42640.Modal.SinhVien;
import com.example.asm_ph42640.Modal.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    
    String DOMAIN = "http://192.168.90.51:3000/api/";

    ApiService apiService  = new Retrofit.Builder()
            .baseUrl(ApiService.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);
    @GET("students")
    Call<Response<List<SinhVien>>> getData();

    @Multipart
    @POST("students/add")
    Call<Response<SinhVien>> addStudent(
            @Part("masv") RequestBody masv,
            @Part("name") RequestBody name,
            @Part("point") RequestBody point,
            @Part MultipartBody.Part avatar);

    @DELETE("students/delete/{id}")
    Call<Response<SinhVien>> deleteStudent(@Path("id") String idStudent);

    @Multipart
    @PUT("students/update/{id}")
    Call<Response<SinhVien>> updateStudent(
            @Path("id") String idStudent,
            @Part("masv") RequestBody masv,
            @Part("name") RequestBody name,
            @Part("point") RequestBody point,
            @Part MultipartBody.Part avatar);

    @Multipart
    @POST("register")
    Call<Response<User>> registerUser(@Part("username") RequestBody username,
                            @Part("password") RequestBody password,
                            @Part("email") RequestBody email,
                            @Part("fullname") RequestBody fullname,
                            @Part MultipartBody.Part avatar );

    @POST("login")
    Call<Response<User>> loginUser(@Body User user);

    @GET("search")
    Call<Response<List<SinhVien>>> searchStudent(@Query("key") String key);

    @GET("sort")
    Call<Response<List<SinhVien>>> sortStudent(@Query("type") Integer type);
}
