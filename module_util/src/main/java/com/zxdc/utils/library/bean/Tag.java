package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/2/28.
 */

public class Tag extends BaseBean {

    private List<TagBean> data;

    public List<TagBean> getData() {
        return data;
    }

    public void setData(List<TagBean> data) {
        this.data = data;
    }

    public static class TagBean implements Serializable{

        private String imgurl;
        private String name;
        private int id;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
