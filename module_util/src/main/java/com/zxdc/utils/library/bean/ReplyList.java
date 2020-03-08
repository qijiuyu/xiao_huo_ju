package com.zxdc.utils.library.bean;

import java.util.List;

/**
 * Created by Administrator on 2020/3/8.
 */

public class ReplyList extends BaseBean {

    private List<Reply> data;

    public List<Reply> getData() {
        return data;
    }

    public void setData(List<Reply> data) {
        this.data = data;
    }
}
