package com.zxdc.utils.library.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/3/7.
 */

public class Agreement extends BaseBean {

    private AgreementBean data;

    public AgreementBean getData() {
        return data;
    }

    public void setData(AgreementBean data) {
        this.data = data;
    }

    public static class AgreementBean implements Serializable{
        private int id;
        private String title;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
