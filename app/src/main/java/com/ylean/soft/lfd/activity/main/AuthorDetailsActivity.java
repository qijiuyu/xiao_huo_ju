package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.init.LoginActivity;
import com.ylean.soft.lfd.adapter.main.AuthorDetailsAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.AuthorDetails;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.StatusBarUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.ClickTextView;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 作者界面
 * Created by Administrator on 2020/2/7.
 */
public class AuthorDetailsActivity extends BaseActivity  implements MyRefreshLayoutListener {

    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_focus)
    ClickTextView tvFocus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private AuthorDetailsAdapter authorDetailsAdapter;
    //作者id
    private int id;
    //作者对象
    private AuthorDetails.DetailsBean detailsBean;
    //数据集合
    private List<HotTop.DataBean> listAll=new ArrayList<>();
    //页码
    private int page=1;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_author);
        ButterKnife.bind(this);
        initView();
        //获取作者详情
        getAuthorDetails();
        //加载作品数据
        reList.startRefresh();
    }


    /**
     * 初始化
     */
    private void initView() {
        id = getIntent().getIntExtra("id", -1);
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        authorDetailsAdapter = new AuthorDetailsAdapter(this,listAll);
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        listView.setAdapter(authorDetailsAdapter);
    }

    @OnClick(R.id.tv_focus)
    public void onViewClicked() {
        if(!MyApplication.isLogin()){
            setClass(LoginActivity.class);
            return;
        }
        //关注、取消关注用户
        if(detailsBean==null){
            return;
        }
        if(detailsBean.isFollowUser()){
            DialogUtil.showProgress(this, "取消中");
        }else{
            DialogUtil.showProgress(this, "关注中");
        }
        followUser();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what) {
                //获取用户详情
                case HandlerConstant.AUTHOR_DETAILS_SUCCESS:
                    AuthorDetails authorDetails = (AuthorDetails) msg.obj;
                    if (authorDetails == null) {
                        break;
                    }
                    if (authorDetails.isSussess()) {
                        //展示用户信息
                        showAuthor(detailsBean = authorDetails.getData());
                    } else {
                        ToastUtil.showLong(authorDetails.getDesc());
                    }
                    break;
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((HotTop) msg.obj);
                    break;
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((HotTop) msg.obj);
                    break;
                //取消关注用户
                case HandlerConstant.FOLLOW_SUCCESS:
                     BaseBean baseBean= (BaseBean) msg.obj;
                     if(baseBean==null){
                         break;
                     }
                     if(baseBean.isSussess()){
                         if(detailsBean.isFollowUser()){
                             detailsBean.setFollowUser(false);
                         }else{
                             detailsBean.setFollowUser(true);
                         }
                         //展示用户信息
                         showAuthor(detailsBean);
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
     * 展示用户信息
     *
     * @param detailsBean
     */
    private void showAuthor(AuthorDetails.DetailsBean detailsBean) {
        if (detailsBean == null) {
            return;
        }
        Glide.with(activity).load(detailsBean.getImgurl()).into(imgHead);
        tvFans.setText(String.valueOf(detailsBean.getFollowCount()));
        if (detailsBean.isFollowUser()) {
            tvFocus.setBackgroundResource(R.drawable.bg_author_focus);
            tvFocus.setText("已关注");
        } else {
            tvFocus.setBackgroundResource(R.drawable.btn_bg_sex);
            tvFocus.setText("关注");
        }
        tvName.setText(detailsBean.getNickname());
    }


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
            authorDetailsAdapter.notifyDataSetChanged();
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
        HttpMethod.serialList(0,null,page,id, HandlerConstant.GET_SERIAL_LIST_SUCCESS1,handler);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.serialList(0,null,page,id, HandlerConstant.GET_SERIAL_LIST_SUCCESS2,handler);
    }



    /**
     * 获取作者详情
     */
    private void getAuthorDetails() {
        DialogUtil.showProgress(this, "数据加载中");
        HttpMethod.getAuthorDetails(id, handler);
    }

    /**
     * 关注、取消关注用户
     */
    private void followUser() {
        HttpMethod.follow(id, "0", HandlerConstant.FOLLOW_SUCCESS, handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
