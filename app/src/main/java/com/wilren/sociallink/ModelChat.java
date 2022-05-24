package com.wilren.sociallink;

import java.util.Date;

public class ModelChat {
    private String userid, text,uri;

    public ModelChat(String userid, String text, String uri, Date time) {
        this.userid = userid;
        this.text = text;
        this.uri = uri;
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    private Date time;

    public ModelChat(String userid, String text, Date time) {
        this.userid = userid;
        this.text = text;
        this.time = time;
    }

    public ModelChat() {

    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public void setTime(Date time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
