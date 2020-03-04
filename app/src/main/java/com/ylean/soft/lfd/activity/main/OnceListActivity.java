package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.OnceListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Project;
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
 * 往事精彩
 * Created by Administrator on 2020/2/7.
 */

public class OnceListActivity extends BaseActivity  implements MyRefreshLayoutListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    //页码
    private int page=1;
    //数据集合
    private List<Project.ProjectBean> listAll=new ArrayList<>();
    private OnceListAdapter onceListAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_once_list);
        ButterKnife.bind(this);
        initView();
        //获取专题列表
        reList.startRefresh();
        //返回
        imgBank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnceListActivity.this.finish();
            }
        });
    }

    /**
     * 初始化
     */
    private void initView() {
        tvTitle.setText("往事精彩");
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HandlerConstant.GET_PROJECT_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((Project) msg.obj);
                    break;
                case HandlerConstant.GET_PROJECT_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((Project) msg.obj);
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
    private void refresh(Project project){
        if(project==null){
            return;
        }
        if(project.isSussess()){
            List<Project.ProjectBean> list=project.getData();
            listAll.addAll(list);
            if(onceListAdapter==null){
                onceListAdapter=new OnceListAdapter(this,listAll);
                listView.setAdapter(onceListAdapter);
            }else{
                onceListAdapter.notifyDataSetChanged();
            }
            if(list.size()<HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
        }else{
            ToastUtil.showLong(project.getDesc());
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        getProject(HandlerConstant.GET_PROJECT_SUCCESS1);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        getProject(HandlerConstant.GET_PROJECT_SUCCESS2);
    }


    /**
     * 获取专题列表
     */
    public void getProject(int index) {
        HttpMethod.getProject(page, 10, index, handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
