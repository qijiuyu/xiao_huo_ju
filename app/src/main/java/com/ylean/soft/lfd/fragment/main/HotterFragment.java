package com.ylean.soft.lfd.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.MoreHotterActivity;
import com.ylean.soft.lfd.adapter.main.HotterFragmentAdapter;
import com.ylean.soft.lfd.view.HotOvalImageView;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HotterFragment extends BaseFragment  implements MyRefreshLayoutListener {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    Unbinder unbinder;
    private View headView;
    private TextView tvFocus;
    private HotterFragmentAdapter hotterFragmentAdapter;
    //数据集合
    private List<HotTop.DataBean> listAll=new ArrayList<>();
    //页码
    private int page=1;
    //fragment是否可见
    private boolean isVisibleToUser=false;
    //是否已添加头部view
    private boolean isAddHeadView=false;
    private HotTop.DataBean dataBean ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus
        EventBus.getDefault().register(this);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more_hotter, container, false);
        unbinder = ButterKnife.bind(this, view);
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
        //获取热播和精选TOP剧集列表
        getHot_Top();
        return view;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                case HandlerConstant.GET_HOT_TOP_SUCCESS1:
                      reList.refreshComplete();
                      listAll.clear();
                      refresh((HotTop) msg.obj);
                      break;
                case HandlerConstant.GET_HOT_TOP_SUCCESS2:
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
                         hotterFragmentAdapter.notifyDataSetChanged();
                         if(listAll.get(0).isFollowUser()){
                             tvFocus.setText("已关注");
                         }else{
                             tvFocus.setText("未关注");
                         }
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
            //展示列表第一条数据
            showListHead();
            //展示列表
            if(hotterFragmentAdapter==null){
                hotterFragmentAdapter=new HotterFragmentAdapter(mActivity,listAll);
                listView.setAdapter(hotterFragmentAdapter);
            }else{
                hotterFragmentAdapter.notifyDataSetChanged();
            }
            if(list.size()<HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
        }else{
            ToastUtil.showLong(hotTop.getDesc());
        }
    }


    /**
     * 展示列表第一条数据
     */
    private void showListHead(){
        if(listAll.size()==0){
            return;
        }
        if(isAddHeadView){
            return;
        }
        isAddHeadView=true;
        final HotTop.DataBean dataBean=listAll.get(0);
        headView = LayoutInflater.from(mActivity).inflate(R.layout.hotter_head, null);
        listView.addHeaderView(headView);
        HotOvalImageView imgHead=headView.findViewById(R.id.img_head);
        TextView tvTitle=headView.findViewById(R.id.tv_title);
        CircleImageView imgPic=headView.findViewById(R.id.img_pic);
        TextView tvName=headView.findViewById(R.id.tv_name);
        tvFocus=headView.findViewById(R.id.tv_focus);
        TextView tvDes=headView.findViewById(R.id.tv_des);
        //背景图片
        Glide.with(this).load(HttpConstant.IP+dataBean.getImgurl()).into(imgHead);
        tvTitle.setText(dataBean.getName());
        //用户头像
        Glide.with(this).load(HttpConstant.IP+dataBean.getUserImg()).into(imgPic);
        tvName.setText(dataBean.getUserNickName());
        if(dataBean.isFollowUser()){
            tvFocus.setText("已关注");
        }else{
            tvFocus.setText("未关注");
        }
        switch (dataBean.getUpdateStatus()){
            case 0:
                tvDes.setText("即将开播");
                break;
            case 1:
                tvDes.setText(Html.fromHtml("播放 "+dataBean.getPlayCountDesc()+"w   更新至 <font color=\"#ffffff\">第" + dataBean.getEpisodeCount() + "集</font>"));
                break;
            case 2:
                tvDes.setText(Html.fromHtml("播放 "+dataBean.getPlayCountDesc()+"w    <font color=\"#ffffff\">全" + dataBean.getEpisodeCount() + "集</font>"));
                break;
        }

        /**
         * 关注，或者取消关注
         */
        tvFocus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusType(EventStatus.HOT_PLAY_FOCUS,dataBean));
            }
        });
    }


    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        if(!isVisibleToUser){
            return;
        }
        switch (eventBusType.getStatus()) {
            case EventStatus.HOT_PLAY_FOCUS:
                dataBean= (HotTop.DataBean) eventBusType.getObject();
                if(dataBean==null){
                    return;
                }
                followUser(dataBean.getUserId());
                break;
            default:
                break;
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        HttpMethod.getHot_Top(String.valueOf(MoreHotterActivity.pageIndex),page, HandlerConstant.GET_HOT_TOP_SUCCESS1,handler);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.getHot_Top(String.valueOf(MoreHotterActivity.pageIndex),page, HandlerConstant.GET_HOT_TOP_SUCCESS2,handler);
    }


    /**
     * 获取热播和精选TOP剧集列表
     */
    public void getHot_Top(){
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isVisibleToUser && view!=null && listAll.size()==0){
                    reList.startRefresh();
                }
            }
        },200);
    }


    /**
     * 关注、取消关注用户
     */
    private void followUser(int id) {
        DialogUtil.showProgress(mActivity,"操作中");
        HttpMethod.follow(id, "0", HandlerConstant.FOLLOW_SUCCESS, handler);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        //获取热播和精选TOP剧集列表
        getHot_Top();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
