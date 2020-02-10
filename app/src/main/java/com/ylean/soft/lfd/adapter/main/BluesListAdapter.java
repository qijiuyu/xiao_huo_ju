package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.BluesListActivity;
import com.zxdc.utils.library.view.OvalImageViews;

public class BluesListAdapter extends RecyclerView.Adapter<BluesListAdapter.MyHolder> {

    private Activity activity;
    public BluesListAdapter(Activity activity) {
        this.activity = activity;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_blues, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;

    }

    @Override
    public int getItemCount() {
        return 15;
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

