package com.ylean.soft.lfd.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.DownLoad;
import com.zxdc.utils.library.bean.Version;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.ArrowDownloadButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 版本更新工具类
 * http://dyrs.yl-mall.cn/upload/image/20200311/f0f62207-3778-41be-bb5b-dd890f1516a8.apk
 */
public class UpdateVersionUtils {
    private Dialog dialog;
    private ArrowDownloadButton abtn;
    private Version version;
    private Context mContext;
    private final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xhj.apk";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * 查询最新版本
     *
     * @param mContext
     */
    public void getVersion(Context mContext) {
        this.mContext = mContext;
        final String today_time=SPUtil.getInstance(mContext).getString("today_time");
        if(today_time.equals(sdf.format(new Date()))){
            return;
        }
        HttpMethod.version(mHandler);
    }


    private Handler mHandler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //查询最新版本
                case HandlerConstant.UPDATE_VERSION_SUCCESS:
                    version = (Version) msg.obj;
                    if(version==null){
                        break;
                    }
                    if(version.isSussess() && version.getData()!=null){
                        //判断是否需要更新
                        if(!Util.getVersionName(mContext).equals(version.getData().getVersion())){
                            View view = LayoutInflater.from(mContext).inflate(R.layout.version_pop, null);
                            TextView tvCalcle =view.findViewById(R.id.tv_cancle);
                            TextView tvConfirm =view.findViewById(R.id.tv_confirm);

                            dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setTitle(null);
                            dialog.setCancelable(false);
                            dialog.setContentView(view);
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置
                            dialog.show();

                            tvConfirm.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    View view = LayoutInflater.from(mContext).inflate(R.layout.update_version, null);
                                    abtn =view.findViewById(R.id.arrow_download_button);
                                    dialog.setContentView(view);

                                    //先删除重复安装包文件
                                    File file = new File(savePath);
                                    if (file.isFile()) {
                                        file.delete();
                                    }

                                    DownLoad d = new DownLoad();
                                    d.setDownPath(HttpConstant.IP+version.getData().getUrl());
                                    d.setSavePath(savePath);
                                    //下载文件
                                    HttpMethod.download(d, mHandler);
                                    abtn.startAnimating();
                                }
                            });
                            tvCalcle.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    //记录今天的日期
                                    SPUtil.getInstance(mContext).addString("today_time",sdf.format(new Date()));
                                }
                            });

                        }
                    }
                    break;
                //下载进度
                case HandlerConstant.DOWNLOAD_PRORESS:
                    String progress = (String) msg.obj;
                    if (!TextUtils.isEmpty(progress)) {
                        progress = progress.replace("%", "");
                        abtn.setProgress(Integer.parseInt(progress));
                    }
                    break;
                //下载完成后自动安装
                case HandlerConstant.DOWNLOAD_SUCCESS:
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            File file = new File(savePath);
                            if (file.isFile()) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri uri;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 适配Android 7系统版本
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                        uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);//通过FileProvider创建一个content类型的Uri
                                    } else {
                                        uri = Uri.fromFile(file);
                                    }
                                    intent.setDataAndType(uri, "application/vnd.android.package-archive"); // 对应apk类型
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, 1000);
                    break;

            }
            return false;
        }
    });

}
