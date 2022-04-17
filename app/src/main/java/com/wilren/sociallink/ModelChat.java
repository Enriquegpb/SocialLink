package com.wilren.sociallink;

import java.util.Date;

public class ModelChat {
    private String username,text;
    private String time;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ModelChat() {
        this.username = username;
        this.text = text;
        this.time = time;
    }
}
