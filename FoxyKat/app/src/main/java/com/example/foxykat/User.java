package com.example.foxykat;
public  class User {
    String isActive ;
    String sMsg ;
    String uName;
    String email,mob;
    public User () {}
    public User(String isActive ,String sMsg,String uName, String email,String mob) {
        this.isActive = isActive ;
        this.sMsg = sMsg;
        this.uName = uName;
        this.email = email;
        this.mob = mob;

    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getsMsg() {
        return sMsg;
    }

    public void setsMsg(String sMsg) {
        this.sMsg = sMsg;
    }

    public String getuName() {
        return uName;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}