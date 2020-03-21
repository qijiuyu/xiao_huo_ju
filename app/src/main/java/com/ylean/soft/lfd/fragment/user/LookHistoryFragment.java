package com.ylean.soft.lfd.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.user.UserActivity;
import com.ylean.soft.lfd.adapter.focus.LookHistoryAdapter;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Browse;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MeasureListView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class LookHistoryFragment extends BaseFragment {


    @BindView(R.id.listView)
    MeasureListView listView;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    Unbinder unbinder;
    private LookHistoryAdapter lookHistoryAdapter;
    //数据集合
    private List<Browse.BrowseBean> listAll = new ArrayList<>();
    private List<Browse.BrowseBean> list = new ArrayList<>();
    //页码
    private int page = 1;
    //fragment是否可见
    private boolean isVisibleToUser = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_look, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((UserActivity)mActivity).pager.setObjectForPosition(view,2);

        listView.setDivider(null);
        lookHistoryAdapter=new LookHistoryAdapter(mActivity,listAll);
        listView.setAdapter(lookHistoryAdapter);

        /**
         * 一键清除
         */
        tvClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearLook();
            }
        });

        //加载数据
        if(isVisibleToUser && view!=null && listAll.size()==0){
            getBrowse();
        }
        return view;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            //加载完成
            EventBus.getDefault().post(new EventBusType(EventStatus.USER_LOAD_MORE_SUCCESS));
            switch (msg.what){
                case HandlerConstant.GET_BROWSE_SUCCESS:
                    refresh((Browse) msg.obj);
                    break;
                //一键清除
                case HandlerConstant.CLEAR_LOOK_SUCCESS:
                      BaseBean baseBean= (BaseBean) msg.obj;
                      if(baseBean==null){
                          break;
                      }
                      if(baseBean.isSussess()){
                          listAll.clear();
                          if(lookHistoryAdapter!=null){
                              lookHistoryAdapter.notifyDataSetChanged();
                          }
                          tvClear.setVisibility(View.GONE);
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
     * 刷新界面数据
     */
    private void refresh(Browse browse){
        if(browse==null){
            return;
        }
        if(browse.isSussess()){
            List<Browse.BrowseBean> list=browse.getData();
            listAll.addAll(list);
            //适配器遍历显示
            lookHistoryAdapter.notifyDataSetChanged();
            if(listAll.size()==0){
                tvNo.setVisibility(View.VISIBLE);
                tvClear.setVisibility(View.GONE);
            }else{
                tvNo.setVisibility(View.GONE);
                tvClear.setVisibility(View.VISIBLE);
            }
            if(page==1){
                EventBus.getDefault().post(new EventBusType(EventStatus.USER_POSITION_TOP));
            }
        }else{
            ToastUtil.showLong(browse.getDesc());
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
                    getBrowse();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 获取浏览记录
     */
    private void getBrowse(){
        HttpMethod.getBrowse(page, HandlerConstant.GET_BROWSE_SUCCESS,handler);
    }


    /**
     * 一键清除
     */
    private void clearLook(){
        if(listAll.size()==0){
            return;
        }
        DialogUtil.showProgress(mActivity,"清除中");
        HttpMethod.clearLook(handler);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        //加载数据
        if(isVisibleToUser && view!=null && listAll.size()==0){
            getBrowse();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
