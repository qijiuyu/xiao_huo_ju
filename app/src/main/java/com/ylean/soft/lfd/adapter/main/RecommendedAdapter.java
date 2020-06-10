package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.MyHolder> {

    private Activity activity;
    private List<HotTop.DataBean> list;
    public RecommendedAdapter(Activity activity,List<HotTop.DataBean> list) {
        this.activity = activity;
        this.list=list;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_project, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        HotTop.DataBean dataBean=list.get(i);
        //背景图片
        String imgUrl= HttpConstant.IP+dataBean.getImgurl();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }
        holder.tvTitle.setText(dataBean.getName());
        holder.tvSize.setText(dataBean.getPlayCountDesc());
        switch (dataBean.getUpdateStatus()){
            case 0:
                holder.tvStatus.setText("即将开播");
                break;
            case 1:
                holder.tvStatus.setText(Html.fromHtml("更新至 <font color=\"#000000\">第" + dataBean.getEpisodeCount() + "集</font>"));
                break;
            case 2:
                holder.tvStatus.setText(Html.fromHtml("<font color=\"#000000\">全" + dataBean.getEpisodeCount() + "集</font>"));
                break;
        }

        /**
         * 跳转视频页面
         */
        holder.linClick.setTag(dataBean);
        holder.linClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HotTop.DataBean dataBean= (HotTop.DataBean) v.getTag();
                Intent intent=new Intent(activity, VideoPlayActivity.class);
                intent.putExtra("serialId",dataBean.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout linClick;
       OvalImageViews imgHead;
        TextView tvTitle,tvStatus,tvSize;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            linClick=itemView.findViewById(R.id.lin_click);
            imgHead=itemView.findViewById(R.id.img_head);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvStatus=itemView.findViewById(R.id.tv_status);
            tvSize=itemView.findViewById(R.id.tv_size);
        }
    }

}

