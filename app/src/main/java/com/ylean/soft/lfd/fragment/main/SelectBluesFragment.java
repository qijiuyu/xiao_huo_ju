package com.ylean.soft.lfd.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.SelectBluesAdapter;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.SerialVideo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectBluesFragment extends BaseFragment implements MyRefreshLayoutListener {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    Unbinder unbinder;
    //页码
    private int page = 1;
    private SelectBluesAdapter selectBluesAdapter;
    //剧情id
    private int serialId;
    private List<SerialVideo.SerialVideoBean> listAll = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus
        EventBus.getDefault().register(this);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_blues, container, false);
        unbinder = ButterKnife.bind(this, view);

        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        reList.setPullDownRefreshEnable(false);
        selectBluesAdapter=new SelectBluesAdapter(mActivity,listAll);
        listView.setAdapter(selectBluesAdapter);

        //点击空白处
        view.findViewById(R.id.rel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusType(EventStatus.CLOSE_VIDEO_RIGHT));
            }
        });
        return view;
    }


    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerConstant.GET_SERIAL_VIDEO_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((SerialVideo) msg.obj);
                    break;
                case HandlerConstant.GET_SERIAL_VIDEO_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((SerialVideo) msg.obj);
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
    private void refresh(SerialVideo serialVideo) {
        if (serialVideo == null) {
            return;
        }
        if (serialVideo.isSussess()) {
            List<SerialVideo.SerialVideoBean> list = serialVideo.getData();
            listAll.addAll(list);
            selectBluesAdapter.notifyDataSetChanged();
            if (list.size() < HttpMethod.size) {
                reList.setIsLoadingMoreEnabled(false);
            }
        } else {
            ToastUtil.showLong(serialVideo.getDesc());
        }
    }


    /**
     * 下刷
     *
     * @param view
     */
    public void onRefresh(View view) {
        page = 1;
        HttpMethod.getSerialVideo(0, page, serialId, HandlerConstant.GET_SERIAL_VIDEO_SUCCESS1, handler);
    }

    /**
     * 上拉加载更多
     *
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.getSerialVideo(0, page, serialId, HandlerConstant.GET_SERIAL_VIDEO_SUCCESS2, handler);
    }



    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            //开始查询剧集列表
            case EventStatus.SELECT_BLUES:
                  page=1;
                  listAll.clear();
                  selectBluesAdapter.notifyDataSetChanged();

                  serialId= (int) eventBusType.getObject();
                  //获取专题列表
                  reList.startRefresh();
                  break;
            default:
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
