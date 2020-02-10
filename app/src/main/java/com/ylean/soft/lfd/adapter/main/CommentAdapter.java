package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.view.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends BaseAdapter {

    private Activity activity;
    private Map<Integer,Integer> praiseMap=new HashMap<>();
    public CommentAdapter(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 10;
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

        if(praiseMap.get(position)==null){
            holder.imgPraise.setImageResource(R.mipmap.no_praise);
        }else{
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
        }

        holder.imgPraise.setTag(position);
        holder.imgPraise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int index= (int) v.getTag();
                if(praiseMap.get(index)!=null){
                    praiseMap.remove(index);
                }else{
                    praiseMap.put(index,index);
                }
                notifyDataSetChanged();
                ((ImageView)v).setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
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

