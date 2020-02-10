package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.user.NewsAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.view.MyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息
 * Created by Administrator on 2020/2/8.
 */

public class NewsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private NewsAdapter newsAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        newsAdapter=new NewsAdapter(this);
        listView.setDivider(null);
        listView.setAdapter(newsAdapter);
    }
}
