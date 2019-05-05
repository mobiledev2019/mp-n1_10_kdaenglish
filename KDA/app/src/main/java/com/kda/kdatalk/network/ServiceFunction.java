package com.kda.kdatalk.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceFunction {

    @FormUrlEncoded
    @POST(ServiceConst.NEWFEED)
    Call<String> getNewFeed(@Field("data") String data, @Field("iv") String iv);

    @FormUrlEncoded
    @POST(ServiceConst.NEWFEED)
    Call<String> getDetail(@Field("data") String data, @Field("iv") String iv);

//    @FormUrlEncoded
//    @POST(ServiceConst.ACCOUNT_LOGIN_PASSWORD_EMAIL)
//    Call<String> loginPasswordEmail(@Field("data") String data, @Field("iv") String iv);
//
//    @FormUrlEncoded
//    @POST(ServiceConst.NEW_FEED)
//    Call<String> getnewfeed(@Field("data") String data, @Field("iv") String iv);
//
//    @FormUrlEncoded
//    @POST(ServiceConst.NOTIFICATION)
//    Call<String> getNotification(@Field("data") String data, @Field("iv") String iv);
//
//
//    @FormUrlEncoded
//    @POST(ServiceConst.UPLOAD_IMAGE)
//    Call<String> post_UploadImage(@Field("data") String data, @Field("iv") String iv);
//
//    @FormUrlEncoded
//    @POST(ServiceConst.UPLOAD_VIDEO)
//    Call<String> post_UpLoadVideo(@Field("data") String data, @Field("iv") String iv);
//
//    @Multipart
//    @POST(ServiceConst.UPLOAD_VIDEO_MULTI)
//    Call<String> post_UpLoadVideoMulti(@Part("data") RequestBody data, @Part MultipartBody.Part video);


}
