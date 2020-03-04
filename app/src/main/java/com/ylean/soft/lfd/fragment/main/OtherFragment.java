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
import com.ylean.soft.lfd.activity.main.MainActivity;
import com.ylean.soft.lfd.adapter.main.OtherListAdapter;
import com.youth.banner.Banner;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
public class OtherFragment extends BaseFragment implements MyRefreshLayoutListener {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    Unbinder unbinder;
    //数据集合
    private List<HotTop.DataBean> listAll=new ArrayList<>();
    //解析后的集合数据
    private  List<List<HotTop.DataBean>> arrayList=new ArrayList<>();
    //页码
    private int page=1;
    //fragment是否可见
    private boolean isVisibleToUser=false;
    private OtherListAdapter otherListAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other, container, false);
        unbinder = ButterKnife.bind(this, view);
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
        //获取剧集数据
        serialList();
        return view;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((HotTop) msg.obj);
                    break;
                case HandlerConstant.GET_SERIAL_LIST_SUCCESS2:
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
            //将数据解析为7个对象为一组
            parsingData();
            //适配器遍历显示
            if(otherListAdapter==null){
                otherListAdapter=new OtherListAdapter(mActivity,arrayList);
                listView.setAdapter(otherListAdapter);
            }else{
                otherListAdapter.notifyDataSetChanged();
            }
            if(list.size()< HttpMethod.size){
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
        HttpMethod.serialList(getChannelId(),null,page, HandlerConstant.GET_SERIAL_LIST_SUCCESS1,handler);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.serialList(getChannelId(),null,page, HandlerConstant.GET_SERIAL_LIST_SUCCESS2,handler);
    }


    /**
     * 获取频道ID
     */
    private int getChannelId(){
        MainActivity activity=(MainActivity)mActivity;
        if(activity==null){
            return 0;
        }
        return activity.channelList.get(activity.pageIndex).getId();
    }


    /**
     * 将数据解析为7个对象为一组
     * @return
     */
    private void parsingData(){
        List<HotTop.DataBean> list=new ArrayList<>();
        for (int index = 0,len=listAll.size(); index<len; index++) {
             list.add(listAll.get(index));
             if((index+1)%7==0){
                 arrayList.add(list);
                 list=new ArrayList<>();
             }
        }
        if(list.size()>0){
            arrayList.add(list);
        }
    }


    /**
     * serialList
     */
    public void serialList(){
        handler.postDelayed(new Runnable() {
            public void run() {
                if(isVisibleToUser && view!=null && listAll.size()==0){
                    reList.startRefresh();
                }
            }
        },200);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        //获取剧集数据
        serialList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
