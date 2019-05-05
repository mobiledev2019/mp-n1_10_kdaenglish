package com.kda.kdatalk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyGson {

    static Gson mGson;


    public static Gson getInstance() {
        if (mGson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();

            mGson = gsonBuilder.create();
        }
        return mGson;
    }

}