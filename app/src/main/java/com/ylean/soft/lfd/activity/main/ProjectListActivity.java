package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.ProjectListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.HotTop;
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
import butterknife.OnClick;

/**
 * 热门专题
 * Created by Administrator on 2020/2/7.
 */

public class ProjectListActivity extends BaseActivity  implements MyRefreshLayoutListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    //数据集合
    private List<HotTop.DataBean> listAll=new ArrayList<>();
    //页码
    private int page=1;
    //专题对象
    private Project.ProjectBean projectBean;
    private ProjectListAdapter projectListAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        ButterKnife.bind(this);
        initView();
        //获取专题列表
        reList.startRefresh();
    }


    /**
     * 初始化
     */
    private void initView(){
        tvRight.setText("往期精彩");
        projectBean= (Project.ProjectBean) getIntent().getSerializableExtra("projectBean");
        if(projectBean!=null){
            tvTitle.setText(projectBean.getName());
        }
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
    }

    @OnClick({R.id.img_bank, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                 finish();
                break;
            //往事精彩
            case R.id.tv_right:
                setClass(OnceListActivity.class);
                break;
            default:
                break;
        }
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HandlerConstant.GET_PROJECT_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((HotTop) msg.obj);
                    break;
                case HandlerConstant.GET_PROJECT_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((HotTop) msg.obj);
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
    private void refresh(HotTop hotTop){
        if(hotTop==null){
            return;
        }
        if(hotTop.isSussess()){
            List<HotTop.DataBean> list=hotTop.getData();
            listAll.addAll(list);
            if(projectListAdapter==null){
                projectListAdapter=new ProjectListAdapter(this,listAll);
                listView.setLayoutManager(new GridLayoutManager(this, 3));
                listView.setAdapter(projectListAdapter);
            }else{
                projectListAdapter.notifyDataSetChanged();
            }
            if(list.size()<HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
        }else{
            ToastUtil.showLong(hotTop.getDesc());
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        getProjectList(HandlerConstant.GET_PROJECT_SUCCESS1);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        getProjectList(HandlerConstant.GET_PROJECT_SUCCESS2);
    }


    /**
     * 获取专题列表
     * @param index
     */
    private void getProjectList(int index){
        if(projectBean!=null){
            HttpMethod.getProjectList(projectBean.getId(),page,index,handler);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
