package com.ylean.soft.lfd.activity.focus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.focus.FocusListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.view.MyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关注
 * Created by Administrator on 2020/2/5.
 */

public class FocusActivity extends BaseActivity {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private FocusListAdapter focusListAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        ButterKnife.bind(this);

        findViewById(R.id.tv_focus).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setClass(FocusPopleActivity.class);
            }
        });

        focusListAdapter=new FocusListAdapter(this);
        listView.setDivider(null);
        listView.setAdapter(focusListAdapter);
    }
}
