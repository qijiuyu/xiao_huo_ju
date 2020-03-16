package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.CommentActivity;
import com.zxdc.utils.library.bean.Comment;
import com.zxdc.utils.library.bean.Reply;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.MeasureListView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReplyAdapter extends BaseAdapter {

    private CommentActivity activity;
    private List<Reply> list;
    //评论对象
    private Comment comment;
    public ReplyAdapter(CommentActivity activity, List<Reply> list,Comment comment) {
        super();
        this.activity = activity;
        this.list=list;
        this.comment=comment;
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_reply, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Reply reply=list.get(position);
        //用户头像
        String headUrl= HttpConstant.IP+reply.getUserImg();
        holder.imgHead.setTag(R.id.imageid,headUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && headUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(headUrl).into(holder.imgHead);
        }
        holder.tvName.setText(reply.getNickname());
        String content;
        if(TextUtils.isEmpty(reply.getBeNickName())){
            content=reply.getContent();
        }else{
            content="回复  "+reply.getBeNickName()+"："+reply.getContent();
        }
        holder.tvContent.setText(Util.getSpanString(activity,content+"  ",reply.getCreatetimestr(),R.style.text3,R.style.text4));
        if(reply.isThumbComment()){
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
        }else{
            holder.imgPraise.setImageResource(R.mipmap.no_praise);
        }
        holder.tvNum.setText(String.valueOf(reply.getThumbCount()));

        /**
         * 二级回复
         */
        holder.tvContent.setTag(reply);
        holder.tvContent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag()==null){
                    return;
                }
                Reply reply= (Reply) v.getTag();
                activity.showSendReply(2,comment,reply);
            }
        });


        /**
         * 回复点赞
         */
        holder.imgPraise.setTag(reply);
        holder.imgPraise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Reply reply= (Reply) v.getTag();
                activity.replyPrise(reply);

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

