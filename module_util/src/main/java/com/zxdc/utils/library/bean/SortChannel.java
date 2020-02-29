package com.zxdc.utils.library.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/2/29.
 */

public class SortChannel implements Serializable {

    private int channelId;
    private int sort;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
