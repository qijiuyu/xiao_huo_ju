package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.Project;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

public class MainProjectAdapter extends RecyclerView.Adapter<MainProjectAdapter.MyHolder> {

    private Activity activity;
    private List<Project.ListData> list;
    public MainProjectAdapter(Activity activity,List<Project.ListData> list) {
        this.activity = activity;
        this.list=list;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_main_project, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        Project.ListData listData=list.get(i);
        //图片
        String imgUrl=listData.getImgurl();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }
        holder.tvBlues.setText("全"+listData.getEpisodeCount()+"集");
        //用户头像
        String headUrl=listData.getUserImg();
        holder.imgAuthor.setTag(R.id.imageid2,headUrl);
        if(holder.imgAuthor.getTag(R.id.imageid2)!=null && headUrl==holder.imgAuthor.getTag(R.id.imageid2)){
            Glide.with(activity).load(headUrl).into(holder.imgAuthor);
        }
        holder.tvSize.setText(listData.getPlayCount()+"w");
        holder.tvTitle.setText(listData.getName());
        holder.tvNum.setText("第"+listData.getEpisodeCount()+"集");
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        OvalImageViews imgHead;
        CircleImageView imgAuthor;
        TextView tvTitle,tvNum,tvSize,tvBlues;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgHead=itemView.findViewById(R.id.img_head);
            imgAuthor=itemView.findViewById(R.id.img_author);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvNum=itemView.findViewById(R.id.tv_num);
            tvSize=itemView.findViewById(R.id.tv_size);
            tvBlues=itemView.findViewById(R.id.tv_blues);
        }
    }

}

