package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.OnceListAdapter;
import com.zxdc.utils.library.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 往事精彩
 * Created by Administrator on 2020/2/7.
 */

public class OnceListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_once_list);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("往事精彩");
        listView.setAdapter(new OnceListAdapter(this));
    }
}
