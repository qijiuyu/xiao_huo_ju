package com.ylean.soft.lfd.adapter.user;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.activity.user.NewsDetailsActivity;
import com.ylean.soft.lfd.activity.web.WebViewActivity;
import com.zxdc.utils.library.bean.News;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends BaseAdapter {

    private Activity activity;
    private List<News.NewsBean> list;
    public NewsAdapter(Activity activity,List<News.NewsBean> list) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_news, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        News.NewsBean newsBean=list.get(position);
        holder.tvTitle.setText(newsBean.getTitle());
        if(newsBean.getJumpType()>0){
            String content=newsBean.getContent()+"<font color=\"#FF6D32\"><u>去查看＞</u></font>";
            holder.tvDes.setText(Html.fromHtml(content));
        }else{
            holder.tvDes.setText(Html.fromHtml(newsBean.getContent()));
        }
        holder.tvTime.setText(newsBean.getCreatetime());

        /**
         * 进入播放页面
         */
        holder.tvDes.setTag(newsBean);
        holder.tvDes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                News.NewsBean newsBean= (News.NewsBean) v.getTag();
                Intent intent=new Intent();
                switch (newsBean.getJumpType()){
                    case 0:
                         break;
                    case 1:
                        intent.setClass(activity, WebViewActivity.class);
                        intent.putExtra("url",newsBean.getLinkUrl());
                        intent.putExtra("title",newsBean.getTitle());
                        activity.startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(activity, NewsDetailsActivity.class);
                        intent.putExtra("newsBean",newsBean);
                        activity.startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(activity, VideoPlayActivity.class);
                        intent.putExtra("serialId",newsBean.getSerialId());
                        activity.startActivity(intent);
                        break;
                    case 4:
                        EventBus.getDefault().post(new EventBusType(EventStatus.GO_TO_CHANNEL,newsBean.getSerialId()));
                        activity.finish();
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_des)
        TextView tvDes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

