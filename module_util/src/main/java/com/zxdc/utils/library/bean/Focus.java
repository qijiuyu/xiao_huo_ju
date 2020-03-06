package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/6.
 */

public class Focus extends BaseBean {

    private List<FocusBean> data;

    public List<FocusBean> getData() {
        return data;
    }

    public void setData(List<FocusBean> data) {
        this.data = data;
    }

    public static class FocusBean implements Serializable{
        private int id;
        private String imgurl;
        private String nickname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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
    }
}
