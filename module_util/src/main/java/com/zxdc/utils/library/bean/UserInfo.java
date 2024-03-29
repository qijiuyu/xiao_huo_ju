package com.zxdc.utils.library.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/2/22.
 */

public class UserInfo extends BaseBean{

    private UserBean data;

    public UserBean getData() {
        return data;
    }

    public void setData(UserBean data) {
        this.data = data;
    }

    public static class UserBean implements Serializable{

        private int id;
        private String birthday;
        private String code;
        private int constellation;
        private int fansCount;
        private String fansCountDesc;
        private String followCountDesc;
        private int followCount;
        private String imgurl;
        private String introduction;
        private String mobile;
        private String nickname;
        private int sex;
        private int type;
        private boolean bindQq;
        private boolean bindWx;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getConstellation() {
            return constellation;
        }

        public void setConstellation(int constellation) {
            this.constellation = constellation;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getFansCount() {
            return fansCount;
        }

        public void setFansCount(int fansCount) {
            this.fansCount = fansCount;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }

        public boolean isBindQq() {
            return bindQq;
        }

        public void setBindQq(boolean bindQq) {
            this.bindQq = bindQq;
        }

        public boolean isBindWx() {
            return bindWx;
        }

        public void setBindWx(boolean bindWx) {
            this.bindWx = bindWx;
        }

        public String getFollowCountDesc() {
            return followCountDesc;
        }

        public void setFollowCountDesc(String followCountDesc) {
            this.followCountDesc = followCountDesc;
        }

        public String getFansCountDesc() {
            return fansCountDesc;
        }

        public void setFansCountDesc(String fansCountDesc) {
            this.fansCountDesc = fansCountDesc;
        }
    }
}
