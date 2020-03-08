package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.user.NewsAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.News;
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
 * 消息
 * Created by Administrator on 2020/2/8.
 */

public class NewsActivity extends BaseActivity  implements MyRefreshLayoutListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    private NewsAdapter newsAdapter;
    //页码
    private int page=1;
    //数据集合
    private List<News.NewsBean> listAll=new ArrayList<>();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        initView();
        //加载数据
        reList.startRefresh();
    }


    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("消息");
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        newsAdapter=new NewsAdapter(this,listAll);
        listView.setDivider(null);
        listView.setAdapter(newsAdapter);

        //返回
        findViewById(R.id.img_bank).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NewsActivity.this.finish();
            }
        });
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HandlerConstant.GET_NEWS_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((News) msg.obj);
                    break;
                case HandlerConstant.GET_NEWS_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((News) msg.obj);
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
    private void refresh(News news){
        if(news==null){
            return;
        }
        if(news.isSussess()){
            List<News.NewsBean> list=news.getData();
            listAll.addAll(list);
            //展示列表
            newsAdapter.notifyDataSetChanged();
            if(list.size()< HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
        }else{
            ToastUtil.showLong(news.getDesc());
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        HttpMethod.getNews(page, HandlerConstant.GET_NEWS_SUCCESS1,handler);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.getNews(page, HandlerConstant.GET_NEWS_SUCCESS2,handler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
