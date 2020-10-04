package com.example.foxykat;

public class message {
    private int id;


    private String img;
    private String msg;
    private String name;
    private String time;
    private String phone;
    private int token , seen;

    public message(int id ,String phone, String img , String msg, String name  , int token, int seen,String time) {
        this.id = id;
        this.img = img;
        this.msg = msg;
        this.name = name;
        this.time = time;
        this.token = token;
        this.seen = seen;
        this.phone = phone;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
