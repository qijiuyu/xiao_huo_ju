package com.ylean.soft.lfd.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.user.UserActivity;
import com.ylean.soft.lfd.adapter.user.MyLikeAdapter;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyLikeFragment extends BaseFragment{

    @BindView(R.id.listView)
    RecyclerView listView;
    Unbinder unbinder;
    private MyLikeAdapter myLikeAdapter;
    //数据集合
    private List<HotTop.DataBean> listAll = new ArrayList<>();
    private List<HotTop.DataBean> list = new ArrayList<>();
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
        view = inflater.inflate(R.layout.fragment_mylike, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((UserActivity) mActivity).pager.setObjectForPosition(view, 0);

        myLikeAdapter = new MyLikeAdapter(mActivity,listAll);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        listView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        listView.setAdapter(myLikeAdapter);

        //加载数据
        if(isVisibleToUser && view!=null && listAll.size()==0){
            mylike();
        }
        return view;
    }


    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            //加载完成
            EventBus.getDefault().post(new EventBusType(EventStatus.USER_LOAD_MORE_SUCCESS));
            switch (msg.what) {
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS1:
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
    private void refresh(HotTop hotTop) {
        if (hotTop == null) {
            return;
        }
        if (hotTop.isSussess()) {
            list = hotTop.getData();
            listAll.addAll(list);
            //适配器遍历显示
            myLikeAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showLong(hotTop.getDesc());
        }
    }


    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            case EventStatus.USER_LOAD_MORE:
                LogUtils.e("+++++++++++++++++++++++123");
//                 if(list.size()< HttpMethod.size){
//                     //加载完成
//                     EventBus.getDefault().post(new EventBusType(EventStatus.USER_LOAD_MORE_SUCCESS));
//                     return;
//                 }
                 //加载数据
                 if(isVisibleToUser && view!=null){
                     page++;
                     mylike();
                 }
                  break;
            default:
                break;
        }
    }



    /**
     * 查询我都喜欢的数据
     */
    private void mylike(){
        HttpMethod.mylike(page, HandlerConstant.GET_SERIAL_LIST_SUCCESS1, handler);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        //加载数据
        if(isVisibleToUser && view!=null && listAll.size()==0){
            mylike();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
