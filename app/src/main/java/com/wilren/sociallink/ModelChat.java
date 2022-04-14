package com.wilren.sociallink;

import java.util.Date;

public class ModelChat {
    private String username,text;
    private Date time;


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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ModelChat(String username, String text, Date time) {
        this.username = username;
        this.text = text;
        this.time = time;
    }
}
