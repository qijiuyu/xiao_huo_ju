package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.BluesListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.SerialVideo;
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
 * Created by Administrator on 2020/2/7.
 */
public class BluesListActivity extends BaseActivity implements MyRefreshLayoutListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private BluesListAdapter bluesListAdapter;
    //剧集id
    private int serialId;
    //页码
    private int page = 1;
    /**
     * 0：正序
     * 1：倒序
     */
    private int order;
    private List<SerialVideo.SerialVideoBean> listAll = new ArrayList<>();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blues);
        ButterKnife.bind(this);
        initView();
        //获取专题列表
        reList.startRefresh();
    }


    /**
     * 初始化
     */
    private void initView() {
        tvRight.setText("正序/逆序");
        serialId = getIntent().getIntExtra("serialId", 0);
        String title=getIntent().getStringExtra("title");
        tvTitle.setText(title);

        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        bluesListAdapter = new BluesListAdapter(this, listAll);
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        listView.setAdapter(bluesListAdapter);
    }


    @OnClick({R.id.img_bank, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                 finish();
                break;
            case R.id.tv_right:
                if(order==0){
                    order=1;
                }else{
                    order=0;
                }
                //获取专题列表
                reList.startRefresh();
                break;
            default:
                break;
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerConstant.GET_SERIAL_VIDEO_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((SerialVideo) msg.obj);
                    break;
                case HandlerConstant.GET_SERIAL_VIDEO_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((SerialVideo) msg.obj);
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
    private void refresh(SerialVideo serialVideo) {
        if (serialVideo == null) {
            return;
        }
        if (serialVideo.isSussess()) {
            List<SerialVideo.SerialVideoBean> list = serialVideo.getData();
            listAll.addAll(list);
            bluesListAdapter.notifyDataSetChanged();
            if (list.size() < HttpMethod.size) {
                reList.setIsLoadingMoreEnabled(false);
            }
        } else {
            ToastUtil.showLong(serialVideo.getDesc());
        }
    }


    /**
     * 下刷
     *
     * @param view
     */
    public void onRefresh(View view) {
        page = 1;
        HttpMethod.getSerialVideo(order, page, serialId, HandlerConstant.GET_SERIAL_VIDEO_SUCCESS1, handler);
    }

    /**
     * 上拉加载更多
     *
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.getSerialVideo(order, page, serialId, HandlerConstant.GET_SERIAL_VIDEO_SUCCESS2, handler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
