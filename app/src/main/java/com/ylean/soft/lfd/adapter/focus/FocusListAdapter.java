package com.ylean.soft.lfd.adapter.focus;

import android.app.Activity;
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

public class FocusListAdapter extends BaseAdapter {

    private Activity activity;

    public FocusListAdapter(Activity activity) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_focus_data, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }


    static class ViewHolder {
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

