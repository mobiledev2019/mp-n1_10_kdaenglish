package com.kda.kdatalk.model;

public class Comment {
    public String id;
    public String content;
    public String err_text; // html
    public String corr_text; // html
    public User user;
    public String timme;
    public int num_vote;

    public int rank;

    public boolean is_up ;
    public boolean is_down ;

    public Comment(String id, String content, String err_text, String corr_text, User user, String timme, int num_vote, boolean is_up, boolean is_down) {
        this.id = id;
        this.content = content;
        this.err_text = err_text;
        this.corr_text = corr_text;
        this.user = user;
        this.timme = timme;
        this.num_vote = num_vote;
        this.is_up = is_up;
        this.is_down = is_down;
        this.rank = 5;
    }

    public Comment() {
    }
}
