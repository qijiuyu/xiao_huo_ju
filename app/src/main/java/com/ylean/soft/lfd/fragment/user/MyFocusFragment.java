package com.ylean.soft.lfd.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.AuthorDetailsActivity;
import com.ylean.soft.lfd.activity.user.UserActivity;
import com.ylean.soft.lfd.adapter.focus.FocusPopleAdapter;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.Focus;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MeasureListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class MyFocusFragment extends BaseFragment{

    @BindView(R.id.listView)
    MeasureListView listView;
    Unbinder unbinder;
    @BindView(R.id.tv_no)
    TextView tvNo;
    private FocusPopleAdapter focusPopleAdapter;
    //数据集合
    private List<Focus.FocusBean> listAll = new ArrayList<>();
    private List<Focus.FocusBean> list = new ArrayList<>();
    //页码
    private int page = 1;
    //fragment是否可见
    private boolean isVisibleToUser = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus
        EventBus.getDefault().register(this);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_focus, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((UserActivity) mActivity).pager.setObjectForPosition(view, 1);

        listView.setDivider(null);
        focusPopleAdapter = new FocusPopleAdapter(mActivity, listAll);
        listView.setAdapter(focusPopleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mActivity, AuthorDetailsActivity.class);
            }
        });

        //加载数据
        if(isVisibleToUser && view!=null && listAll.size()==0){
            focusUser();
        }
        return view;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            //加载完成
            EventBus.getDefault().post(new EventBusType(EventStatus.USER_LOAD_MORE_SUCCESS));
            switch (msg.what){
                case HandlerConstant.FOCUS_USER_SUCCESS1:
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
            focusPopleAdapter.notifyDataSetChanged();
            if(listAll.size()==0){
                tvNo.setVisibility(View.VISIBLE);
            }else{
                tvNo.setVisibility(View.GONE);
            }
            if(page==1){
                EventBus.getDefault().post(new EventBusType(EventStatus.USER_POSITION_TOP));
            }
        }else{
            ToastUtil.showLong(focus.getDesc());
        }
    }


    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            case EventStatus.USER_LOAD_MORE:
                if(list.size()< HttpMethod.size){
                    //加载完成
                    EventBus.getDefault().post(new EventBusType(EventStatus.USER_LOAD_MORE_SUCCESS));
                    return;
                }
                //加载数据
                if(isVisibleToUser && view!=null){
                    page++;
                    focusUser();
                }
                break;
            //加载数据
            case EventStatus.USER_CLEAR_DATA:
                if(listAll.size()>0){
                    listAll.clear();
                    focusUser();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 查询我的关注
     */
    private void focusUser(){
        HttpMethod.focusUser(page, HandlerConstant.FOCUS_USER_SUCCESS1,handler);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        //加载数据
        if(isVisibleToUser && view!=null && listAll.size()==0){
            focusUser();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
