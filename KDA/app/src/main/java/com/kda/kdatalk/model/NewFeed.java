package com.kda.kdatalk.model;

import java.util.ArrayList;
import java.util.List;

public class NewFeed {

    public String id;

    public String content;

    public String user_name;

    public String user_url;

    public ArrayList<String> list_image;

    public String create_at;

    public int num_like;

    public int num_comment;

    public boolean isLike;

    public ArrayList<Comment> list_comment;

    public NewFeed() {
    }

    public NewFeed(String id, String content, String user_name, String user_url, ArrayList<String> list_image, String create_at, int num_like, int num_comment, boolean isLike, ArrayList<Comment> list_comment) {
        this.id = id;
        this.content = content;
        this.user_name = user_name;
        this.user_url = user_url;
        this.list_image = list_image;
        this.create_at = create_at;
        this.num_like = num_like;
        this.num_comment = num_comment;
        this.isLike = isLike;
        this.list_comment = list_comment;
    }
}
