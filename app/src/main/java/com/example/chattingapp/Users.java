package com.example.chattingapp;

class Users {
    private String uid;
    private String name;

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public Users(String uid, String name) {
        this.uid=uid;
        this.name=name;
    }
}
