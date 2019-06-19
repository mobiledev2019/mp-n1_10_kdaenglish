package com.kda.kdatalk.network;

import static com.kda.kdatalk.network.ServiceConst.URL_SERVER_KDA;

public class APIUtils {

    private APIUtils() {
    }

//    public static final String BASE_URL = URL_SERVER_KDA;// url server

    public static ServiceFunction getAPIService() {

        return RetrofitClient.getClient(URL_SERVER_KDA).create(ServiceFunction.class);
    }

}