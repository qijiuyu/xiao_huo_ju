package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.AuthorDetailsAdapter;
import com.ylean.soft.lfd.adapter.main.MainAuthorAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.StatusBarUtils;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.ClickTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者界面
 * Created by Administrator on 2020/2/7.
 */

public class AuthorDetailsActivity extends BaseActivity {

    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_focus)
    ClickTextView tvFocus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.listView)
    RecyclerView listView;
    private AuthorDetailsAdapter authorDetailsAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_author);
        ButterKnife.bind(this);

        authorDetailsAdapter=new AuthorDetailsAdapter(this);
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        listView.setAdapter(authorDetailsAdapter);
    }
}
