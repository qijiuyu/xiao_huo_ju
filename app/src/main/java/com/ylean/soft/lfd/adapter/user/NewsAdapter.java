package com.ylean.soft.lfd.adapter.user;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylean.soft.lfd.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends BaseAdapter {

    private Activity activity;

    public NewsAdapter(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 7;
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
         String des="您关注的暖男先生的霸道总裁超粘人更新了，快点去看看吧<font color=\"#FF6D32\"><u>去查看＞</u></font>";
        holder.tvDes.setText(Html.fromHtml(des));
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

