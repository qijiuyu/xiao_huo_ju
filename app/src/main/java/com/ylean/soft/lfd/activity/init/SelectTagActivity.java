package com.ylean.soft.lfd.activity.init;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.init.SelectTagAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择标签
 * Created by Administrator on 2020/2/7.
 */

public class SelectTagActivity extends BaseActivity {

    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.tv_look)
    TextView tvLook;
    private SelectTagAdapter selectTagAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        ButterKnife.bind(this);
        initView();
        //获取频道列表
        channel();
    }

    /**
     * 初始化
     */
    private void initView(){
        final String register_des="<u>随便看看</u>";
        tvLook.setText(Html.fromHtml(register_des));
    }

    @OnClick({R.id.tv_skip, R.id.tv_ok, R.id.tv_look})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
            case R.id.tv_look:
                finish();
                break;
            //选好了
            case R.id.tv_ok:
                if(selectTagAdapter==null){
                    return;
                }
                if(selectTagAdapter.map.size()==0){
                    ToastUtil.showLong("请选择您感兴趣的频道");
                    return;
                }
                StringBuilder stringBuilder=new StringBuilder("[");
                for (Integer key : selectTagAdapter.map.keySet()) {
                     stringBuilder.append(key+",");
                }
                String ids=stringBuilder.substring(0,stringBuilder.length()-1)+"]";
                //设置感兴趣的频道
                setChannel(ids);
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
                      final Tag tag= (Tag) msg.obj;
                      if(tag==null){
                          break;
                      }
                      if(tag.isSussess()){
                          listView.setLayoutManager(new GridLayoutManager(SelectTagActivity.this, 3));
                          selectTagAdapter=new SelectTagAdapter(SelectTagActivity.this,tag.getData());
                          listView.setAdapter(selectTagAdapter);
                      }else{
                          ToastUtil.showLong(tag.getDesc());
                      }
                      break;
                //设置感兴趣的频道
                case HandlerConstant.SET_CHANNEL_SUCCESS:
                     final BaseBean baseBean= (BaseBean) msg.obj;
                     if(baseBean==null){
                         break;
                     }
                     if(baseBean.isSussess()){
                         finish();
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
     * 获取频道列表
     */
    private void channel(){
        DialogUtil.showProgress(this,"数据加载中");
        HttpMethod.channel("0",handler);
    }


    /**
     * 设置感兴趣的频道
     */
    private void setChannel(String ids){
        DialogUtil.showProgress(this,"提交中");
        HttpMethod.setChannel(ids,handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
