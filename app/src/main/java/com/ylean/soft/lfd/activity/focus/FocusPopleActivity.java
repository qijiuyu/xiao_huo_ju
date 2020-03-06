package com.ylean.soft.lfd.activity.focus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.focus.FocusPopleAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Focus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关注的人
 * Created by Administrator on 2020/2/7.
 */

public class FocusPopleActivity extends BaseActivity  implements MyRefreshLayoutListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    @BindView(R.id.tv_no)
    TextView tvNo;
    private FocusPopleAdapter focusPopleAdapter;
    //数据集合
    private List<Focus.FocusBean> listAll=new ArrayList<>();
    //页码
    private int page=1;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_pople);
        ButterKnife.bind(this);
        initView();
        //加载数据
        reList.startRefresh();

        //返回
        findViewById(R.id.img_bank).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FocusPopleActivity.this.finish();
            }
        });
    }

    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("关注的人");
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HandlerConstant.FOCUS_USER_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((Focus) msg.obj);
                    break;
                case HandlerConstant.FOCUS_USER_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((Focus) msg.obj);
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
     * 刷新界面数据
     */
    private void refresh(Focus focus){
        if(focus==null){
            return;
        }
        if(focus.isSussess()){
            List<Focus.FocusBean> list=focus.getData();
            listAll.addAll(list);
            //适配器遍历显示
            if(focusPopleAdapter==null){
                focusPopleAdapter=new FocusPopleAdapter(this,listAll);
                listView.setAdapter(focusPopleAdapter);
            }else{
                focusPopleAdapter.notifyDataSetChanged();
            }
            if(list.size()< HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
            if(listAll.size()==0){
                tvNo.setVisibility(View.VISIBLE);
            }
        }else{
            ToastUtil.showLong(focus.getDesc());
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        HttpMethod.focusUser(page, HandlerConstant.FOCUS_USER_SUCCESS1,handler);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.focusUser(page, HandlerConstant.FOCUS_USER_SUCCESS2,handler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
