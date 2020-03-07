package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/7.
 */

public class SerialVideo extends BaseBean {

    private List<SerialVideoBean> data;

    public List<SerialVideoBean> getData() {
        return data;
    }

    public void setData(List<SerialVideoBean> data) {
        this.data = data;
    }

    public static class SerialVideoBean implements Serializable{
        private int id;
        private String imgurl;
        private String name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
