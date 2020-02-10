package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.OtherListDataAdapter;
import com.ylean.soft.lfd.adapter.main.RecommendedAdapter;
import com.ylean.soft.lfd.adapter.main.SearchAdapter;
import com.ylean.soft.lfd.persenter.main.SearchPersenter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索结果页
 * Created by Administrator on 2020/2/7.
 */

public class SearchResultActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.lin_no)
    LinearLayout linNo;
    //要搜索的关键字
    private String strKey;
    private SearchPersenter searchPersenter;
    private SearchAdapter searchAdapter;
    private RecommendedAdapter recommendedAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        searchPersenter = new SearchPersenter(this);
        etKey.setOnEditorActionListener(this);

//        listView.setDivider(null);
//        searchAdapter = new SearchAdapter(this);
//        listView.setAdapter(searchAdapter);

        recycle.setLayoutManager(new GridLayoutManager(activity, 3));
        recycle.setAdapter(new RecommendedAdapter(activity));
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
        }
        return false;
    }
}
