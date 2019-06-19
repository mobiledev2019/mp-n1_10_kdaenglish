package com.kda.kdatalk.model.noti;

public class Noti {
    public String id;
    public boolean isComment = false;
    public String contentNoti; // like: xxx da binh luan ve 1 anh cua ban
    public String id_feed;
    public String user_name;
    public String user_url;
    public String time;

    public boolean isRead = false;


    public Noti(String id, boolean isComment, String contentNoti, String id_feed, String user_name, String user_url, String time, boolean isRead) {
        this.id = id;
        this.isComment = isComment;
        this.contentNoti = contentNoti;
        this.id_feed = id_feed;
        this.user_name = user_name;
        this.user_url = user_url;
        this.time = time;
        this.isRead = isRead;
    }

    public Noti() {
    }
}
