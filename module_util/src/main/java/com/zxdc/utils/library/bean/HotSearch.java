package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/10.
 */

public class HotSearch extends BaseBean {

    private List<HotSearchBean> data;

    public List<HotSearchBean> getData() {
        return data;
    }

    public void setData(List<HotSearchBean> data) {
        this.data = data;
    }

    public static class HotSearchBean implements Serializable{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
