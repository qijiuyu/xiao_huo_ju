package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.init.SelectTagAdapter;
import com.zxdc.utils.library.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择标签
 * Created by Administrator on 2020/2/7.
 */

public class SelectTagActivity extends BaseActivity {

    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.tv_look)
    TextView tvLook;
    private SelectTagAdapter selectTagAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        final String register_des="<u>随便看看</u>";
        tvLook.setText(Html.fromHtml(register_des));

        listView.setLayoutManager(new GridLayoutManager(this, 3));
        selectTagAdapter=new SelectTagAdapter(this);
        listView.setAdapter(selectTagAdapter);
    }

    @OnClick({R.id.tv_skip, R.id.tv_ok, R.id.tv_look})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                break;
            case R.id.tv_ok:
                break;
            case R.id.tv_look:
                break;
            default:
                break;
        }
    }
}
