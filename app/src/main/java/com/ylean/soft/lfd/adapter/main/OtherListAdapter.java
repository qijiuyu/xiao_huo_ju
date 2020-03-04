package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherListAdapter extends BaseAdapter {

    private Activity activity;
    private List<List<HotTop.DataBean>> listAll;
    public OtherListAdapter(Activity activity,List<List<HotTop.DataBean>> listAll) {
        super();
        this.activity = activity;
        this.listAll=listAll;
    }

    @Override
    public int getCount() {
        return listAll==null ? 0 : listAll.size();
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_other, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        List<HotTop.DataBean> list=listAll.get(position);
        if(list.size()==7){
            holder.relBottom.setVisibility(View.VISIBLE);
            HotTop.DataBean dataBean=list.get(list.size()-1);

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
        }else{
            holder.relBottom.setVisibility(View.GONE);
        }

        holder.listView.setLayoutManager(new GridLayoutManager(activity, 3));
        holder.listView.setAdapter(new OtherListDataAdapter(activity,list));
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.rel_bottom)
        RelativeLayout relBottom;
        @BindView(R.id.listView)
        RecyclerView listView;
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
        @BindView(R.id.tv_focus)
        TextView tvFocus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}

