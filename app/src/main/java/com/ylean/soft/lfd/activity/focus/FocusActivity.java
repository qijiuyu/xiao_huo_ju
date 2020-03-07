package com.ylean.soft.lfd.activity.focus;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.focus.FocusListAdapter;
import com.ylean.soft.lfd.adapter.main.RecommendedAdapter;
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
 * 关注
 * Created by Administrator on 2020/2/5.
 */

public class FocusActivity extends BaseActivity implements MyRefreshLayoutListener {

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
    private FocusListAdapter focusListAdapter;
    //数据集合
    private List<HotTop.DataBean> listAll = new ArrayList<>();
    //页码
    private int page = 1;
    //旋转动画
    private ObjectAnimator rotation;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        ButterKnife.bind(this);
        initView();
        //加载数据
        reList.startRefresh();
    }


    /**
     * 初始化
     */
    private void initView() {
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
        focusListAdapter = new FocusListAdapter(this, listAll);
        listView.setAdapter(focusListAdapter);
    }


    @OnClick({R.id.tv_focus, R.id.lin_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //关注的人
            case R.id.tv_focus:
                setClass(FocusPopleActivity.class);
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
                case HandlerConstant.FOCUS_SERIAL_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((HotTop) msg.obj);
                    break;
                case HandlerConstant.FOCUS_SERIAL_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((HotTop) msg.obj);
                    break;
                //获取推荐数据
                case HandlerConstant.GET_MAIN_BANNER:
                    HotTop hotTop = (HotTop) msg.obj;
                    if (hotTop == null) {
                        break;
                    }
                    if (hotTop.isSussess()) {
                        recycle.setLayoutManager(new GridLayoutManager(FocusActivity.this, 3));
                        recycle.setAdapter(new RecommendedAdapter(FocusActivity.this, hotTop.getData()));
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
            //适配器遍历显示
            focusListAdapter.notifyDataSetChanged();
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


    /**
     * 下刷
     *
     * @param view
     */
    public void onRefresh(View view) {
        page = 1;
        HttpMethod.focusSerial(page, HandlerConstant.FOCUS_SERIAL_SUCCESS1, handler);
    }

    /**
     * 上拉加载更多
     *
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.focusSerial(page, HandlerConstant.FOCUS_SERIAL_SUCCESS2, handler);
    }

    /**
     * 获取推荐数据
     */
    private void mainBanner() {
        HttpMethod.mainBanner(handler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
