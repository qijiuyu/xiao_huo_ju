package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.BluesListAdapter;
import com.zxdc.utils.library.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2020/2/7.
 */

public class BluesListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.listView)
    RecyclerView listView;
    private BluesListAdapter bluesListAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blues);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("标题");
        tvRight.setText("正序/逆序");

        bluesListAdapter=new BluesListAdapter(this);
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        listView.setAdapter(bluesListAdapter);
    }
}
