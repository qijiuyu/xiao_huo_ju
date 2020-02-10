package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.ProjectListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.view.MyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 热门专题
 * Created by Administrator on 2020/2/7.
 */

public class ProjectListActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private ProjectListAdapter projectListAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("专题名称");
        tvRight.setText("往期精彩");

        projectListAdapter=new ProjectListAdapter(this);
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        listView.setAdapter(projectListAdapter);
    }

    @OnClick({R.id.img_bank, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                 finish();
                break;
            //往事精彩
            case R.id.tv_right:
                setClass(OnceListActivity.class);
                break;
            default:
                break;
        }
    }
}
