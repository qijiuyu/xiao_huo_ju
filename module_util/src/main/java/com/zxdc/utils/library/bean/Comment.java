package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/5.
 */

public class Comment extends BaseBean {

    private List<CommentBean> data;

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }

    public static class CommentBean implements Serializable{
        private int id;
        private String content;
        private String createtimestr;
        private String nickname;
        private String userImg;
        private int replyCount;
        private List<ReplyList> replyList;

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

        public int getReplyCount() {
            return replyCount;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public List<ReplyList> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<ReplyList> replyList) {
            this.replyList = replyList;
        }
    }


    public static class ReplyList implements Serializable{
        private String beNickName;
        private int id;
        private String content;
        private String createtimestr;
        private String nickname;
        private String userImg;

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
    }
}