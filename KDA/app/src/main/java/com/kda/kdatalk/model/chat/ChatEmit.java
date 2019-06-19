package com.kda.kdatalk.model.chat;

public class ChatEmit {
    public String type;
    public String detail;
    public String auth;
    public String roomId;

    public ChatEmit(String type, String detail, String auth, String roomId) {
        this.type = type;
        this.detail = detail;
        this.auth = auth;
        this.roomId = roomId;
    }
}
