package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

public class MainOnlineAdapter extends RecyclerView.Adapter<MainOnlineAdapter.MyHolder> {

    private Activity activity;
    public MainOnlineAdapter(Activity activity) {
        this.activity = activity;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_main_online, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       OvalImageViews imgHead;
        CircleImageView imgPic;
        TextView tvTitle,tvName;
        RelativeLayout relBespoke;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgHead=itemView.findViewById(R.id.img_head);
            imgPic=itemView.findViewById(R.id.img_pic);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvName=itemView.findViewById(R.id.tv_name);
            relBespoke=itemView.findViewById(R.id.rel_bespoke);
        }
    }

}

