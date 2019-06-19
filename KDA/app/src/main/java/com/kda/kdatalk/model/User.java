package com.kda.kdatalk.model;

public class User {


    public String _id;


    public String email;

    public String address;

    public String name;

    public String url_img_ava = "";

    public String url_img_cover = "";


    public User(String _id, String email, String address, String name, String url_img_ava) {
        this._id = _id;
        this.email = email;
        this.address = address;
        this.name = name;
        this.url_img_ava = url_img_ava;
    }


    public User(String _id, String email, String address, String name, String url_img_ava, String url_img_cover) {
        this._id = _id;
        this.email = email;
        this.address = address;
        this.name = name;
        this.url_img_ava = url_img_ava;
        this.url_img_cover = url_img_cover;
    }
    public User() {
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
