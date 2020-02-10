package com.ylean.soft.lfd.adapter.main;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ylean.soft.lfd.R;

/**
 * 商品列表adapter
 */
public class MainTypeAdapter extends RecyclerView.Adapter<MainTypeAdapter.MyHolder> {

    private Context context;
    //选中的item
    public int index;
    private OnItemClickListener onItemClickListener;
    public MainTypeAdapter(Context context,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int index);
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_main_type, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;
        if(i==index){
//            holder.tvName.setTextSize(17);
            holder.viewLine.setVisibility(View.VISIBLE);
            holder.tvName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
//            holder.tvName.setTextSize(16);
            holder.viewLine.setVisibility(View.GONE);
            holder.tvName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        holder.tvName.setTag(i);
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag()==null){
                    return;
                }
                index= (int) v.getTag();
                notifyDataSetChanged();
                onItemClickListener.onItemClick(index);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        View viewLine;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_name);
            viewLine=itemView.findViewById(R.id.view_line);
        }
    }
}

