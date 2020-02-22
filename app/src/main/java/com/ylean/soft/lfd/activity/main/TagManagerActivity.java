package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.utils.channel.ChannerAdapter;
import com.ylean.soft.lfd.utils.channel.DataBean;
import com.ylean.soft.lfd.utils.channel.MyItemTouchCallBack;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 频道管理
 * Created by Administrator on 2020/2/5.
 */

public class TagManagerActivity extends BaseActivity {

    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private ChannerAdapter channerAdapter;
    public List<DataBean> list = new ArrayList<>();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);
        ButterKnife.bind(this);
        //获取频道列表
        channel();

        DataBean bean1 = new DataBean("体育", 0, "url");
        DataBean bean2 = new DataBean("新闻", 1, "url");
        DataBean bean3 = new DataBean("影视", 2, "url");
        DataBean bean4 = new DataBean("电视", 3, "url");
        DataBean bean5 = new DataBean("热点", 4, "url");
        DataBean bean6 = new DataBean("推荐", 5, "url");
        DataBean bean7 = new DataBean("屌丝", 6, "url");
        DataBean bean8 = new DataBean("音乐", 7, "url");
        DataBean bean9 = new DataBean("电影", 8, "url");

        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        list.add(bean6);
        list.add(bean7);
        list.add(bean8);
        list.add(bean9);


        listView.setLayoutManager(new GridLayoutManager(this, 4));
        channerAdapter=new ChannerAdapter(this, list);
        //关联ItemTouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(new MyItemTouchCallBack(channerAdapter));
        touchHelper.attachToRecyclerView(listView);

        channerAdapter.setTouchHelper(touchHelper);
        listView.setAdapter(channerAdapter);
    }

    @OnClick({R.id.img_bank, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                 finish();
                break;
            //编辑/完成
            case R.id.tv_right:
                 if(tvRight.getText().toString().trim().equals("编辑")){
                     tvRight.setText("完成");
                     channerAdapter.setOnClick(true);
                     //设置控件抖动
                     channerAdapter.isJitter=true;
                     channerAdapter.notifyDataSetChanged();
                 }else{
                     finish();
                 }
                break;
            default:
                break;
        }
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //获取频道列表
                case HandlerConstant.GET_CHANNEL_SUCCESS:
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
     * 获取频道列表
     */
    private void channel(){
        DialogUtil.showProgress(this,"数据加载中");
        HttpMethod.channel(handler);
    }
}
