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
import com.ylean.soft.lfd.activity.main.AuthorDetailsActivity;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

public class MainAuthorAdapter extends RecyclerView.Adapter<MainAuthorAdapter.MyHolder> {

    private Activity activity;
    public MainAuthorAdapter(Activity activity) {
        this.activity = activity;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_main_author, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;

        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(activity, AuthorDetailsActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
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

