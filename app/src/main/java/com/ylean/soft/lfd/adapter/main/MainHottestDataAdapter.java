package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainHottestDataAdapter extends BaseAdapter {

    private Activity activity;
    private List<HotTop.DataBean> list;
    private int index;
    public MainHottestDataAdapter(Activity activity,List<HotTop.DataBean> list,int index) {
        super();
        this.activity = activity;
        this.list=list;
        this.index=index;
    }

    @Override
    public int getCount() {
        return list==null ? 0 : list.size();
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_main_hottest, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HotTop.DataBean dataBean=list.get(position);
        if(index==0){
            holder.imgNum.setVisibility(View.VISIBLE);
            holder.tvNum.setVisibility(View.GONE);
            switch (position){
                case 0:
                     holder.imgNum.setImageResource(R.mipmap.item_one);
                     break;
                case 1:
                    holder.imgNum.setImageResource(R.mipmap.item_two);
                    break;
                case 2:
                    holder.imgNum.setImageResource(R.mipmap.item_three);
                    break;
                default:
                    break;
            }
        }else{
            holder.imgNum.setVisibility(View.GONE);
            holder.tvNum.setVisibility(View.VISIBLE);
            holder.tvNum.setText(String.valueOf(index*3+position+1));
        }
        //背景图片
        String imgUrl= HttpConstant.IP+dataBean.getImgurl();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }
        holder.tvTitle.setText(dataBean.getName());
        //用户头像
        String headUrl=HttpConstant.IP+dataBean.getUserImg();
        holder.imgPic.setTag(R.id.imageid2,headUrl);
        if(holder.imgPic.getTag(R.id.imageid2)!=null && headUrl==holder.imgPic.getTag(R.id.imageid2)){
            Glide.with(activity).load(headUrl).into(holder.imgPic);
        }
        holder.tvName.setText(dataBean.getUserNickName());
        holder.tvSize.setText(dataBean.getPlayCountDesc());


        /**
         * 进入视频详情页面
         */
        holder.imgHead.setTag(R.id.tag1,dataBean);
        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HotTop.DataBean dataBean= (HotTop.DataBean) v.getTag(R.id.tag1);
                if(dataBean.getUpdateStatus()==0){
                    ToastUtil.showLong("敬请期待");
                    return;
                }
                Intent intent=new Intent(activity, VideoPlayActivity.class);
                intent.putExtra("serialId",dataBean.getId());
                activity.startActivity(intent);
            }
        });


        switch (dataBean.getUpdateStatus()){
            case 0:
                holder.tvBlues.setText("即将开播");
                break;
            case 1:
                holder.tvBlues.setText(Html.fromHtml("更新至 <font color=\"#000000\">第" + dataBean.getEpisodeCount() + "集</font>"));
                break;
            case 2:
                holder.tvBlues.setText(Html.fromHtml("<font color=\"#000000\">全" + dataBean.getEpisodeCount() + "集</font>"));
                break;
        }
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_head)
        OvalImageViews imgHead;
        @BindView(R.id.img_num)
        ImageView imgNum;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.img_pic)
        CircleImageView imgPic;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_blues)
        TextView tvBlues;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private int showNum(){
        int num=(index*3+3)-list.size();
        if(num>0){
            return (index*3+3)-num;
        }else{
            return 3;
        }
    }
}

