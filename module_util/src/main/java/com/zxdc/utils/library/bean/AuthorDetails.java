package com.zxdc.utils.library.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/3/7.
 */

public class AuthorDetails extends BaseBean {

    private DetailsBean data;

    public DetailsBean getData() {
        return data;
    }

    public void setData(DetailsBean data) {
        this.data = data;
    }

    public static class DetailsBean implements Serializable{
        private String imgurl;
        private String nickname;
        private int followCount;
        private boolean followUser;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public boolean isFollowUser() {
            return followUser;
        }

        public void setFollowUser(boolean followUser) {
            this.followUser = followUser;
        }
    }
}
