package com.kda.kdatalk.model.chat;

import com.kda.kdatalk.model.User;

public class ChatModel {
    public String id;
    public User user;
    public User user_2;
    public String content;
    public boolean isMe;

    public ChatModel(String id, User user, String content, boolean isMe) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.isMe = isMe;
    }

    public ChatModel() {
    }
}
