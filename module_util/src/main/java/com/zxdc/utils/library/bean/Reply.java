package com.zxdc.utils.library.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/3/8.
 */

public class Reply implements Serializable {

    private String beNickName;
    private int id;
    private String content;
    private String createtimestr;
    private String nickname;
    private String userImg;
    private boolean thumbComment;
    private int thumbCount;

    public String getBeNickName() {
        return beNickName;
    }

    public void setBeNickName(String beNickName) {
        this.beNickName = beNickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetimestr() {
        return createtimestr;
    }

    public void setCreatetimestr(String createtimestr) {
        this.createtimestr = createtimestr;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public boolean isThumbComment() {
        return thumbComment;
    }

    public void setThumbComment(boolean thumbComment) {
        this.thumbComment = thumbComment;
    }

    public int getThumbCount() {
        return thumbCount;
    }

    public void setThumbCount(int thumbCount) {
        this.thumbCount = thumbCount;
    }
}
