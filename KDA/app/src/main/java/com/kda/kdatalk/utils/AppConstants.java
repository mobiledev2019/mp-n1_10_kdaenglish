package com.kda.kdatalk.utils;

public final class AppConstants {

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final String NEW_MSG = "NEW_MSG";
    public static final String NEWCHAT = "NEWCHAT";
    public static final String RELOAD = "RELOAD";

    public interface SocketEvent {

        String newMessage = "NEW_MESSAGE";
        String addMessage = "addMessage";
        String newNotification = "NEW_NOTIFICATION";
        String connected = "connected";
        String disconnect = "disconnect";
        String invalidToken = "invalidToken";

    }

    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
