package com.kda.kdatalk.network;

public class ServiceConst {
    public static final String URL_SERVER_KDA = "https://df4fb1f7.ngrok.io";
    public static final String URL_SERVER_SOCKET = "http://35.247.180.113:5000";

//    public static final String URL_SERVER_IMAGE = "https://api.abababba/";

    /*KEY PARAMS*/
    public static final String KEY_AUTHORIZATION = "Authorization";
    public static final String KEY_TOKEN = "Token";
    public static final String KEY_LANG = "Lang";
    public static final String KEY_USER_ID = "UserId";
    public static final String KEY_ACCOUNT_TOKEN = "AccountToken";

    //NEWFEED
    public static final String NEWFEED = "/feeds";
    public static final String NUM_NOTI = "/notification/num_unread";
    public static final String NOTIFICATION = "/notifications";
    public static final String READ_NOTI = "/notification/read";
    public static final String DETAIL_USER = "/user/";
    public static final String CHANGE_AVA = "/user/update_avatar";
    public static final String CHANGE_COVER = "/user/update_cover";
    public static final String VOTE_UP = "/comment/up_vote";
    public static final String VOTE_DOWN = "/comment/down_vote";
    public static final String POST_COMMENT = "/comment/create";
    public static final String DETAIL = "/feed/";
    public static final String LIKE = "/feed/like";
    public static final String ADDNEWFEED = "/feed/create";

    //LOGIN
    public static final String LOGIN = "/authenticate";

    public static final String REGISTER = "/register" ;
}
