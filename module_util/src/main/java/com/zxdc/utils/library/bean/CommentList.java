package com.zxdc.utils.library.bean;

import java.util.List;

/**
 * Created by Administrator on 2020/3/12.
 */

public class CommentList extends BaseBean {

    private List<Comment> data;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }
}
