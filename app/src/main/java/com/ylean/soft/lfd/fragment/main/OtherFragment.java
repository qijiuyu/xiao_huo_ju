package com.ylean.soft.lfd.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.AbvertActivity;
import com.ylean.soft.lfd.activity.main.MainActivity;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.activity.web.WebViewActivity;
import com.ylean.soft.lfd.adapter.main.OtherListAdapter;
import com.ylean.soft.lfd.utils.CornerTransform;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.Abvert;
import com.zxdc.utils.library.bean.AbvertList;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class OtherFragment extends BaseFragment implements MyRefreshLayoutListener {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    Unbinder unbinder;
    Banner banner;
    //数据集合
    private List<HotTop.DataBean> listAll=new ArrayList<>();
    //解析后的集合数据
    private  List<List<HotTop.DataBean>> arrayList=new ArrayList<>();
    //页码
    private int page=1;
    //fragment是否可见
    private boolean isVisibleToUser=false;
    private OtherListAdapter otherListAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other, container, false);
        unbinder = ButterKnife.bind(this, view);
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
        otherListAdapter=new OtherListAdapter(mActivity,arrayList);
        listView.setAdapter(otherListAdapter);
        //头部view
        View headView=LayoutInflater.from(mActivity).inflate(R.layout.layout_banner,null);
        banner=headView.findViewById(R.id.banner_other);
        listView.addHeaderView(headView);
        //获取剧集数据
        serialList();
        return view;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    arrayList.clear();
                    refresh((HotTop) msg.obj);
                    break;
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((HotTop) msg.obj);
                    break;
                //回执广告数据
                case HandlerConstant.GET_ABVERT_SUCCESS:
                      AbvertList abvertList= (AbvertList) msg.obj;
                      if(abvertList==null){
                          break;
                      }
                      if(abvertList.isSussess()){
                          //显示banner图片
                          showBanner(abvertList.getData());
                      }else{
                          ToastUtil.showLong(abvertList.getDesc());
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
    private void refresh(HotTop hotTop){
        if(hotTop==null){
            return;
        }
        if(hotTop.isSussess()){
            List<HotTop.DataBean> list=hotTop.getData();
            listAll.addAll(list);
            //将数据解析为7个对象为一组
            parsingData();
            //适配器遍历显示
            otherListAdapter.notifyDataSetChanged();
            if(list.size()< HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
        }else{
            ToastUtil.showLong(hotTop.getDesc());
        }
    }


    /**
     * 显示banner图片
     */
    private void showBanner(List<Abvert> list){
        if(list==null || list.size()==0){
            list=new ArrayList<>();
            banner.update(list);
            return;
        }
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
            Abvert abvert= (Abvert) path;
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH) //优先级
                    .transform(new CornerTransform(10)); //圆角
            Glide.with(context).load(HttpConstant.IP+abvert.getImgurl()).apply(options).into(imageView);

            /**
             * 广告跳转
             */
            imageView.setTag(R.id.tag1,abvert);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Abvert abvert= (Abvert) v.getTag(R.id.tag1);
                    Intent intent=new Intent();
                    switch (abvert.getJumpType()){
                        //外部链接
                        case 1:
                            intent.setClass(mActivity, WebViewActivity.class);
                            intent.putExtra("url",abvert.getLinkUrl());
                            break;
                        //图文详情页
                        case 2:
                            intent.setClass(mActivity, AbvertActivity.class);
                            intent.putExtra("content",abvert.getContent());
                            break;
                        //剧集
                        case 3:
                            intent.setClass(mActivity, VideoPlayActivity.class);
                            intent.putExtra("serialId",abvert.getSerialId());
                            break;
                        default:
                            break;
                    }
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        HttpMethod.serialList(getChannelId(),null,page,0, HandlerConstant.GET_SERIAL_LIST_SUCCESS1,handler);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.serialList(getChannelId(),null,page,0, HandlerConstant.GET_SERIAL_LIST_SUCCESS2,handler);
    }


    /**
     * 获取广告列表
     */
    private void getAbvert(){
        HttpMethod.getAbvert("0",getChannelId(),handler);
    }


    /**
     * 获取频道ID
     */
    private int getChannelId(){
        MainActivity activity=(MainActivity)mActivity;
        if(activity==null){
            return 0;
        }
        return activity.channelList.get(activity.pageIndex).getId();
    }


    /**
     * 将数据解析为7个对象为一组
     * @return
     */
    private void parsingData(){
        List<HotTop.DataBean> list=new ArrayList<>();
        for (int index = 0,len=listAll.size(); index<len; index++) {
             list.add(listAll.get(index));
             if((index+1)%7==0){
                 arrayList.add(list);
                 list=new ArrayList<>();
             }
        }
        if(list.size()>0){
            arrayList.add(list);
        }
    }


    /**
     * serialList
     */
    public void serialList(){
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isVisibleToUser && view!=null && listAll.size()==0){
                    //加载频道剧集数据
                    reList.startRefresh();
                    //获取广告列表
                    getAbvert();
                }
            }
        },200);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        //获取剧集数据
        serialList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
