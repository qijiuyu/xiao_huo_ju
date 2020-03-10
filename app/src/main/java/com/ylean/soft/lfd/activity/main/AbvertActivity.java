package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2020/3/10.
 */

public class AbvertActivity extends BaseActivity {

    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_des)
    HtmlTextView tvDes;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abvert);
        ButterKnife.bind(this);
        initView();
        imgBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbvertActivity.this.finish();
            }
        });
    }


    /**
     * 初始化
     */
    private void initView(){
        String content=getIntent().getStringExtra("content");
        tvDes.setHtml(content, new HtmlHttpImageGetter(tvDes));
    }
}
