package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.init.LoginActivity;
import com.ylean.soft.lfd.adapter.main.CommentAdapter;
import com.ylean.soft.lfd.utils.SoftKeyboardStateHelper;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.AddComment;
import com.zxdc.utils.library.bean.AddReply;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Comment;
import com.zxdc.utils.library.bean.CommentList;
import com.zxdc.utils.library.bean.Reply;
import com.zxdc.utils.library.bean.ReplyList;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;
import com.zxdc.utils.library.view.MyRefreshLayoutListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频评论列表
 * Created by Administrator on 2020/3/5.
 */
public class CommentActivity extends BaseActivity implements MyRefreshLayoutListener {

    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.rel)
    RelativeLayout rel;
    //视频详情对象
    private VideoInfo.VideoBean videoBean;
    //评论对象
    private Comment comment;
    //回复对象
    private Reply reply;
    private CommentAdapter commentAdapter;
    //评论列表数据集合
    private List<Comment> listAll = new ArrayList<>();
    //评论的页码
    private int page=1;
    /**
     * 0：发送评论
     * 1：一级回复
     * 2：二级回复
     */
    private int playStatus;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initView();
        //监听软键盘打开还是关闭
        setListenerFotEditText(rel);
        //获取评论列表
        getComment();
    }

    /**
     * 初始化
     */
    private void initView() {
        videoBean = (VideoInfo.VideoBean) getIntent().getSerializableExtra("videoBean");
        if(videoBean==null){
            return;
        }
        tvTotal.setText(videoBean.getCommentCount()+"条评论");

        //刷新加载
        reList.setMyRefreshLayoutListener(this);
        reList.setPullDownRefreshEnable(false);
        listView.setDivider(null);
        //监听发表评论的输入框
        etContent.setOnEditorActionListener(commListener);
    }

    @OnClick({R.id.lin_bank, R.id.rel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_bank:
            case R.id.rel:
                finish();
                break;
            default:
                break;
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            BaseBean baseBean;
            switch (msg.what) {
                //获取评论列表
                case HandlerConstant.GET_COMMENT_SUCCESS:
                    reList.loadMoreComplete();
                    refresh((CommentList) msg.obj);
                    break;
                //发送评论回执
                case HandlerConstant.SEND_COMMENT_SUCCESS:
                     AddComment addComment= (AddComment) msg.obj;
                     if(addComment==null){
                         break;
                     }
                     if(addComment.isSussess() && addComment.getData()!=null){
                         listAll.add(0,addComment.getData());
                         commentAdapter.notifyDataSetChanged();

                         //增加评论个数
                         videoBean.setCommentCount(videoBean.getCommentCount()+1);
                         tvTotal.setText(videoBean.getCommentCount()+"条评论");
                     }
                    break;
                //发送回复回执
                case HandlerConstant.REPLY_SUCCESS:
                      AddReply addReply= (AddReply) msg.obj;
                      if(addReply==null){
                          break;
                      }
                      if(addReply.isSussess() && addReply.getData()!=null){
                          //设置回复的个数
                          if(comment.getReplyCount()==0){
                              comment.setReplyCount(1);
                          }
                          comment.getReplyList().add(0,addReply.getData());
                          commentAdapter.notifyDataSetChanged();
                      }
                      break;
                //获取回复列表
                case HandlerConstant.GET_REPLY_LIST_SUCCESS:
                      ReplyList replyList= (ReplyList) msg.obj;
                      if(replyList==null){
                          break;
                      }
                      if(replyList.isSussess()){
                          comment.getReplyList().clear();
                          comment.setReplyList(replyList.getData());
                          commentAdapter.notifyDataSetChanged();
                      }else{
                          ToastUtil.showLong(replyList.getDesc());
                      }
                      break;
                //评论点赞
                case HandlerConstant.COMM_PRISE_SUCCESS:
                     baseBean = (BaseBean) msg.obj;
                     if (baseBean == null) {
                         break;
                     }
                     if(baseBean.isSussess()){
                         if(comment.isThumbComment()){
                             comment.setThumbComment(false);
                             comment.setThumbCount(comment.getThumbCount()-1);
                         }else{
                             comment.setThumbComment(true);
                             comment.setThumbCount(comment.getThumbCount()+1);
                         }
                         commentAdapter.notifyDataSetChanged();
                     }
                      break;
                //回复点赞
                case HandlerConstant.REPLY_PRISE_SUCCESS:
                     baseBean = (BaseBean) msg.obj;
                     if (baseBean == null) {
                         break;
                     }
                     if(baseBean.isSussess()){
                         if(reply.isThumbComment()){
                             reply.setThumbComment(false);
                             reply.setThumbCount(reply.getThumbCount()-1);
                         }else{
                             reply.setThumbComment(true);
                             reply.setThumbCount(reply.getThumbCount()+1);
                         }
                         commentAdapter.notifyDataSetChanged();
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
     * 刷新界面数据
     */
    private void refresh(CommentList commentList) {
        if (commentList == null) {
            return;
        }
        if (commentList.isSussess()) {
            List<Comment> list = commentList.getData();
            listAll.addAll(list);
            //展示列表
            if (commentAdapter == null) {
                commentAdapter = new CommentAdapter(this, listAll);
                listView.setAdapter(commentAdapter);
            } else {
                commentAdapter.notifyDataSetChanged();
            }
            if (list.size() < 20) {
                reList.setIsLoadingMoreEnabled(false);
            }
        } else {
            ToastUtil.showLong(commentList.getDesc());
        }
    }


    /**
     * 监听发表评论的输入框
     */
    private TextView.OnEditorActionListener commListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String content = v.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.showLong("请输入评论内容");
                } else {
                    //先登录
                    if(!MyApplication.isLogin()){
                        setClass(LoginActivity.class);
                        return false;
                    }
                    etContent.setText(null);
                    if(playStatus==0){
                        //发送评论
                        sendComment(content);
                    }else{
                        //回复评论
                        reply(content);
                    }
                }
            }
            return false;
        }
    };



    /**
     * 监听软键盘打开还是关闭
     * @param view
     */
    private void setListenerFotEditText(View view) {
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            }
            public void onSoftKeyboardClosed() {
                //输入没有数据
                if(TextUtils.isEmpty(etContent.getText().toString().trim())){
                    playStatus=0;
                    etContent.setHint("说点什么吧～～");
                }
            }
        });
    }


    /**
     * 下刷
     *
     * @param view
     */
    public void onRefresh(View view) {
    }

    /**
     * 上拉加载更多
     *
     * @param view
     */
    public void onLoadMore(View view) {
        page++;
        getComment();
    }


    /**
     * 获取评论列表
     */
    private void getComment() {
        if (videoBean == null) {
            return;
        }
        HttpMethod.getComment(videoBean.getId(), page, handler);
    }


    /**
     * 发送评论
     */
    public void sendComment(String content) {
        DialogUtil.showProgress(activity, "评论中");
        HttpMethod.sendComment(content, videoBean.getId(), handler);
    }


    /**
     * 展示回复的界面
     * @param playStatus:1：一级回复    2：二级回复
     */
    public void showSendReply(int playStatus,Comment comment,Reply reply){
        this.playStatus=playStatus;
        this.comment=comment;
        this.reply=reply;
        if(playStatus==1){
            etContent.setHint("回复 @" + comment.getNickname());
        }else{
            etContent.setHint("回复 @" + reply.getNickname());
        }
        //弹出软键盘
        showInput(etContent);
    }

    /**
     * 回复评论
     */
    private void reply(String content){
        DialogUtil.showProgress(activity, "回复中");
        if(playStatus==1){
            HttpMethod.reply(content,comment.getId(),handler);
        }else{
            HttpMethod.reply(content,reply.getId(),handler);
        }
    }

    /**
     * 获取回复列表
     */
    public void getReply(Comment comment){
        this.comment=comment;
        HttpMethod.getReply(comment.getId(),1,handler);
    }


    /**
     * 评论点赞、取消点赞
     */
    public void commPrise(Comment comment){
        //先登录
        if(!MyApplication.isLogin()){
            setClass(LoginActivity.class);
            return;
        }
        this.comment=comment;
        HttpMethod.commPrise(comment.getId(),HandlerConstant.COMM_PRISE_SUCCESS,handler);
    }


    /**
     * 回复点赞、取消点赞
     */
    public void replyPrise(Reply reply){
        //先登录
        if(!MyApplication.isLogin()){
            setClass(LoginActivity.class);
            return;
        }
        this.reply=reply;
        HttpMethod.commPrise(reply.getId(),HandlerConstant.REPLY_PRISE_SUCCESS,handler);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
