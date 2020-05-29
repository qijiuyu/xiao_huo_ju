package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.OnlineListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 即将上线
 * Created by Administrator on 2020/2/7.
 */

public class OnlineListActivity extends BaseActivity  implements MyRefreshLayoutListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private OnlineListAdapter onlineListAdapter;
    //数据集合
    private List<HotTop.DataBean> listAll=new ArrayList<>();
    //页码
    private int page=1;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_list);
        ButterKnife.bind(this);
        initView();
        //注册eventBus
        EventBus.getDefault().register(this);
        //获取即将上线
        reList.startRefresh();
    }


    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("即将上线");
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
        //返回
        findViewById(R.id.img_bank).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnlineListActivity.this.finish();
            }
        });
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                case HandlerConstant.GET_ONLINE_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((HotTop) msg.obj);
                    break;
                case HandlerConstant.GET_ONLINE_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((HotTop) msg.obj);
                    break;
                //关注，或者取消关注
                case HandlerConstant.FOLLOW_SUCCESS:
                    BaseBean baseBean = (BaseBean) msg.obj;
                    if (baseBean == null) {
                        break;
                    }
                    if (baseBean.isSussess()) {
                        if(dataBean.isFollowUser()){
                            dataBean.setFollowUser(false);
                        }else{
                            dataBean.setFollowUser(true);
                        }
                        onlineListAdapter.notifyDataSetChanged();
                    }
                    ToastUtil.showLong(baseBean.getDesc());
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
    private void refresh(HotTop hotTop){
        if(hotTop==null){
            return;
        }
        if(hotTop.isSussess()){
            List<HotTop.DataBean> list=hotTop.getData();
            listAll.addAll(list);
            //展示列表
            if(onlineListAdapter==null){
                onlineListAdapter=new OnlineListAdapter(this,listAll);
                listView.setAdapter(onlineListAdapter);
            }else{
                onlineListAdapter.notifyDataSetChanged();
            }
            if(list.size()< HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
        }else{
            ToastUtil.showLong(hotTop.getDesc());
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        getOnline(HandlerConstant.GET_ONLINE_SUCCESS1);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        getOnline(HandlerConstant.GET_ONLINE_SUCCESS2);
    }


    /**
     * 获取即将上线
     */
    public void getOnline(int index){
        HttpMethod.getOnline(page, index,handler);
    }

    /**
     * 关注、取消关注用户
     */
    private HotTop.DataBean dataBean;
    public void followUser(HotTop.DataBean dataBean) {
        this.dataBean=dataBean;
        DialogUtil.showProgress(this,"操作中");
        HttpMethod.follow(dataBean.getUserId(), "0", HandlerConstant.FOLLOW_SUCCESS, handler);
    }


    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            //取消用户关注
            case EventStatus.CANCLE_FOCUS_USER:
                //关注用户
            case EventStatus.FOCUS_USER:
                final int userId= (int) eventBusType.getObject();
                for (int i=0;i<listAll.size();i++){
                      if(userId==listAll.get(i).getUserId()){
                          if(EventStatus.CANCLE_FOCUS_USER==eventBusType.getStatus()){
                              listAll.get(i).setFollowUser(false);
                          }else{
                              listAll.get(i).setFollowUser(true);
                          }
                          break;
                      }
                }
                onlineListAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
        EventBus.getDefault().unregister(this);
    }
}
