package com.kda.kdatalk.model.chat;

import com.kda.kdatalk.model.User;

public class ChatModel {
    public String id;
    public String id_user;
    public String content;

    public boolean isSuccess;
    public boolean isSending = false;

    public ChatModel(String id, String id_user, String content) {
        this.id = id;
        this.id_user = id_user;
        this.content = content;
    }

    public ChatModel() {
    }
}
