package com.ylean.soft.lfd.adapter.focus;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.zxdc.utils.library.bean.Browse;
import com.zxdc.utils.library.bean.SerialVideo;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LookHistoryAdapter extends BaseAdapter {

    private Activity activity;
    private List<Browse.BrowseBean> list;

    public LookHistoryAdapter(Activity activity,List<Browse.BrowseBean> list) {
        super();
        this.activity = activity;
        this.list=list;
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_my_look, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Browse.BrowseBean browseBean=list.get(position);
        //背景图片
        String imgUrl=browseBean.getSerialImg();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }
        holder.tvTitle.setText(browseBean.getSerialName());
        holder.tvBlues.setText(browseBean.getEpisodeName());
        if(browseBean.getSeconds()==0){
            holder.tvTime.setText("已完成观看");
        }else{
            holder.tvTime.setText("剩余 "+showTime(browseBean.getSeconds()));
        }


        /**
         * 进入视频详情页面
         */
        holder.imgHead.setTag(R.id.tag1,browseBean);
        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Browse.BrowseBean browseBean= (Browse.BrowseBean) v.getTag(R.id.tag1);
                Intent intent=new Intent(activity, VideoPlayActivity.class);
                intent.putExtra("singleId",browseBean.getEpisodeId());
                intent.putExtra("seconds",browseBean.getSeconds());
                activity.startActivity(intent);
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_head)
        OvalImageViews imgHead;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_blues)
        TextView tvBlues;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private String showTime(int seconds){
        final int hoursInt = seconds / 3600;
        final int minutesInt = (seconds - hoursInt * 3600) / 60;
        final int secondsInt = seconds - hoursInt * 3600 - minutesInt * 60;
        if(hoursInt==0){
            return String.format("%02d", minutesInt) + ":" + String.format("%02d", secondsInt);
        } else{
            return String.format("%02d", hoursInt) + ":" + String.format("%02d", minutesInt) + ":" + String.format("%02d", secondsInt);
        }
    }
}

