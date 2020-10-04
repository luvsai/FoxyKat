package com.example.foxykat;

public class person {
    private String img , phone, msg, name,time;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public person(String phone , String img , String msg, String name , String time) {
        this.img = img;
        this.phone =  phone;
        this.msg = msg;
        this.name = name;
        this.time = time;

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




}
