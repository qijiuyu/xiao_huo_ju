package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/7.
 */

public class Help extends BaseBean {

    private List<HelpBean> data;

    public List<HelpBean> getData() {
        return data;
    }

    public void setData(List<HelpBean> data) {
        this.data = data;
    }

    public static class HelpBean implements Serializable{
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
