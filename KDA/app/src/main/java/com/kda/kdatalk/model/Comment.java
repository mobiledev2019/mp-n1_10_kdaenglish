package com.kda.kdatalk.model;

public class Comment {
    public String id;
    public String content;
    public String err_text; // html
    public String corr_text; // html
    public User user;
    public String timme;

    public boolean is_up = false;
    public boolean is_down = false;

    public Comment(String id, String content, String err_text, String corr_text, User user, String timme) {
        this.id = id;
        this.content = content;
        this.err_text = err_text;
        this.corr_text = corr_text;
        this.user = user;
        this.timme = timme;
    }

    public Comment() {
    }
}
