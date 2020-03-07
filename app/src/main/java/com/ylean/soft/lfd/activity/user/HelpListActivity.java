package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.user.HelpListAdapter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Help;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 帮助中心
 * Created by Administrator on 2020/3/7.
 */
public class HelpListActivity extends BaseActivity {

    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        tvTitle.setText("帮助中心");
        //帮助列表
        getHelp();

        imgBank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HelpListActivity.this.finish();
            }
        });
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                case HandlerConstant.GET_AGREEMENT_SUCCESS:
                    Help help= (Help) msg.obj;
                    if(help==null){
                        break;
                    }
                    if(help.isSussess()){
                        HelpListAdapter helpListAdapter=new HelpListAdapter(HelpListActivity.this,help.getData());
                        listView.setAdapter(helpListAdapter);
                    }else{
                        ToastUtil.showLong(help.getDesc());
                    }
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
     * 帮助列表
     */
    private void getHelp(){
        DialogUtil.showProgress(this,"加载中");
        HttpMethod.getHelp(handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
