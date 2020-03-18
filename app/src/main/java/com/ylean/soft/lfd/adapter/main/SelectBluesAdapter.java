package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.SerialVideo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.view.OvalImageViews;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectBluesAdapter extends BaseAdapter {

    private Activity activity;
    private List<SerialVideo.SerialVideoBean> lis;
    public SelectBluesAdapter(Activity activity,List<SerialVideo.SerialVideoBean> lis) {
        super();
        this.activity = activity;
        this.lis=lis;
    }

    @Override
    public int getCount() {
        return lis==null ? 0 : lis.size();
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_blues, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SerialVideo.SerialVideoBean serialVideoBean=lis.get(position);
        //背景图片
        String imgUrl= HttpConstant.IP+serialVideoBean.getImgurl();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }
        String name=serialVideoBean.getName();
        if(!TextUtils.isEmpty(name)){
            String[] strings=name.split("：");
            if(strings!=null && strings.length>0){
                holder.tvBlues.setText(strings[0]);
            }
        }


        /**
         * 选择播放
         */
        holder.imgHead.setTag(R.id.tag1,serialVideoBean.getId());
        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int id= (int) v.getTag(R.id.tag1);
                EventBus.getDefault().post(new EventBusType(EventStatus.SELECT_SINGLE_PLAY,id));
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_head)
        OvalImageViews imgHead;
        @BindView(R.id.tv_blues)
        TextView tvBlues;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

