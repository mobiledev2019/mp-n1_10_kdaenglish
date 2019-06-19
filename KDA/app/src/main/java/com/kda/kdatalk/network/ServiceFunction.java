package com.kda.kdatalk.network;

import org.json.JSONObject;

import java.lang.reflect.Parameter;
import java.net.URLEncoder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ServiceFunction {

    @GET(ServiceConst.NEWFEED)
    Call<String> getNewFeed(@Header("x-access-token") String token, @Query("page") String page, @Query("limit") String limit);


    @GET(ServiceConst.NOTIFICATION)
    Call<String> getNotification(@Header("x-access-token") String token, @Query("page") String page, @Query("limit") String limit);

    @GET
    Call<String> getDetail(@Header("x-access-token") String token, @Url String url);

    @POST(ServiceConst.LOGIN)
    Call<String> login(@Body RequestBody data);


    @GET(ServiceConst.NUM_NOTI)
    Call<String> getNumNoti(@Header("x-access-token") String token);



    //@Header("x-access-token") String token,
    @POST(ServiceConst.REGISTER)
    Call<String> register(@Body RequestBody data);

    @POST(ServiceConst.LIKE)
    Call<String> actionLike(@Header("x-access-token") String token, @Body RequestBody data);

    @POST(ServiceConst.ADDNEWFEED)
    Call<String> addNewFeed(@Header("x-access-token") String token, @Body RequestBody data);

    @POST(ServiceConst.POST_COMMENT)
    Call<String> postComment(@Header("x-access-token") String token, @Body RequestBody data);


    @POST(ServiceConst.CHANGE_AVA)
    Call<String> changeAva(@Header("x-access-token") String token, @Body RequestBody data);


    @POST(ServiceConst.CHANGE_COVER)
    Call<String> changeCover(@Header("x-access-token") String token, @Body RequestBody data);


    @GET
    Call<String> getDetailUser(@Header("x-access-token") String token, @Url String url);


    @FormUrlEncoded
    @POST(ServiceConst.NEWFEED)
    Call<String> getLearnModel(@Header("x-access-token") String token, @Body RequestBody data);


    @POST(ServiceConst.NEWFEED)
    Call<String> getListVocab(@Header("x-access-token") String token, @Body RequestBody data);


    @FormUrlEncoded
    @POST(ServiceConst.NEWFEED)
    Call<String> sendVoiceVocab(@Header("x-access-token") String token, @Body RequestBody data);


    @POST(ServiceConst.READ_NOTI)
    Call<String> readNoti(@Header("x-access-token") String token, @Body RequestBody  data);


//    @GET
//    Call<String> getListContact(@Header("x-access-token") String token, @Url String url);
    @GET
    Call<String> getListContact(@Url String url);


    @GET
    Call<String> getListChat(@Url String url);


    @POST(ServiceConst.VOTE_UP)
    Call<String> voteUpComment(@Header("x-access-token") String token, @Body RequestBody data);


    @POST(ServiceConst.VOTE_DOWN)
    Call<String> voteDownComment(@Header("x-access-token") String token, @Body RequestBody data);


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
