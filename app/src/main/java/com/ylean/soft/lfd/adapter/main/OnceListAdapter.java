package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylean.soft.lfd.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnceListAdapter extends BaseAdapter {

    private Activity activity;

    public OnceListAdapter(Activity activity) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_once, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.listView.setLayoutManager(layoutManager);
        holder.listView.setAdapter(new OnceListDataAdapter(activity));
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_more_project)
        TextView tvMoreProject;
        @BindView(R.id.listView)
        RecyclerView listView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

