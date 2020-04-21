package com.example.chattingapp;

class ChatData {
    private String uid;
    private String img;
    private String name;
    private String maincontent;
    private String time;

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getMaincontent() {
        return maincontent;
    }

    public String getTime() {
        return time;
    }


    public String getuid() {
        return uid;
    }
    public ChatData(){}

    public ChatData(String uid, String img, String name, String maincontent, String time){
        this.uid=uid;
        this.img=img;
        this.name=name;
        this.maincontent=maincontent;
        this.time=time;

    }

}
