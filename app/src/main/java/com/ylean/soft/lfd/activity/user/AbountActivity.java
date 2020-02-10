package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2020/2/8.
 */

public class AbountActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abount);
        ButterKnife.bind(this);

        tvTitle.setText("关于我们");
        tvVersion.setText(Util.getVersionName(this));
    }

    @OnClick(R.id.img_bank)
    public void onViewClicked() {
        finish();
    }
}
