package com.ylean.soft.lfd.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.MoreHotterActivity;
import com.ylean.soft.lfd.activity.main.OnlineListActivity;
import com.ylean.soft.lfd.activity.main.ProjectListActivity;
import com.ylean.soft.lfd.adapter.main.MainAuthorAdapter;
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
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.id.list;

/**
 * 精选
 */
public class SelectFragment extends BaseFragment {

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
    Unbinder unbinder;
    private SelectFragmentPersenter selectFragmentPersenter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus
        EventBus.getDefault().register(this);
        selectFragmentPersenter=new SelectFragmentPersenter(mActivity);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select, container, false);
        unbinder = ButterKnife.bind(this, view);

        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_BANNER));
        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_HOTTER));
        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_LOOK));
        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_PROJECT));
        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_ONLINE));
        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_AUTHOR));
        return view;
    }


    @OnClick({R.id.tv_hottest, R.id.tv_top, R.id.tv_more_hottest, R.id.tv_refresh_look,R.id.tv_more_project,R.id.tv_more_online})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //今日最热
            case R.id.tv_hottest:
                 viewHot.setVisibility(View.VISIBLE);
                 viewTop.setVisibility(View.GONE);
                tvHottest.setTextColor(mActivity.getResources().getColor(R.color.color_FFBC32));
                tvTop.setTextColor(mActivity.getResources().getColor(android.R.color.black));
                break;
            //top50
            case R.id.tv_top:
                viewHot.setVisibility(View.GONE);
                viewTop.setVisibility(View.VISIBLE);
                tvHottest.setTextColor(mActivity.getResources().getColor(android.R.color.black));
                tvTop.setTextColor(mActivity.getResources().getColor(R.color.color_FFBC32));
                break;
            //今日最热和top50查看更多
            case R.id.tv_more_hottest:
                setClass(MoreHotterActivity.class);
                break;
            //换一换：大家都在看
            case R.id.tv_refresh_look:
                break;
            //热门专题查看更多
            case R.id.tv_more_project:
                  setClass(ProjectListActivity.class);
                  break;
            //即将上线查看更多
            case R.id.tv_more_online:
                 setClass(OnlineListActivity.class);
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
                showBanner();
                break;
            //显示今日最热/top50
            case EventStatus.SHOW_MAIN_HOTTER:
                  showHotter();
                  break;
            //显示大家都在看
            case EventStatus.SHOW_MAIN_LOOK:
                 showLook();
                 break;
            //显示热门专题
            case EventStatus.SHOW_MAIN_PROJECT:
                  showProject();
                  break;
            //显示即将上线
            case EventStatus.SHOW_MAIN_ONLINE:
                  showOnline();
                  break;
            //显示热门作者专区
            case EventStatus.SHOW_MAIN_AUTHOR:
                  showAuthor();
                  break;
            default:
                break;

        }
    }


    /**
     * 显示banner图片
     */
    private void showBanner(){
        List<String> list=new ArrayList<>();
        list.add("http://dyrs.yl-mall.cn/upload/image/20200115/1bd5569b-b960-409f-922e-b1204f4be959.png");
        banner.setVisibility(View.VISIBLE);
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
            if(path==null){
                return;
            }
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH) //优先级
                    .transform(new CornerTransform(10)); //圆角
            Glide.with(context).load(path.toString()).apply(options).into(imageView);
        }
    }


    /**
     * 显示今日最热/top50
     */
    private void showHotter(){
        MainHottestAdapter mainHottestAdapter=new MainHottestAdapter(mActivity);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleHottest.setLayoutManager(layoutManager);
        recycleHottest.setAdapter(mainHottestAdapter);
    }


    /**
     * 显示大家都在看
     */
    private void showLook(){
        MainLookAdapter mainLookAdapter=new MainLookAdapter(mActivity);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleLook.setLayoutManager(layoutManager);
        recycleLook.setAdapter(mainLookAdapter);
    }


    /**
     * 显示热门专题
     */
    private void showProject(){
        MainProjectAdapter mainProjectAdapter=new MainProjectAdapter(mActivity);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleProject.setLayoutManager(layoutManager);
        recycleProject.setAdapter(mainProjectAdapter);
    }


    /**
     * 显示即将上线
     */
    private void showOnline(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleOnLine.setLayoutManager(layoutManager);
        recycleOnLine.setAdapter(new MainOnlineAdapter(mActivity));
    }


    /**
     * 展示热门作者专区
     */
    private void showAuthor(){
        recycleAuthor.setLayoutManager(new GridLayoutManager(mActivity, 4));
        recycleAuthor.setAdapter(new MainAuthorAdapter(mActivity));
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
