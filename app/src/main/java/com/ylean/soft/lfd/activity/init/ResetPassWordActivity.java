package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Login;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置登录密码
 * Created by Administrator on 2020/2/5.
 */

public class ResetPassWordActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd2)
    EditText etPwd2;
    private String mobile,code;
    /**
     * 1：注册进入
     * 2：忘记密码进入
     */
    private int type;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化
     */
    private void initView(){
        type=getIntent().getIntExtra("type",-1);
        if(type==1){
            tvTitle.setText("设置登录密码");
        }else{
            tvTitle.setText("重置登录密码");
        }
        mobile=getIntent().getStringExtra("mobile");
        code=getIntent().getStringExtra("code");
    }

    @OnClick({R.id.img_bank, R.id.tv_confirm})
    public void onViewClicked(View view) {
        String pwd=etPwd.getText().toString().trim();
        String pwd2=etPwd2.getText().toString().trim();
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            case R.id.tv_confirm:
                if(TextUtils.isEmpty(pwd)){
                    ToastUtil.showLong("请输入登录密码");
                    return;
                }
                if(TextUtils.isEmpty(pwd2)){
                    ToastUtil.showLong("请再次输入登录密码");
                    return;
                }
                if(!pwd.equals(pwd2)){
                    ToastUtil.showLong("两次输入的登录密码不一致");
                    return;
                }
                if(type==1){
                    register(code,pwd,mobile);
                }else{
                    editPwd(code,pwd,mobile);
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
                //注册回执
                case HandlerConstant.REGISTER_SUCCESS:
                     Login login= (Login) msg.obj;
                     if(login==null){
                         break;
                     }
                     if(login.isSussess()){
                         //存储token
                         SPUtil.getInstance(activity).addString(SPUtil.TOKEN,login.getData().getToken());
                         //存储用户id
                         SPUtil.getInstance(activity).addString(SPUtil.USER_ID,login.getData().getId()+"");
                         //是否通过第三方登录
                         SPUtil.getInstance(activity).addBoolean(SPUtil.IS_THREE_LOGIN,false);
                         //存储手机号
                         SPUtil.getInstance(activity).addString(SPUtil.MOBILE,login.getData().getMobile());
                         //通知关闭上个页面
                         EventBus.getDefault().post(new EventBusType(EventStatus.CLOSE_PAGE));
                         //进入选择喜好的页面
                         setClass(SelectTagActivity.class);
                         finish();
                     }
                     ToastUtil.showLong(login.getDesc());
                      break;
                //修改密码回执
                case HandlerConstant.EDIT_PWD_SUCCESS:
                     BaseBean baseBean= (BaseBean) msg.obj;
                     if(baseBean==null){
                         break;
                     }
                     if(baseBean.isSussess()){
                         EventBus.getDefault().post(new EventBusType(EventStatus.CLOSE_PAGE));
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
     * 注册
     * @param code
     * @param pwd
     * @param mobile
     */
    private void register(String code,String pwd,String mobile){
        DialogUtil.showProgress(this,"注册中");
        HttpMethod.register(code,pwd,mobile,handler);
    }


    /**
     * 修改密码
     * @param code
     * @param pwd
     * @param mobile
     */
    private void editPwd(String code,String pwd,String mobile){
        DialogUtil.showProgress(this,"修改中");
        HttpMethod.editPwd(code,pwd,mobile,handler);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
