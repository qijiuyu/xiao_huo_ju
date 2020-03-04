package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/4.
 */

public class Screen extends BaseBean {

    private List<ScreenBean> data;

    public List<ScreenBean> getData() {
        return data;
    }

    public void setData(List<ScreenBean> data) {
        this.data = data;
    }

    public static class ScreenBean implements Serializable{
        private String content;
        private String nickname;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
