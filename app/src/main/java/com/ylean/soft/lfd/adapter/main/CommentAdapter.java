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
import com.ylean.soft.lfd.activity.main.CommentActivity;
import com.zxdc.utils.library.bean.Comment;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.MeasureListView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends BaseAdapter {

    private CommentActivity activity;
    private List<Comment.CommentBean> list;
    //回复列表适配器
    private ReplyAdapter replyAdapter;
    public CommentAdapter(CommentActivity activity,List<Comment.CommentBean> list) {
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

        /**
         * 显示回复列表
         */
        if(commentBean.getReplyCount()>0){
            holder.replyList.setVisibility(View.VISIBLE);
            holder.replyList.setAdapter(replyAdapter=new ReplyAdapter(activity,commentBean.getReplyList()));
        }else{
            holder.replyList.setVisibility(View.GONE);
        }

        //显示或隐藏“查看更多”
        if(commentBean.getReplyCount()>1 && commentBean.getReplyCount()!=commentBean.getReplyList().size()){
            holder.tvMore.setVisibility(View.VISIBLE);
        }else{
            holder.tvMore.setVisibility(View.GONE);
        }


        /**
         * 点击查看更多
         */
        holder.tvMore.setTag(commentBean);
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Comment.CommentBean commentBean= (Comment.CommentBean) v.getTag();
                if(commentBean!=null){
                    activity.getReply(commentBean.getId());
                }
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
        @BindView(R.id.reply_list)
        MeasureListView replyList;
        @BindView(R.id.tv_more)
        TextView tvMore;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

