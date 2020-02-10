package com.ylean.soft.lfd.utils.channel;

public class DataBean {
    String name;
    int page;
    String url;

    public DataBean(String name, int page, String url) {
        this.name = name;
        this.page = page;
        this.url = url;
    }

    @Override
    public String toString() {
        return name;
    }
}
