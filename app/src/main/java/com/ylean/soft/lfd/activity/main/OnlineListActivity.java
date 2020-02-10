package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.OnlineListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.view.MyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 即将上线
 * Created by Administrator on 2020/2/7.
 */

public class OnlineListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private OnlineListAdapter onlineListAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_list);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("即将上线");

        listView.setDivider(null);
        onlineListAdapter=new OnlineListAdapter(this);
        listView.setAdapter(onlineListAdapter);
    }
}
