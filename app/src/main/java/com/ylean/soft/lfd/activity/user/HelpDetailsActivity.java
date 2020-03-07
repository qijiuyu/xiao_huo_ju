package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Help;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2020/3/7.
 */

public class HelpDetailsActivity extends BaseActivity {

    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private Help.HelpBean helpBean;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);
        ButterKnife.bind(this);

        helpBean= (Help.HelpBean) getIntent().getSerializableExtra("helpBean");
        if(helpBean==null){
            return;
        }
        tvTitle.setText(helpBean.getTitle());
        tvContent.setText(helpBean.getContent());

        imgBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpDetailsActivity.this.finish();
            }
        });
    }
}
