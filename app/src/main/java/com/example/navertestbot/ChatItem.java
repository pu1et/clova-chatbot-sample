package com.example.navertestbot;

public class ChatItem {
    private String name; // user name
    private String msg; // chat message
    private String time; // chat time
    private int viewType; // left, right

    public ChatItem(String name, String msg, String time, int viewType){
        this.name = name;
        this.msg = msg;
        this.time = time;
        this.viewType = viewType;
    }

    public String getMsg(){ return this.msg; }
    public String getName() { return this.name; }
    public String getTime() { return this.time; }
    public int getViewType() { return this.viewType; }

}
