package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.persenter.main.SearchPersenter;
import com.ylean.soft.lfd.view.TagsLayout;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索页面
 * Created by Administrator on 2020/2/7.
 */

public class SearchActivity extends BaseActivity  implements TextView.OnEditorActionListener {

    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.tag_hoistory)
    TagsLayout tagHoistory;
    @BindView(R.id.tag_hot)
    TagsLayout tagHot;
    private SearchPersenter searchPersenter;
    //要搜索的关键字
    private String strKey;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        searchPersenter=new SearchPersenter(this);
        etKey.setOnEditorActionListener(this);
    }

    @OnClick({R.id.tv_cancle, R.id.img_remove})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                finish();
                break;
            //清空搜索历史
            case R.id.img_remove:
                tagHoistory.removeAllViews();
                SPUtil.getInstance(this).removeMessage(SPUtil.SEARCH_KEY);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            strKey = etKey.getText().toString().trim();
            if (TextUtils.isEmpty(strKey)) {
                ToastUtil.showLong("请输入您想看的内容！");
                return false;
            }
            //关闭键盘
            lockKey(etKey);
            //保存搜索过的关键字
            searchPersenter.addTabKey(strKey);
            etKey.setText(null);
            searchPersenter.gotoSearchList(strKey);
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        searchPersenter.showHistory(tagHoistory);
    }
}
