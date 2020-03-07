package com.zxdc.utils.library.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/3/7.
 */

public class Version extends BaseBean {

    private VersionBean data;

    public VersionBean getData() {
        return data;
    }

    public void setData(VersionBean data) {
        this.data = data;
    }

    public static class VersionBean implements Serializable{

        private int version;
        private String url;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
