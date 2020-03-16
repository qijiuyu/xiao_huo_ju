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
import com.ylean.soft.lfd.activity.main.AuthorDetailsActivity;
import com.zxdc.utils.library.bean.Author;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

public class MainAuthorAdapter extends RecyclerView.Adapter<MainAuthorAdapter.MyHolder> {

    private Activity activity;
    private List<Author.DataBean> list;
    public MainAuthorAdapter(Activity activity,List<Author.DataBean> list) {
        this.activity = activity;
        this.list=list;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_main_author, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        Author.DataBean dataBean=list.get(i);
        if(dataBean==null){
            return;
        }
        holder.tvName.setText(dataBean.getNickname());
        Glide.with(activity).load(HttpConstant.IP+dataBean.getImgurl()).into(holder.imgHead);

        /**
         * 进入作者详情
         */
        holder.imgHead.setTag(R.id.tag1,dataBean.getId());
        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag(R.id.tag1)==null){
                    return;
                }
                int id= (int) v.getTag(R.id.tag1);
                Intent intent=new Intent(activity, AuthorDetailsActivity.class);
                intent.putExtra("id",id);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       CircleImageView imgHead;
        TextView tvName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgHead=itemView.findViewById(R.id.img_head);
            tvName=itemView.findViewById(R.id.tv_name);
        }
    }

}

