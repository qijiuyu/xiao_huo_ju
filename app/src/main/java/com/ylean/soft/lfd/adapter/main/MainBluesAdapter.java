package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.view.OvalImageViews;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainBluesAdapter extends BaseAdapter {

    private Activity activity;
    private List<Tag.TagBean> list;

    public MainBluesAdapter(Activity activity, List<Tag.TagBean> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_main_blues, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Tag.TagBean tagBean=list.get(position);
        holder.tvName.setText(tagBean.getName());

        if(tagBean.getSerialList()!=null && tagBean.getSerialList().size()>0){
            holder.imgHead.setVisibility(View.VISIBLE);
            holder.listView.setVisibility(View.VISIBLE);

            //第一个剧集图片
            String imgUrl=tagBean.getSerialList().get(0).getImgurl();
            holder.imgHead.setTag(R.id.imageid,imgUrl);
            if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
                Glide.with(activity).load(imgUrl).into(holder.imgHead);
            }

            //显示列表
            LinearLayoutManager layoutManager=new LinearLayoutManager(activity);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.listView.setLayoutManager(layoutManager);
            holder.listView.setAdapter(new MainBluesDataAdapter(activity,list.get(position).getSerialList()));
        }else{
            holder.imgHead.setVisibility(View.GONE);
            holder.listView.setVisibility(View.GONE);
        }


        /**
         * 查看更多
         */
        holder.tvMore.setTag(tagBean.getId());
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int channelId= (int) v.getTag();
                EventBus.getDefault().post(new EventBusType(EventStatus.LOOK_MORE_MAIN_BLUES,channelId));
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_more)
        TextView tvMore;
        @BindView(R.id.img_head)
        OvalImageViews imgHead;
        @BindView(R.id.listView)
        RecyclerView listView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

