package com.ylean.soft.lfd.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.MoreAuthorAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Author;
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
 * 更多作者
 * Created by Administrator on 2020/3/19.
 */
public class MoreAuthorActivity extends BaseActivity   implements MyRefreshLayoutListener {

    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    //数据集合
    private List<Author.DataBean> listAll=new ArrayList<>();
    //页码
    private int page=1;
    private MoreAuthorAdapter moreAuthorAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_author);
        ButterKnife.bind(this);
        initView();
        //加载数据
        reList.startRefresh();
        //返回
        imgBank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MoreAuthorActivity.this.finish();
            }
        });
    }


    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("热门作者");
        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        listView.setDivider(null);
        moreAuthorAdapter=new MoreAuthorAdapter(this,listAll);
        listView.setAdapter(moreAuthorAdapter);
    }

    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case HandlerConstant.HOT_AUTHOR_SUCCESS1:
                    reList.refreshComplete();
                    listAll.clear();
                    refresh((Author) msg.obj);
                    break;
                case HandlerConstant.HOT_AUTHOR_SUCCESS2:
                    reList.loadMoreComplete();
                    refresh((Author) msg.obj);
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
    private void refresh(final Author author){
        if(author==null){
            return;
        }
        if(author.isSussess()){
            List<Author.DataBean> list=author.getData();
            listAll.addAll(list);
            //适配器遍历显示
            moreAuthorAdapter.notifyDataSetChanged();
            /**
             * 进入作者详情页面
             */
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Author.DataBean dataBean=listAll.get(position);
                    Intent intent=new Intent(MoreAuthorActivity.this, AuthorDetailsActivity.class);
                    intent.putExtra("id",dataBean.getId());
                    startActivity(intent);
                }
            });
            if(list.size()< HttpMethod.size){
                reList.setIsLoadingMoreEnabled(false);
            }
        }else{
            ToastUtil.showLong(author.getDesc());
        }
    }


    /**
     * 下刷
     * @param view
     */
    public void onRefresh(View view) {
        page=1;
        HttpMethod.hotAuthor(page, HandlerConstant.HOT_AUTHOR_SUCCESS1,handler);
    }

    /**
     * 上拉加载更多
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        HttpMethod.hotAuthor(page, HandlerConstant.HOT_AUTHOR_SUCCESS2,handler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
