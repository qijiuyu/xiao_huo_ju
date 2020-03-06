package com.ylean.soft.lfd.adapter.focus;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.Focus;
import com.zxdc.utils.library.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FocusPopleAdapter extends BaseAdapter {

    private Activity activity;
    private List<Focus.FocusBean> list;
    public FocusPopleAdapter(Activity activity,List<Focus.FocusBean> list) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_focus_pople, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Focus.FocusBean focusBean=list.get(position);
        //用户头像
        String headUrl=focusBean.getImgurl();
        holder.imgHead.setTag(R.id.imageid2,headUrl);
        if(holder.imgHead.getTag(R.id.imageid2)!=null && headUrl==holder.imgHead.getTag(R.id.imageid2)){
            Glide.with(activity).load(headUrl).into(holder.imgHead);
        }
        holder.tvName.setText(focusBean.getNickname());
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.img_head)
        CircleImageView imgHead;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_focus)
        TextView tvFocus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

