package com.ylean.soft.lfd.utils.channel;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2020/2/10.
 */
public class ChannerAdapter extends RecyclerView.Adapter<MyViewHolder> implements TouchInterface {

    private Context context;
    private ItemTouchHelper touchHelper;
    private List<DataBean> list;
    /**
     * true：点击直接拖拽
     * false：只能长按拖拽
     */
    private boolean isClick=false;
    //是否抖动
    public boolean isJitter=false;
    //抖动动画
    private Animation shake;
    public ChannerAdapter(Context context, List<DataBean> list) {
        this.context = context;
        this.list = list;
        shake = AnimationUtils.loadAnimation(context, R.anim.jitter);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false));
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_des.setText(list.get(position).name);
        if(isJitter){
            holder.tv_des.setAnimation(shake);
        }
        holder.tv_des.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
                vibrator.vibrate(70);
                return true;
            }
        });

        holder.tv_des.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!isClick){
                        return false;
                    }
                    touchHelper.startDrag(holder);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onMove(int currentPosition, int targetPosition) {
        Collections.swap(list, currentPosition, targetPosition);
        if (targetPosition < currentPosition) {
            List<DataBean> subList = list.subList(targetPosition + 1, currentPosition + 1);
            //向右移一位
            DataUtils.rightStepList(0, subList);
        } else {
            List<DataBean> subList = list.subList(currentPosition, targetPosition);
            //向左移一位
            DataUtils.leftStepList(0, subList);
        }
        notifyItemMoved(currentPosition, targetPosition);
    }


    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper=touchHelper;
    }

    public void setOnClick(boolean isClick){
        this.isClick=isClick;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_des;
    public ImageView imgTag;
    public MyViewHolder(View itemView) {
        super(itemView);
        tv_des =itemView.findViewById(R.id.text_item);
        imgTag=itemView.findViewWithTag(R.id.img_tag);
    }
}

