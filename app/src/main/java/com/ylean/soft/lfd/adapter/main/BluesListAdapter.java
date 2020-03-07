package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.BluesListActivity;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.SerialVideo;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

public class BluesListAdapter extends RecyclerView.Adapter<BluesListAdapter.MyHolder> {

    private Activity activity;
    private List<SerialVideo.SerialVideoBean> list;
    public BluesListAdapter(Activity activity,List<SerialVideo.SerialVideoBean> list) {
        this.activity = activity;
        this.list=list;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_blues, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        SerialVideo.SerialVideoBean serialVideoBean=list.get(i);
        //背景图片
        String imgUrl=serialVideoBean.getImgurl();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }
        holder.tvBlues.setText(serialVideoBean.getName());

        /**
         * 进入视频详情页面
         */
        holder.imgHead.setTag(R.id.tag1,serialVideoBean);
        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SerialVideo.SerialVideoBean serialVideoBean= (SerialVideo.SerialVideoBean) v.getTag(R.id.tag1);
                Intent intent=new Intent(activity, VideoPlayActivity.class);
                intent.putExtra("singleId",serialVideoBean.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       OvalImageViews imgHead;
        TextView tvBlues;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgHead=itemView.findViewById(R.id.img_head);
            tvBlues=itemView.findViewById(R.id.tv_blues);
        }
    }

}

