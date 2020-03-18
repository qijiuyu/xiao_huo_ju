package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Agreement;
import com.zxdc.utils.library.bean.News;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2020/3/7.
 */

public class AgreementActivity extends BaseActivity {

    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    HtmlTextView tvContent;
    //1：注册协议，2：用户协议，3：隐私协议，4：合作内容，5：关于我们
    private int type;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        ButterKnife.bind(this);
        initView();
        //查询协议
        getAgreement();

        imgBank.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AgreementActivity.this.finish();
            }
        });
    }

    /**
     * 初始化
     */
    private void initView(){
        type=getIntent().getIntExtra("type",-1);
        switch (type){
            case 1:
                 tvTitle.setText("注册协议");
                 break;
            case 2:
                tvTitle.setText("用户协议");
                break;
            case 3:
                tvTitle.setText("隐私协议");
                break;
            case 4:
                tvTitle.setText("合作内容");
                break;
            default:
                break;
        }
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                case HandlerConstant.GET_AGREEMENT_SUCCESS:
                     Agreement agreement= (Agreement) msg.obj;
                     if(agreement==null){
                         break;
                     }
                     if(agreement.isSussess() && agreement.getData()!=null){
                         tvContent.setText(Html.fromHtml(agreement.getData().getContent()));
                         tvContent.setHtml(agreement.getData().getContent(), new HtmlHttpImageGetter(tvContent));
                     }else{
                         ToastUtil.showLong(agreement.getDesc());
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
     * 查询协议
     */
    private void getAgreement(){
        DialogUtil.showProgress(this,"加载中");
        HttpMethod.getAgreement(type,handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
