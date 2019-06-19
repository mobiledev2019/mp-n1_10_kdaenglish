package com.kda.kdatalk.network;

import com.kda.kdatalk.utils.DraffKey;
import com.kda.kdatalk.utils.MyCache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {



            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
//        String accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
//
//        if (accessToken != null) {
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request().newBuilder().addHeader("x-access-token", accessToken).build();
//                    return chain.proceed(request);
//                }
//            });
//
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient.build())
//                    .build();
//        }

        return retrofit;
    }

//    public static void addHeaderRetrofit(String baseUrl) {
//
//        String accessToken = MyCache.getInstance().getString(DraffKey.accessToken);
//
//
//        if (accessToken != null) {
//            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//            httpClient.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request request = chain.request().newBuilder().addHeader("x-access-token", accessToken).build();
//                    return chain.proceed(request);
//                }
//            });
//
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(httpClient.build())
//                    .build();
//        }
//
//    }

}