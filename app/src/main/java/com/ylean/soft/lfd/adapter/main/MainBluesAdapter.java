package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.AbvertActivity;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.activity.web.WebViewActivity;
import com.ylean.soft.lfd.fragment.main.SelectFragment;
import com.ylean.soft.lfd.utils.CornerTransform;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zxdc.utils.library.bean.Abvert;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.view.OvalImageViews;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainBluesAdapter extends BaseAdapter {

    private Activity activity;
    public List<Tag.TagBean> list;
    private Map<Integer,List<Abvert>> abvertMap;

    public MainBluesAdapter(Activity activity, List<Tag.TagBean> list,Map<Integer,List<Abvert>> abvertMap) {
        super();
        this.activity = activity;
        this.list = list;
        this.abvertMap=abvertMap;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ViewHolder holder = null;
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_main_blues, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Tag.TagBean tagBean=list.get(position);
        //标题
        holder.tvName.setText(tagBean.getName());
        //显示广告图片
        if(tagBean.getBanner()!=null){
            String imgUrl= HttpConstant.IP+tagBean.getBanner().getImgurl();
            holder.imgAbvert.setTag(R.id.imageid,imgUrl);
            if(holder.imgAbvert.getTag(R.id.imageid)!=null && imgUrl==holder.imgAbvert.getTag(R.id.imageid)){
                Glide.with(activity).load(imgUrl).into(holder.imgAbvert);
            }
            holder.imgAbvert.setVisibility(View.VISIBLE);
        }else{
            holder.imgAbvert.setVisibility(View.GONE);
        }

        //显示剧集列表
        LinearLayoutManager layoutManager=new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.listView.setLayoutManager(layoutManager);
        holder.listView.setAdapter(new MainBluesDataAdapter(activity,list.get(position).getSerialList()));


        /**
         * 广告跳转
         */
        holder.imgAbvert.setTag(R.id.imageid2,tagBean.getBanner());
        holder.imgAbvert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Abvert abvert= (Abvert) v.getTag(R.id.imageid2);
                //广告跳转
                abvertIntent(abvert);
            }
        });


        /**
         * 查看更多
         */
        holder.tvMore.setTag(tagBean.getId());
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int channelId= (int) v.getTag();
                EventBus.getDefault().post(new EventBusType(EventStatus.LOOK_MORE_MAIN_BLUES,channelId));
            }
        });


        /**
         * 显示banner广告
         */
        List<Abvert> abverts = null;
        switch (position){
            case 0:
                 if(abvertMap.get(1)!=null){
                     abverts=abvertMap.get(1);
                 }else{
                     holder.banner.setVisibility(View.GONE);
                 }
                 break;
            case 4:
                if(abvertMap.get(2)!=null){
                    abverts=abvertMap.get(2);
                }else{
                    holder.banner.setVisibility(View.GONE);
                }
                break;
            case 8:
                if(abvertMap.get(3)!=null){
                    abverts=abvertMap.get(3);
                }else{
                    holder.banner.setVisibility(View.GONE);
                }
                break;
            default:
                holder.banner.setVisibility(View.GONE);
                break;
        }
        //显示banner图片
        showBanner(abverts,holder.banner);
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.banner)
        Banner banner;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.img_abvert)
        OvalImageViews imgAbvert;
        @BindView(R.id.listView)
        RecyclerView listView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    /**
     * 显示banner图片
     */
    private void showBanner(List<Abvert> list,Banner banner){
        if(list==null || list.size()==0){
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

            imageView.setTag(R.id.imageid,abvert);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Abvert abvert= (Abvert) v.getTag(R.id.imageid);
                    //广告跳转
                    abvertIntent(abvert);
                }
            });
        }
    }


    /**
     * 广告跳转
     * @param abvert
     */
    private void abvertIntent(Abvert abvert){
        if(abvert==null){
            return;
        }
        Intent intent=new Intent();
        switch (abvert.getJumpType()){
            //外部链接
            case 1:
                intent.setClass(activity, WebViewActivity.class);
                intent.putExtra("url",abvert.getLinkUrl());
                break;
            //图文详情页
            case 2:
                intent.setClass(activity, AbvertActivity.class);
                intent.putExtra("content",abvert.getContent());
                break;
            //剧集
            case 3:
                intent.setClass(activity, VideoPlayActivity.class);
                intent.putExtra("serialId",abvert.getSerialId());
                break;
            default:
                break;
        }
        activity.startActivity(intent);
    }
}

