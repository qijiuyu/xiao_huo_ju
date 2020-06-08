package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.News;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    HtmlTextView tvContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private News.NewsBean newsBean;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        newsBean= (News.NewsBean) getIntent().getSerializableExtra("newsBean");
        tvTitle.setText(newsBean.getTitle());
        tvContent.setHtml(newsBean.getDetails(), new HtmlHttpImageGetter(tvContent));
    }

    @OnClick(R.id.lin_back)
    public void onViewClicked() {
        finish();
    }
}
