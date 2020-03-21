package com.ylean.soft.lfd.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.DownLoad;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.ClickTextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2020/3/21.
 */
public class UploadVideoActivity extends BaseActivity {

    @BindView(R.id.lin_dialog)
    LinearLayout linDialog;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_open)
    ClickTextView tvOpen;
    //视频下载地址
    private String videoUrl;
    //视频保存地址
    private String savePath;
    /**
     * 1：微信
     * 2：朋友圈
     * 3：QQ
     * 4：QQ空间
     */
    private int shareType;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        ButterKnife.bind(this);
        initView();
        //开始下载视频
        uploadVideo();
    }


    /**
     * 初始化
     */
    private void initView() {
        videoUrl = getIntent().getStringExtra("videoUrl");
        shareType=getIntent().getIntExtra("shareType",0);
        String[] str = videoUrl.split("/");
        savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + str[str.length - 1];
        switch (shareType){
            case 1:
                 tvContent.setText("由于微信分享限制，请到微信上传视频来分享");
                 tvOpen.setText("继续分享到微信");
                 break;
            case 2:
                tvContent.setText("由于微信朋友圈分享限制，请到微信朋友圈上传视频来分享");
                tvOpen.setText("继续分享到微信");
                break;
            case 3:
                tvContent.setText("由于QQ分享限制，请到QQ上传视频来分享");
                tvOpen.setText("继续分享到QQ");
                break;
            case 4:
                tvContent.setText("由于QQ空间分享限制，请到QQ空间上传视频来分享");
                tvOpen.setText("继续分享到QQ");
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.img_close, R.id.tv_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.tv_open:
                if(shareType==1 || shareType==2){
                    Intent lan = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(lan.getComponent());
                    startActivity(intent);
                }else{
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                    startActivity(intent);
                }
                finish();
                break;
            default:
                break;
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //下载进度
                case HandlerConstant.DOWNLOAD_PRORESS:
                    String progress = (String) msg.obj;
                    if (!TextUtils.isEmpty(progress)) {
                        tvProgress.setText(progress);
                    }
                    break;
                //下载完成
                case HandlerConstant.DOWNLOAD_SUCCESS:
                    //更新相册
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(new File(savePath)));
                    sendBroadcast(intent);

                    //展示完成提示框
                    linDialog.setVisibility(View.VISIBLE);
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
     * 开始下载视频
     */
    private void uploadVideo() {
        //判断是否下载过该视频
        File file=new File(savePath);
        if(file.isFile()){
            linDialog.setVisibility(View.VISIBLE);
            return;
        }
        //开始下载
        DownLoad d = new DownLoad();
        d.setDownPath(HttpConstant.IP + videoUrl);
        d.setSavePath(savePath);
        HttpMethod.download(d, handler);
    }
}
