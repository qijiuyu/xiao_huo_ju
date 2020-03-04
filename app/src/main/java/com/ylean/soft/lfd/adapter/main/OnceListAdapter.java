package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.ProjectListActivity;
import com.zxdc.utils.library.bean.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnceListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Project.ProjectBean> list;
    public OnceListAdapter(Activity activity,List<Project.ProjectBean> list) {
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_once, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Project.ProjectBean projectBean=list.get(position);
        holder.tvTitle.setText(projectBean.getName());

        LinearLayoutManager layoutManager=new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.listView.setLayoutManager(layoutManager);
        holder.listView.setAdapter(new OnceListDataAdapter(activity,projectBean.getSerialList()));

        /**
         * 查看更多
         */
        holder.tvMoreProject.setTag(projectBean);
        holder.tvMoreProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Project.ProjectBean projectBean= (Project.ProjectBean) v.getTag();
                Intent intent=new Intent(activity,ProjectListActivity.class);
                intent.putExtra("projectBean",projectBean);
                activity.startActivity(intent);
            }
        });
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

