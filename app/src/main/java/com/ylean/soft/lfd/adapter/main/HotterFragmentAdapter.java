package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotterFragmentAdapter extends BaseAdapter {

    private Activity activity;
    private List<HotTop.DataBean> list;

    public HotterFragmentAdapter(Activity activity,List<HotTop.DataBean> list) {
        super();
        this.activity = activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list==null ? 0 : list.size()-1;
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_more_hotter, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HotTop.DataBean dataBean=list.get(position+1);
        switch (position){
            case 0:
                 holder.imgNum.setImageResource(R.mipmap.dier);
                holder.imgNum.setVisibility(View.VISIBLE);
                 break;
            case 1:
                holder.imgNum.setImageResource(R.mipmap.disan);
                holder.imgNum.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.imgNum.setImageResource(R.mipmap.disi);
                holder.imgNum.setVisibility(View.VISIBLE);
                break;
            default:
                holder.imgNum.setVisibility(View.INVISIBLE);
                break;
        }
        //背景图片
        String imgUrl=dataBean.getImgurl();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }
        holder.tvTitle.setText(dataBean.getName());
        //用户头像
        String headUrl=dataBean.getUserImg();
        holder.imgPic.setTag(R.id.imageid2,headUrl);
        if(holder.imgPic.getTag(R.id.imageid2)!=null && headUrl==holder.imgPic.getTag(R.id.imageid2)){
            Glide.with(activity).load(headUrl).into(holder.imgPic);
        }
        holder.tvName.setText(dataBean.getUserNickName());
        if(dataBean.isFollowUser()){
            holder.tvFocus.setText("已关注");
        }else{
            holder.tvFocus.setText("未关注");
        }
        holder.tvSize.setText(dataBean.getPlayCount()+"w");
        holder.tvBlues.setText("第"+dataBean.getEpisodeCount()+"集");
        holder.tvType.setText(dataBean.getChannelName());
        holder.tvDes.setText(dataBean.getIntroduction());
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_num)
        ImageView imgNum;
        @BindView(R.id.img_head)
        OvalImageViews imgHead;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.img_pic)
        CircleImageView imgPic;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_blues)
        TextView tvBlues;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_des)
        TextView tvDes;
        @BindView(R.id.tv_focus)
        TextView tvFocus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

