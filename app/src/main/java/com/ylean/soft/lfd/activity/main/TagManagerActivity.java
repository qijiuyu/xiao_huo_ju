package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.init.LoginActivity;
import com.ylean.soft.lfd.adapter.main.ChannerAdapter;
import com.ylean.soft.lfd.utils.AnimUtil;
import com.ylean.soft.lfd.utils.channel.MyItemTouchCallBack;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.SortChannel;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 频道管理
 * Created by Administrator on 2020/2/5.
 */

public class TagManagerActivity extends BaseActivity {

    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private ChannerAdapter channerAdapter;
    //频道集合
    private List<Tag.TagBean> list=new ArrayList<>();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);
        ButterKnife.bind(this);
        initView();
        //获取频道列表
        channel();
    }

    /**
     * 初始化
     */
    private void initView(){
        listView.setLayoutManager(new GridLayoutManager(this, 4));
        channerAdapter=new ChannerAdapter(this, list);
        //关联ItemTouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(new MyItemTouchCallBack(channerAdapter));
        touchHelper.attachToRecyclerView(listView);
        channerAdapter.setTouchHelper(touchHelper);
        listView.setAdapter(channerAdapter);
    }

    @OnClick({R.id.img_bank, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                 finish();
                break;
            //编辑/完成
            case R.id.tv_right:
                 if(tvRight.getText().toString().trim().equals("编辑")){
                     tvRight.setText("完成");
                     channerAdapter.setOnClick(true);
                 }else{
                     //判断是否登录了
                     if(!MyApplication.isLogin()){
                         setClass(LoginActivity.class);
                         return;
                     }
                     List<SortChannel> sortList=new ArrayList<>();
                     for (int i=0;i<list.size();i++){
                         SortChannel sortChannel=new SortChannel();
                         sortChannel.setChannelId(list.get(i).getId());
                         sortChannel.setSort(i+1);
                         sortList.add(sortChannel);
                     }
                     //频道排序
                     sortChannel(SPUtil.gson.toJson(sortList));
                 }
                break;
            default:
                break;
        }
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //获取频道列表
                case HandlerConstant.GET_CHANNEL_SUCCESS:
                     final Tag tag= (Tag) msg.obj;
                     if(tag==null){
                         break;
                     }
                     if(tag.isSussess()){
                         list.addAll(tag.getData());
                         channerAdapter.notifyDataSetChanged();
                     }else{
                         ToastUtil.showLong(tag.getDesc());
                     }
                      break;
                //排序回执
                case HandlerConstant.SORT_CHANNEL_SUCCESS:
                      BaseBean baseBean= (BaseBean) msg.obj;
                      if(baseBean==null){
                          break;
                      }
                      if(baseBean.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.CHANNEL_SORT_SUCCESS,list));
                          finish();
                      }
                      ToastUtil.showLong(baseBean.getDesc());
                      break;
                case HandlerConstant.REQUST_ERROR:
                     ToastUtil.showLong(msg.obj.toString());
                     break;
                default:
                    break;
            }
            return false;
        }
    });


    /**
     * 设置按钮状态
     */
    public void editBtn(){
        if(tvRight.getText().toString().trim().equals("编辑")){
            tvRight.setText("完成");
            channerAdapter.setOnClick(true);

        }
    }

    /**
     * 获取频道列表
     */
    private void channel(){
        DialogUtil.showProgress(this,"数据加载中");
        HttpMethod.channel("0",handler);
    }

    /**
     * 频道排序
     */
    private void sortChannel(String json){
        DialogUtil.showProgress(this,"数据加载中");
        HttpMethod.sortChannel(json,handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
