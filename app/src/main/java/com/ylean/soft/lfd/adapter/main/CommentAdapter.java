package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.Comment;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends BaseAdapter {

    private Activity activity;
    private List<Comment.CommentBean> list;
    private Map<Integer,Integer> praiseMap=new HashMap<>();
    public CommentAdapter(Activity activity,List<Comment.CommentBean> list) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_comment, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Comment.CommentBean commentBean=list.get(position);
        //用户头像
        String headUrl=commentBean.getUserImg();
        holder.imgHead.setTag(R.id.imageid,headUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && headUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(headUrl).into(holder.imgHead);
        }
        holder.tvName.setText(commentBean.getNickname());
        holder.tvContent.setText(Util.getSpanString(activity,commentBean.getContent()+"  ",commentBean.getCreatetimestr(),R.style.text1,R.style.text2));

        //区分已点赞与未点赞
        if(praiseMap.get(position)==null){
            holder.imgPraise.setImageResource(R.mipmap.no_praise);
        }else{
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
        }

        /**
         * 点赞
         */
        holder.imgPraise.setTag(position);
        holder.imgPraise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int index= (int) v.getTag();
                if(praiseMap.get(index)!=null){
                    praiseMap.remove(index);
                }else{
                    praiseMap.put(index,index);
                }
                notifyDataSetChanged();
                v.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
            }
        });

        /**
         * 回复
         */
        holder.tvContent.setTag(commentBean);
        holder.tvContent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag()==null){
                    return;
                }
                Comment.CommentBean commentBean= (Comment.CommentBean) v.getTag();
                EventBus.getDefault().post(new EventBusType(EventStatus.START_REPLY,commentBean));
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_head)
        CircleImageView imgHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_praise)
        ImageView imgPraise;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

