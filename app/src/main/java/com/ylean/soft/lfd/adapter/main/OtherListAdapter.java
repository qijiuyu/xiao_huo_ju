package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.OvalImageViews;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherListAdapter extends BaseAdapter {

    private Activity activity;

    public OtherListAdapter(Activity activity) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_other, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.listView.setLayoutManager(new GridLayoutManager(activity, 3));
        holder.listView.setAdapter(new OtherListDataAdapter(activity));
        return view;
    }


    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

