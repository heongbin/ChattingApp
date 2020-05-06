package com.example.chattingapp;

class UserDataWithKey {
    private String ChattingRoomKey;
    private String OpponentName;

    public String getChattingRoomKey() {
        return ChattingRoomKey;
    }

    public String getOpponentName() {
        return OpponentName;
    }

    public UserDataWithKey(String CRK, String ON) {
        this.ChattingRoomKey = CRK;
        this.OpponentName = ON;
    }
}
