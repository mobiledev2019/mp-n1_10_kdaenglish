package com.kda.kdatalk.model.chat;

import com.kda.kdatalk.model.User;

public class MessageModel {
    public String id;
    public User user;
    public String last_str;
    public String last_time;


    public MessageModel(String id, User user, String last_str, String last_time) {
        this.id = id;
        this.user = user;
        this.last_str = last_str;
        this.last_time = last_time;
    }

    public MessageModel() {
    }
}
