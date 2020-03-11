package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
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
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Comment;
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
    private Comment.CommentBean commentBean;
    //回复对象
    private Reply reply;
    private CommentAdapter commentAdapter;
    //评论列表数据集合
    private List<Comment.CommentBean> listAll = new ArrayList<>();
    //评论的页码
    private int page=1;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        //注册eventBus
        EventBus.getDefault().register(this);
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

    @OnClick({R.id.img_close, R.id.rel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
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
                    refresh((Comment) msg.obj);
                    break;
                //发送评论回执
                case HandlerConstant.REPLY_SUCCESS:
                case HandlerConstant.SEND_COMMENT_SUCCESS:
                    baseBean = (BaseBean) msg.obj;
                    if (baseBean == null) {
                        break;
                    }
                    ToastUtil.showLong(baseBean.getDesc());

                    //评论后重新获取列表
                    
                    break;
                //获取回复列表
                case HandlerConstant.GET_REPLY_LIST_SUCCESS:
                      ReplyList replyList= (ReplyList) msg.obj;
                      if(replyList==null){
                          break;
                      }
                      if(replyList.isSussess()){
                          commentBean.getReplyList().clear();
                          commentBean.setReplyList(replyList.getData());
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
                         if(commentBean.isThumbComment()){
                             commentBean.setThumbComment(false);
                             commentBean.setThumbCount(commentBean.getThumbCount()-1);
                         }else{
                             commentBean.setThumbComment(true);
                             commentBean.setThumbCount(commentBean.getThumbCount()+1);
                         }
                         commentAdapter.notifyDataSetChanged();
                     }
                     ToastUtil.showLong(baseBean.getDesc());
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
     * 刷新界面数据
     */
    private void refresh(Comment comment) {
        if (comment == null) {
            return;
        }
        if (comment.isSussess()) {
            List<Comment.CommentBean> list = comment.getData();
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
            ToastUtil.showLong(comment.getDesc());
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
                    if(commentBean==null && reply==null){
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
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            //回复评论
            case EventStatus.START_REPLY:
                commentBean = (Comment.CommentBean) eventBusType.getObject();
                reply=null;
                if (commentBean == null) {
                    return;
                }
                etContent.setHint("回复 @" + commentBean.getNickname());
                //弹出软键盘
                showInput(etContent);
                break;
            //二级回复
            case EventStatus.REPLY_REPLY:
                  reply= (Reply) eventBusType.getObject();
                  commentBean=null;
                  if(reply==null){
                      return;
                  }
                  etContent.setHint("回复 @" + reply.getNickname());
                  //弹出软键盘
                  showInput(etContent);
                  break;
            default:
                break;
        }
    }


    /**
     * 监听软键盘打开还是关闭
     *
     * @param view
     */
    private void setListenerFotEditText(View view) {
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            }
            public void onSoftKeyboardClosed() {
                commentBean=null;
                reply=null;
                etContent.setHint("说点什么吧～～");
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
     * 回复评论
     */
    private void reply(String content){
        DialogUtil.showProgress(activity, "回复中");
        if(commentBean!=null){
            HttpMethod.reply(content,commentBean.getId(),handler);
        }
        if(reply!=null){
            HttpMethod.reply(content,reply.getId(),handler);
        }
    }

    /**
     * 获取回复列表
     */
    public void getReply(Comment.CommentBean commentBean){
        this.commentBean=commentBean;
        HttpMethod.getReply(commentBean.getId(),1,handler);
    }


    /**
     * 评论点赞、取消点赞
     */
    public void commPrise(Comment.CommentBean commentBean){
        //先登录
        if(!MyApplication.isLogin()){
            setClass(LoginActivity.class);
            return;
        }
        this.commentBean=commentBean;
        HttpMethod.commPrise(commentBean.getId(),HandlerConstant.COMM_PRISE_SUCCESS,handler);
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
        EventBus.getDefault().unregister(this);
    }
}
