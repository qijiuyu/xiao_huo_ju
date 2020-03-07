package com.ylean.soft.lfd.adapter.user;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.user.HelpDetailsActivity;
import com.zxdc.utils.library.bean.Help;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Help.HelpBean> list;

    public HelpListAdapter(Activity activity, List<Help.HelpBean> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_help, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Help.HelpBean helpBean = list.get(position);
        holder.tvName.setText(helpBean.getTitle());
        holder.tvName.setTag(helpBean);
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Help.HelpBean helpBean= (Help.HelpBean) v.getTag();
                Intent intent=new Intent(activity, HelpDetailsActivity.class);
                intent.putExtra("helpBean",helpBean);
                activity.startActivity(intent);
            }
        });
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

