package com.example.chattingapp;

public class ChatData {
    private String chatkey;
    private String namea;
    private String nameb;
    private String uid;
    private String img;
    private String email;
    private String name;
    private String maincontent;
    private String time;
    private int viewtype;
    public String getEmail(){return email;}
    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
    public String getKey(){return chatkey;}
    public String getMaincontent() {
        return maincontent;
    }

    public void setTime(String time) {
        this.time=time;
    }
    public void setMaincontent(String maincontent) {
        this.maincontent = maincontent;
    }

    public String getTime() {
        return time;
    }


    public String getuid() {
        return uid;
    }

    public int getViewtype(){
        return viewtype;
    }
    public String getnamea(){return namea;}
    public String getnameb(){return nameb;}
    public ChatData(){}

    public ChatData(String img,String name, String lastcomment,int viewtype,String time){
        this.img=img;
        this.name=name;
        this.maincontent=lastcomment;
        this.viewtype=viewtype;
        this.time=time;

    }
    public ChatData(String chatkey,String namea,String nameb){ //채팅방 당 생성된 고유키와 상대이름과 내이름담은 생성자.
        this.chatkey=chatkey;
        this.namea=namea;
        this.nameb=nameb;

    }
    public ChatData(String namea,String nameb){
        this.namea = namea;
        this.nameb = nameb;

    }

    public ChatData(String name,String uid,String img,String Email,int viewtype){
        this.uid = uid;
        this.img = img;
        this.name = name;
        this.viewtype = viewtype;
        this.email = Email;
    }

    public ChatData(String uid, String img, String name, String maincontent, String time){
        this.uid=uid;
        this.img=img;
        this.name=name;
        this.maincontent=maincontent;
        this.time=time;

    }


}
