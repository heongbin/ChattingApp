package com.example.chattingapp;

class ChattingListData extends ChatData{
    private String LastConversation;
    private String OpponentName;
    private String Opponentimg;

    public ChattingListData(String lastConversation, String opponentName, String opponentimg) {
        this.LastConversation = lastConversation;
        this.OpponentName = opponentName;
        this.Opponentimg = opponentimg;
    }

    public String getLastConversation() {
        return LastConversation;
    }

    public String getOpponentName() {
        return OpponentName;
    }

    public String getOpponentimg() {
        return Opponentimg;
    }
}
