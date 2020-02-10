package com.ylean.soft.lfd.activity.focus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.focus.FocusPopleAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.view.MyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关注的人
 * Created by Administrator on 2020/2/7.
 */

public class FocusPopleActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private FocusPopleAdapter focusPopleAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_pople);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("关注的人");

        focusPopleAdapter=new FocusPopleAdapter(this);
        listView.setDivider(null);
        listView.setAdapter(focusPopleAdapter);
    }
}
