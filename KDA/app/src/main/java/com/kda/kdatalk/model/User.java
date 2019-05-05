package com.kda.kdatalk.model;

public class User {

    public int id;

    public String str_id;

    public String accessToken;

    public String email;

    public String address;

    public String name;

    public String url_img_ava = "";

    public String url_img_cover = "";

    public String LOGIN_MODE;

    public User(int id, String str_id, String accessToken, String email, String address, String name, String url_img_ava, String LOGIN_MODE) {
        this.id = id;
        this.str_id = str_id;
        this.accessToken = accessToken;
        this.email = email;
        this.address = address;
        this.name = name;
        this.url_img_ava = url_img_ava;
        this.LOGIN_MODE = LOGIN_MODE;
    }

    public User() {
    }

    public String getLOGIN_MODE() {
        return LOGIN_MODE;
    }

    public void setLOGIN_MODE(String LOGIN_MODE) {
        this.LOGIN_MODE = LOGIN_MODE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_img_ava() {
        return url_img_ava;
    }

    public void setUrl_img_ava(String url_img_ava) {
        this.url_img_ava = url_img_ava;
    }
}
