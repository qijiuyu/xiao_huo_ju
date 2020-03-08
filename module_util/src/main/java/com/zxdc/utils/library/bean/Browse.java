package com.zxdc.utils.library.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/8.
 */

public class Browse extends BaseBean {

    private List<BrowseBean> data;

    public List<BrowseBean> getData() {
        return data;
    }

    public void setData(List<BrowseBean> data) {
        this.data = data;
    }

    public static class BrowseBean implements Serializable{
        private int episodeId;
        private String episodeName;
        private int seconds;
        private int serialId;
        private String serialImg;
        private String serialName;

        public int getEpisodeId() {
            return episodeId;
        }

        public void setEpisodeId(int episodeId) {
            this.episodeId = episodeId;
        }

        public String getEpisodeName() {
            return episodeName;
        }

        public void setEpisodeName(String episodeName) {
            this.episodeName = episodeName;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public int getSerialId() {
            return serialId;
        }

        public void setSerialId(int serialId) {
            this.serialId = serialId;
        }

        public String getSerialImg() {
            return serialImg;
        }

        public void setSerialImg(String serialImg) {
            this.serialImg = serialImg;
        }

        public String getSerialName() {
            return serialName;
        }

        public void setSerialName(String serialName) {
            this.serialName = serialName;
        }
    }
}
