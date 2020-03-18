package com.ylean.soft.lfd.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.MoreHotterActivity;
import com.ylean.soft.lfd.adapter.main.HotterFragmentAdapter;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;
import com.zxdc.utils.library.view.OvalImageViews;
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
    private HotterFragmentAdapter hotterFragmentAdapter;
    //数据集合
    private List<HotTop.DataBean> listAll=new ArrayList<>();
    //页码
    private int page=1;
    //fragment是否可见
    private boolean isVisibleToUser=false;
    //是否已添加头部view
    private boolean isAddHeadView=false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        HotTop.DataBean dataBean=listAll.get(0);
        headView = LayoutInflater.from(mActivity).inflate(R.layout.hotter_head, null);
        listView.addHeaderView(headView);
        OvalImageViews imgHead=headView.findViewById(R.id.img_head);
        TextView tvTitle=headView.findViewById(R.id.tv_title);
        CircleImageView imgPic=headView.findViewById(R.id.img_pic);
        TextView tvName=headView.findViewById(R.id.tv_name);
        TextView tvFocus=headView.findViewById(R.id.tv_focus);
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
        tvDes.setText("播放 "+dataBean.getPlayCount()+"w    更新至 第"+dataBean.getEpisodeCount()+"集");
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
    }
}
