package com.ylean.soft.lfd.activity.main;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.RecommendedAdapter;
import com.ylean.soft.lfd.adapter.main.SearchAdapter;
import com.ylean.soft.lfd.persenter.main.SearchPersenter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索结果页
 * Created by Administrator on 2020/2/7.
 */
public class SearchResultActivity extends BaseActivity implements TextView.OnEditorActionListener, MyRefreshLayoutListener {

    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.lin_no)
    LinearLayout linNo;
    @BindView(R.id.imgRefresh)
    ImageView imgRefresh;
    //要搜索的关键字
    private String strKey;
    private SearchPersenter searchPersenter;
    //搜索结果的适配器
    private SearchAdapter searchAdapter;
    //数据集合
    private List<HotTop.DataBean> listAll = new ArrayList<>();
    //页码
    private int page = 1,page2=0;
    //旋转动画
    private ObjectAnimator rotation;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        initView();
        //加载数据
        reList.startRefresh();
        findViewById(R.id.img_bank).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SearchResultActivity.this.finish();
            }
        });
    }

    /**
     * 初始化
     */
    private void initView() {
        searchPersenter = new SearchPersenter(this);
        strKey = getIntent().getStringExtra("keys");
        etKey.setText(strKey);
        etKey.setOnEditorActionListener(this);
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
        searchAdapter = new SearchAdapter(this, listAll);
        listView.setAdapter(searchAdapter);

        etKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.img_bank, R.id.lin_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                 finish();
                break;
            //刷新
            case R.id.lin_refresh:
                rotation = ObjectAnimator.ofFloat(imgRefresh, "rotation", 0f, 359f);
                rotation.setRepeatCount(ObjectAnimator.INFINITE);
                rotation.setInterpolator(new LinearInterpolator());
                rotation.setDuration(800);
                rotation.start();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //获取推荐数据
                        mainBanner();
                    }
                },1500);
                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if(rotation!=null){
                rotation.end();
            }
            switch (msg.what) {
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((HotTop) msg.obj);
                    break;
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((HotTop) msg.obj);
                    break;
                //获取推荐数据
                case HandlerConstant.GET_HOT_TOP_SUCCESS1:
                    HotTop hotTop = (HotTop) msg.obj;
                    if (hotTop == null) {
                        break;
                    }
                    if (hotTop.isSussess()) {
                        if(hotTop.getData().size()<=5){
                            page2=0;
                        }
                        recycle.setLayoutManager(new GridLayoutManager(SearchResultActivity.this, 3));
                        recycle.setAdapter(new RecommendedAdapter(SearchResultActivity.this, hotTop.getData()));
                    } else {
                        ToastUtil.showLong(hotTop.getDesc());
                    }
                    break;
                case HandlerConstant.REQUST_ERROR:
                    ToastUtil.showLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    /**
     * 刷新界面数据
     */
    private void refresh(HotTop hotTop) {
        if (hotTop == null) {
            return;
        }
        if (hotTop.isSussess()) {
            List<HotTop.DataBean> list = hotTop.getData();
            listAll.addAll(list);
            //刷新数据
            searchAdapter.notifyDataSetChanged();
            if (list.size() < HttpMethod.size) {
                reList.setIsLoadingMoreEnabled(false);
            }
            if (listAll.size() == 0) {
                reList.setVisibility(View.GONE);
                linNo.setVisibility(View.VISIBLE);
                //获取推荐数据
                mainBanner();
            } else {
                reList.setVisibility(View.VISIBLE);
                linNo.setVisibility(View.GONE);
            }
        } else {
            ToastUtil.showLong(hotTop.getDesc());
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
            //清空列表
            listAll.clear();
            searchAdapter.notifyDataSetChanged();
            //加载数据
            reList.startRefresh();
        }
        return false;
    }


    /**
     * 下刷
     *
     * @param view
     */
    public void onRefresh(View view) {
        page = 1;
        HttpMethod.serialList(0, strKey, page, 0, HandlerConstant.GET_SERIAL_LIST_SUCCESS1, handler);
    }

    /**
     * 上拉加载更多
     *
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.serialList(0, strKey, page, 0, HandlerConstant.GET_SERIAL_LIST_SUCCESS2, handler);
    }


    /**
     * 获取推荐数据
     */
    private void mainBanner() {
        page2++;
        HttpMethod.getHot_Top2("1",page2, HandlerConstant.GET_HOT_TOP_SUCCESS1,handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }

}
