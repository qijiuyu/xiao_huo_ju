package com.ylean.soft.lfd.fragment.main;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.MoreAuthorActivity;
import com.ylean.soft.lfd.activity.main.MoreHotterActivity;
import com.ylean.soft.lfd.activity.main.OnlineListActivity;
import com.ylean.soft.lfd.activity.main.ProjectListActivity;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.adapter.main.MainAuthorAdapter;
import com.ylean.soft.lfd.adapter.main.MainBluesAdapter;
import com.ylean.soft.lfd.adapter.main.MainHottestAdapter;
import com.ylean.soft.lfd.adapter.main.MainLookAdapter;
import com.ylean.soft.lfd.adapter.main.MainOnlineAdapter;
import com.ylean.soft.lfd.adapter.main.MainProjectAdapter;
import com.ylean.soft.lfd.persenter.main.SelectFragmentPersenter;
import com.ylean.soft.lfd.utils.CornerTransform;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.Abvert;
import com.zxdc.utils.library.bean.Author;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.Project;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.view.MeasureListView;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.id.candidatesArea;
import static android.R.id.list;

/**
 * 精选
 */
public class SelectFragment extends BaseFragment implements MyRefreshLayoutListener {

    @BindView(R.id.re_list)
    public MyRefreshLayout reList;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_hottest)
    TextView tvHottest;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.view_hot)
    View viewHot;
    @BindView(R.id.view_top)
    View viewTop;
    @BindView(R.id.recycle_hottest)
    RecyclerView recycleHottest;
    @BindView(R.id.recycle_look)
    RecyclerView recycleLook;
    @BindView(R.id.recycle_project)
    RecyclerView recycleProject;
    @BindView(R.id.recycle_online)
    RecyclerView recycleOnLine;
    @BindView(R.id.recycle_author)
    RecyclerView recycleAuthor;
    @BindView(R.id.list_blues)
    MeasureListView listBlues;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.img_like)
    ImageView imgLike;
    Unbinder unbinder;
    /**
     * 0：热播排行
     * 1：精选top
     */
    private int hot_top=0;
    //专题对象
    private Project.ProjectBean projectBean;
    //旋转动画
    private ObjectAnimator rotation;
    //各个频道剧集的adapter
    private MainBluesAdapter mainBluesAdapter;
    //广告集合
    private Map<Integer,List<Abvert>> abvertMap=new HashMap<>();
    private SelectFragmentPersenter selectFragmentPersenter;
    private Handler handler=new Handler();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus
        EventBus.getDefault().register(this);
        //实例化MVP
        selectFragmentPersenter=new SelectFragmentPersenter(mActivity);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select, container, false);
        unbinder = ButterKnife.bind(this, view);

        reList.setMyRefreshLayoutListener(this);
        reList.setIsLoadingMoreEnabled(false);
        //加载数据
        reList.startRefresh();
        return view;
    }


    @OnClick({R.id.tv_hottest, R.id.tv_top, R.id.tv_more_hottest,R.id.tv_more_project,R.id.tv_more_online,R.id.lin_like,R.id.tv_more_author})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //今日最热
            case R.id.tv_hottest:
                 hot_top=0;
                 viewHot.setVisibility(View.VISIBLE);
                 viewTop.setVisibility(View.GONE);
                tvHottest.setTextColor(mActivity.getResources().getColor(android.R.color.black));
                tvTop.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                //获取热播和精选TOP剧集列表
                selectFragmentPersenter.getHot_Top(hot_top);
                break;
            //top50
            case R.id.tv_top:
                 hot_top=1;
                viewHot.setVisibility(View.GONE);
                viewTop.setVisibility(View.VISIBLE);
                tvHottest.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                tvTop.setTextColor(mActivity.getResources().getColor(android.R.color.black));
                //获取热播和精选TOP剧集列表
                selectFragmentPersenter.getHot_Top(hot_top);
                break;
            //今日最热和top50查看更多
            case R.id.tv_more_hottest:
                setClass(MoreHotterActivity.class);
                break;
            //刷新--猜你喜欢
            case R.id.lin_like:
                rotation = ObjectAnimator.ofFloat(imgLike, "rotation", 0f, 359f);
                rotation.setRepeatCount(ObjectAnimator.INFINITE);
                rotation.setInterpolator(new LinearInterpolator());
                rotation.setDuration(800);
                rotation.start();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //获取猜你喜欢的数据
                        selectFragmentPersenter.guessLike();
                    }
                },1500);
                break;
            //热门专题查看更多
            case R.id.tv_more_project:
                  if(projectBean==null){
                      return;
                  }
                  Intent intent=new Intent(mActivity,ProjectListActivity.class);
                  intent.putExtra("projectBean",projectBean);
                  startActivity(intent);
                  break;
            //即将上线查看更多
            case R.id.tv_more_online:
                 setClass(OnlineListActivity.class);
                 break;
            //热门作者--查看更多
            case R.id.tv_more_author:
                  setClass(MoreAuthorActivity.class);
                  break;
            default:
                break;
        }
    }

    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            //显示banner
            case EventStatus.SHOW_MAIN_BANNER:
                showBanner((List<HotTop.DataBean>) eventBusType.getObject());
                break;
            //显示今日最热/top50
            case EventStatus.SHOW_MAIN_HOTTER:
                  showHotter((List<HotTop.DataBean>) eventBusType.getObject());
                  break;
            //显示猜你喜欢
            case EventStatus.SHOW_MAIN_LOOK:
                 showLook((List<HotTop.DataBean>) eventBusType.getObject());
                 break;
            //显示热门专题
            case EventStatus.SHOW_MAIN_PROJECT:
                  showProject((List<Project.ProjectBean>) eventBusType.getObject());
                  break;
            //显示即将上线
            case EventStatus.SHOW_MAIN_ONLINE:
                  showOnline((List<HotTop.DataBean>) eventBusType.getObject());
                  break;
            //显示热门作者专区
            case EventStatus.SHOW_MAIN_AUTHOR:
                  showAuthor((List<Author.DataBean>) eventBusType.getObject());
                  break;
            //显示各个频道的剧集
            case EventStatus.SHOW_MAIN_BLUES:
                  showBlues((List<Tag.TagBean>) eventBusType.getObject());

                  //获取对应频道的广告
                  selectFragmentPersenter.getAbvert();
                  break;
            //显示对应频道的广告
            case EventStatus.CHANNEL_ABVERT:
                  List<Abvert> abverts= (List<Abvert>) eventBusType.getObject();
                  if(abverts.size()>0){
                      abvertMap.put(abverts.get(0).getType(),abverts);
                      mainBluesAdapter.notifyDataSetChanged();
                  }
                  break;
            default:
                break;

        }
    }


    /**
     * 显示banner图片
     */
    private void showBanner(List<HotTop.DataBean> list){
        if(list==null || list.size()==0){
            list=new ArrayList<>();
            banner.update(list);
            return;
        }
        //设置样式，里面有很多种样式可以自己都看看效果
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        banner.setBannerAnimation(Transformer.Default);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new ABImageLoader());
        //设置图片集合
        banner.setImages(list);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    public class ABImageLoader extends ImageLoader {
        public void displayImage(Context context, Object path, ImageView imageView) {
            HotTop.DataBean dataBean= (HotTop.DataBean) path;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH) //优先级
                    .transform(new CornerTransform(10)); //圆角
            Glide.with(context).load(HttpConstant.IP+dataBean.getImgurl()).apply(options).into(imageView);

            imageView.setTag(R.id.tag1,dataBean);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    HotTop.DataBean dataBean= (HotTop.DataBean) v.getTag(R.id.tag1);
                    Intent intent=new Intent(mActivity, VideoPlayActivity.class);
                    intent.putExtra("serialId",dataBean.getId());
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 显示今日最热/top50
     */
    private void showHotter(List<HotTop.DataBean> listAll){
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleHottest.setLayoutManager(layoutManager);
        //解析为3个数据为一组
        List<List<HotTop.DataBean>> arrayList=new ArrayList<>();
        List<HotTop.DataBean> list=new ArrayList<>();
        for (int index = 0,len=listAll.size(); index<len; index++) {
             list.add(listAll.get(index));
             if((index+1)%3==0){
                 arrayList.add(list);
                 list=new ArrayList<>();
             }
        }
        if(list.size()>0){
            arrayList.add(list);
        }
        recycleHottest.setAdapter(new MainHottestAdapter(mActivity,arrayList));
    }


    /**
     * 显示猜你喜欢
     */
    private void showLook(List<HotTop.DataBean> list){
        if(rotation!=null){
            rotation.end();
        }
        recycleLook.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recycleLook.setAdapter(new MainLookAdapter(mActivity,list));
    }


    /**
     * 显示热门专题
     */
    private void showProject(List<Project.ProjectBean> list){
        if(list==null || list.size()==0){
            return;
        }
        projectBean=list.get(0);
        tvProject.setText(projectBean.getName());
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleProject.setLayoutManager(layoutManager);
        recycleProject.setAdapter(new MainProjectAdapter(mActivity,projectBean.getSerialList()));
        handler.postDelayed(new Runnable() {
            public void run() {
                //置顶
                scrollView.scrollTo(0, 0);
            }
        },100);
    }


    /**
     * 显示即将上线
     */
    private void showOnline(List<HotTop.DataBean> list){
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleOnLine.setLayoutManager(layoutManager);
        recycleOnLine.setAdapter(new MainOnlineAdapter(mActivity,list));
        handler.postDelayed(new Runnable() {
            public void run() {
                //置顶
                scrollView.scrollTo(0, 0);
            }
        },100);
    }


    /**
     * 展示热门作者专区
     */
    private void showAuthor(List<Author.DataBean> list){
        recycleAuthor.setLayoutManager(new GridLayoutManager(mActivity, 4));
        recycleAuthor.setAdapter(new MainAuthorAdapter(mActivity,list));
        handler.postDelayed(new Runnable() {
            public void run() {
                //置顶
                scrollView.scrollTo(0, 0);
            }
        },100);
    }


    /**
     * 显示各个频道的剧集
     */
    private void showBlues(List<Tag.TagBean> list){
        mainBluesAdapter=new MainBluesAdapter(mActivity,list,abvertMap);
        listBlues.setAdapter(mainBluesAdapter);
        handler.postDelayed(new Runnable() {
            public void run() {
                //置顶
                scrollView.scrollTo(0, 0);
            }
        },100);
    }


    @Override
    public void onRefresh(View view) {
        //获取banner
        selectFragmentPersenter.mainBanner();
        //获取热播和精选TOP剧集列表
        selectFragmentPersenter.getHot_Top(hot_top);
        //获取猜你喜欢的数据
        selectFragmentPersenter.guessLike();
        //获取专题列表
        selectFragmentPersenter.getProject();
        //获取即将上线的数据
        selectFragmentPersenter.getOnline();
        //获取热门作者
        selectFragmentPersenter.hotAuthor();
        //获取频道剧集列表
        selectFragmentPersenter.channel();
        handler.postDelayed(new Runnable() {
            public void run() {
                reList.refreshComplete();
                //置顶
                scrollView.scrollTo(0, 0);
            }
        },1200);
    }

    @Override
    public void onLoadMore(View view) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
