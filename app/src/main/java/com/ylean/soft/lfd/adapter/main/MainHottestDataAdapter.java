package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainHottestDataAdapter extends BaseAdapter {

    private Activity activity;

    public MainHottestDataAdapter(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 3;
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
        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(activity, VideoPlayActivity.class);
                activity.startActivity(intent);
            }
        });
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
        @BindView(R.id.tv_des)
        TextView tvDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

