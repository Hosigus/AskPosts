package com.hosigus.askposts.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hosigus.askposts.App;
import com.hosigus.askposts.R;
import com.hosigus.askposts.config.NetUrls;
import com.hosigus.askposts.item.Option.BaseOption;
import com.hosigus.askposts.item.jsonBean.BaseBean;
import com.hosigus.askposts.item.jsonBean.QuestionDetail;
import com.hosigus.askposts.ui.view.CircleImageView;
import com.hosigus.imageloader.ImageLoader;
import com.hosigus.simplerecycleadapter.BaseAdapter;
import com.hosigus.simplerecycleadapter.BaseHolder;
import com.hosigus.tools.interfaces.NetCallback;
import com.hosigus.tools.items.JSONBean;
import com.hosigus.tools.utils.NetUtils;
import com.hosigus.tools.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionDetailActivity extends AppCompatActivity {
    public static void start(Context context,int id) {
        Intent intent = new Intent(context,QuestionDetailActivity.class);
        intent.putExtra("questionId", id);
        context.startActivity(intent);
    }

    private QuestionDetail detail;
    private BaseAdapter<QuestionDetail.AnswersBean> adapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle("");
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(v-> finish());

        initData(getIntent().getIntExtra("questionId", -1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_report, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(Integer.parseInt(id));
    }

    private void initData(int id) {
        this.id = String.valueOf(id);
        if (id == -1) {
            ToastUtils.show("找不到该问题");
            finish();
            return;
        }

        NetUtils.post(new BaseOption(NetUrls.QUESTION_DETAIL)
                .addUserParam()
                .param("question_id", String.valueOf(id)), new NetCallback() {
            @Override
            public void onSuccess(JSONBean data) {
                BaseBean baseBean = (BaseBean) data;
                if (!baseBean.isGoodJson()) {
                    ToastUtils.show("服务器错误");
                    finish();
                    return;
                }
                try {
                    QuestionDetailActivity.this.detail = new QuestionDetail(new JSONObject(baseBean.getData()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initView();
            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.show("网络错误");
                finish();
            }
        });
    }

    private void initView() {
        TextView leftTV = findViewById(R.id.tv_left), rightTV = findViewById(R.id.tv_right);
        View.OnClickListener left, right;
        Drawable leftD;
        Drawable rightD;
        if (detail.isSelf()) {
            leftTV.setText("加价");
            rightTV.setText("取消提问");
            leftD = getResources().getDrawable(R.drawable.ic_add_coin);
            rightD = getResources().getDrawable(R.drawable.ic_add_coin);
            left = v -> {
                // TODO: 2018/5/27 加价 
            };
            right = v -> {
                NetUtils.post(new BaseOption(NetUrls.CANCEL_ASK)
                        .addUserParam()
                        .param("question_id", id), new NetCallback() {
                    @Override
                    public void onSuccess(JSONBean data_like) {
                        if (((BaseBean) data_like).isStatusOK()) {
                            ToastUtils.show("问题已取消");
                            finish();
                        } else {
                            ToastUtils.show("取消失败");
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        ToastUtils.show("服务器错误");
                    }
                });
            };

        } else {
            leftTV.setText("忽略");
            rightTV.setText("帮助");
            leftD = getResources().getDrawable(R.drawable.ic_ignore);
            rightD = getResources().getDrawable(R.drawable.ic_help);
            left = v -> {
                // TODO: 2018/5/27 忽略 
            };
            right = v -> {
                PostAnswerActivity.start(this, id);
            };
        }

        leftD.setBounds(0, 0, leftD.getMinimumWidth(), leftD.getMinimumHeight());
        rightD.setBounds(0, 0, rightD.getMinimumWidth(), rightD.getMinimumHeight());
        leftTV.setCompoundDrawables(leftD, null, null, null);
        rightTV.setCompoundDrawables(rightD, null, null, null);
        leftTV.setOnClickListener(left);
        rightTV.setOnClickListener(right);

        detail.getAnswers().add(0, new QuestionDetail.AnswersBean());
        adapter=new BaseAdapter<QuestionDetail.AnswersBean>(detail.getAnswers()) {
            private static final int TYPE_HEAD = 1;
            @Override
            protected int getResId(int viewType) {
                switch (viewType) {
                    case TYPE_HEAD:
                        return R.layout.item_question_detail_head;
                    default:
                        return R.layout.item_answer;
                }
            }

            @Override
            public int getItemViewType(int position) {
                return position == 0 ? TYPE_HEAD : 0;
            }

            @Override
            protected BaseHolder<QuestionDetail.AnswersBean> onCreateHolder(View itemView) {
                return new BaseHolder<QuestionDetail.AnswersBean>(itemView) {
                    private boolean isChangingLike;
                    private boolean isChangingAdopt;
                    @Override
                    public void bindData(QuestionDetail.AnswersBean data) {
                        switch (getItemViewType()){
                            case TYPE_HEAD:{
                                if (detail.getKind().equals("情感")) {
                                    View sex = getView(R.id.v_sex);
                                    if (detail.getQuestioner_gender().equals("男")) {
                                        sex.setVisibility(View.VISIBLE);
                                        sex.setBackgroundResource(R.drawable.ic_sex_boy);
                                    } else if (detail.getQuestioner_gender().equals("女")) {
                                        sex.setVisibility(View.VISIBLE);
                                        sex.setBackgroundResource(R.drawable.ic_sex_girl);
                                    }
                                }
                                if (!detail.getQuestioner_photo_thumbnail_src().isEmpty()) {
                                    CircleImageView head = getView(R.id.civ_head);
                                    ImageLoader.with(QuestionDetailActivity.this)
                                            .place(R.drawable.ic_default_head)
                                            .error(R.drawable.ic_default_head)
                                            .load(detail.getQuestioner_photo_thumbnail_src().replace("http://", "https://"))
                                            .into(head);
                                }
                                TextView name = getView(R.id.tv_name),
                                        time = getView(R.id.tv_time),
                                        coin = getView(R.id.tv_coin),
                                        topic = getView(R.id.tv_topic),
                                        title = getView(R.id.tv_title),
                                        describe = getView(R.id.tv_describe);
                                title.setText(detail.getTitle());
                                describe.setText(detail.getDescription());
                                coin.setText(String.valueOf(detail.getReward()));
                                String topicStr = "#" + detail.getKind() + "#";
                                topic.setText(topicStr);
                                time.setText(App.getDateDifferenceDescribe(detail.getDisappear_at()));
                                name.setText(detail.getQuestioner_nickname());

                                List<String> photoList = detail.getPhoto_urls();
                                List<ImageView> photoVList = new ArrayList<>();
                                switch (photoList.size()) {
                                    case 4:
                                        ImageView p4 = getView(R.id.iv_4);
                                        photoVList.add(p4);
                                    case 3:
                                        ImageView p3 = getView(R.id.iv_3);
                                        photoVList.add(p3);
                                    case 2:
                                        ImageView p2 = getView(R.id.iv_2);
                                        photoVList.add(p2);
                                    case 1:
                                        ImageView p1 = getView(R.id.iv_1);
                                        photoVList.add(p1);
                                }
                                for (int i = 0; i < photoList.size(); i++) {
                                    photoVList.get(i).setVisibility(View.VISIBLE);
                                    ImageLoader.with(QuestionDetailActivity.this).load(photoList.get(i).replace("http://", "https://")).into(photoVList.get(i));
                                }
                                TextView num = getView(R.id.tv_answer_num);
                                String numStr = String.valueOf(detail.getAnswers().size()-1) + "个回答";
                                num.setText(numStr);
                                if (detail.getAnswers().size() - 1 != 0) {
                                    RelativeLayout rl = getView(R.id.rl_no_answer);
                                    rl.setVisibility(View.GONE);
                                }
                                break;
                            }
                            case 0:{
                                if (detail.getKind().equals("情感")) {
                                    View sex = getView(R.id.v_sex);
                                    if (data.getGender().equals("男")) {
                                        sex.setVisibility(View.VISIBLE);
                                        sex.setBackgroundResource(R.drawable.ic_sex_boy);
                                    } else if (data.getGender().equals("女")) {
                                        sex.setVisibility(View.VISIBLE);
                                        sex.setBackgroundResource(R.drawable.ic_sex_girl);
                                    }
                                }
                                if (!data.getPhoto_thumbnail_src().isEmpty()) {
                                    CircleImageView head = getView(R.id.civ_head);
                                    ImageLoader.with(QuestionDetailActivity.this)
                                            .place(R.drawable.ic_default_head)
                                            .error(R.drawable.ic_default_head)
                                            .load(data.getPhoto_thumbnail_src().replace("http://", "https://"))
                                            .into(head);
                                }
                                TextView name=getView(R.id.tv_name),
                                        describe=getView(R.id.tv_describe),
                                        time=getView(R.id.tv_time),
                                        comment=getView(R.id.tv_comment),
                                        like=getView(R.id.tv_like);
                                View likeBtn = getView(R.id.v_like);
                                name.setText(data.getNickname());
                                describe.setText(data.getContent());
                                time.setText(data.getCreated_at().split(" ")[0]);
                                comment.setText(String.valueOf(data.getComment_num()));
                                like.setText(String.valueOf(data.getPraise_num()));
                                if (data.isAdopted()) {
                                    LinearLayout ll = getView(R.id.ll_accept);
                                    ll.setVisibility(View.VISIBLE);
                                }else if (detail.isSelf()) {
                                    TextView btn = getView(R.id.btn_accept);
                                    btn.setVisibility(View.VISIBLE);
                                    btn.setOnClickListener(v -> {
                                        if (isChangingAdopt) {
                                            return;
                                        }
                                        isChangingAdopt = true;
                                        NetUtils.post(new BaseOption(NetUrls.ADOPTED)
                                                .addUserParam()
                                                .param("answer_id", data.getId())
                                                .param("question_id", id), new NetCallback() {
                                            @Override
                                            public void onSuccess(JSONBean data_adopt) {
                                                if (((BaseBean) data_adopt).isStatusOK()) {
                                                    btn.setVisibility(View.GONE);
                                                    LinearLayout ll = getView(R.id.ll_accept);
                                                    ll.setVisibility(View.VISIBLE);
                                                    data.setIs_adopted(1);
                                                }
                                            }

                                            @Override
                                            public void onFail(Exception e) {
                                                isChangingAdopt = false;
                                                ToastUtils.show("网络出错");
                                            }
                                        });
                                    });
                                }
                                if (data.isPraised()) {
                                    likeBtn.setBackgroundResource(R.drawable.ic_good_light);
                                    likeBtn.setClickable(false);
                                } else {
                                    likeBtn.setOnClickListener(v -> {
                                        if (isChangingLike) {
                                            return;
                                        }
                                        isChangingLike = true;
                                        if (!data.isPraised()) {
                                            NetUtils.post(new BaseOption(NetUrls.LIKE)
                                                    .addUserParam()
                                                    .param("answer_id", data.getId()), new NetCallback() {
                                                @Override
                                                public void onSuccess(JSONBean data_like) {
                                                    isChangingLike = false;
                                                    if (((BaseBean) data_like).isStatusOK()) {
                                                        likeBtn.setBackgroundResource(R.drawable.ic_good_light);
                                                        data.setIs_praised(1);
                                                        data.setPraise_num(data.getPraise_num() + 1);
                                                        like.setText(String.valueOf(data.getPraise_num()));
                                                    } else {
                                                        ToastUtils.show("点赞失败");
                                                    }
                                                }

                                                @Override
                                                public void onFail(Exception e) {
                                                    isChangingLike = false;
                                                    ToastUtils.show("服务器错误");
                                                }
                                            });
                                        } else {
                                            NetUtils.post(new BaseOption(NetUrls.UNLIKE)
                                                    .addUserParam()
                                                    .param("answer_id", data.getId()), new NetCallback() {
                                                @Override
                                                public void onSuccess(JSONBean data_like) {
                                                    isChangingLike = false;
                                                    if (((BaseBean) data_like).isStatusOK()) {
                                                        likeBtn.setBackgroundResource(R.drawable.ic_good_dark);
                                                        data.setIs_praised(0);
                                                        data.setPraise_num(data.getPraise_num() - 1);
                                                        like.setText(String.valueOf(data.getPraise_num()));
                                                    } else {
                                                        ToastUtils.show("取消失败");
                                                    }
                                                }

                                                @Override
                                                public void onFail(Exception e) {
                                                    isChangingLike = false;
                                                    ToastUtils.show("服务器错误");
                                                }
                                            });
                                        }

                                    });
                                }
                                TextView unfold = getView(R.id.unfold);
                                unfold.setOnClickListener(v -> AnswerDetailActivity
                                        .start(QuestionDetailActivity.this, data.getJson(), detail));
                            }
                        }
                    }
                };
            }
        };
        RecyclerView rv = findViewById(R.id.rv_question_detail);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PostAnswerActivity.POST_RESULT:
                if (data != null && data.getBooleanExtra("RESULT", false)) {
                    initData(Integer.parseInt(id));
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
