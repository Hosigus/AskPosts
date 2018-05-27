package com.hosigus.askposts.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnswerDetailActivity extends AppCompatActivity {

    public static void start(Context context, String answerJson,QuestionDetail detail) {
        Intent intent = new Intent(context, AnswerDetailActivity.class);
        intent.putExtra("answerJson", answerJson);
        intent.putExtra("kind", detail.getKind());
        intent.putExtra("title", detail.getTitle());
        context.startActivity(intent);
    }

    private String kind,title;
    private boolean isChangingLike,isCommending;
    private QuestionDetail.AnswersBean answer;
    private List<QuestionDetail.AnswersBean.Commend> commendList;
    private BaseAdapter<QuestionDetail.AnswersBean.Commend> adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_report, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_detail);

        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle("");
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(v -> finish());

        initData();
    }

    private void initData() {
        try {
            kind = getIntent().getStringExtra("kind");
            title = getIntent().getStringExtra("title");
            answer = new QuestionDetail.AnswersBean(new JSONObject(getIntent().getStringExtra("answerJson")));
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.show("出错");
            finish();
            return;
        }
        NetUtils.post(new BaseOption(NetUrls.GET_COMMEND_LIST)
                .addUserParam()
                .param("answer_id", answer.getId()), new NetCallback() {
            @Override
            public void onSuccess(JSONBean data) {
                BaseBean baseBean = (BaseBean) data;
                if (baseBean.isGoodJson()) {
                    commendList = new ArrayList<>();
                    try {
                        JSONArray commendArray = new JSONArray(baseBean.getData());
                        for (int i = 0; i < commendArray.length(); i++) {
                            commendList.add(new QuestionDetail.AnswersBean.Commend(commendArray.getJSONObject(i)));
                        }
                        initView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.show("服务器错误");
                        finish();
                    }
                } else {
                    ToastUtils.show("服务器错误");
                    finish();
                }
            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.show("网络错误");
                finish();
            }
        });
    }

    private void initView() {
        TextView likeV, commendV,sendV;

        ConstraintLayout commendCL = findViewById(R.id.cl_commend);
        View mask = findViewById(R.id.mask);
        EditText commend = findViewById(R.id.et_commend);
        likeV = findViewById(R.id.tv_left);
        commendV = findViewById(R.id.tv_right);
        sendV = findViewById(R.id.btn_send);

        Drawable light = getResources().getDrawable(R.drawable.ic_good_light);
        Drawable dark = getResources().getDrawable(R.drawable.ic_good_dark2);
        light.setBounds(0, 0, light.getMinimumWidth(), light.getMinimumHeight());
        dark.setBounds(0, 0, dark.getMinimumWidth(), dark.getMinimumHeight());

        if (answer.isPraised()) {
            likeV.setCompoundDrawables(light, null, null, null);
        }

        likeV.setOnClickListener(v->{
            if (isChangingLike) {
                return;
            }
            isChangingLike = true;
            if (!answer.isPraised()) {
                NetUtils.post(new BaseOption(NetUrls.LIKE)
                        .addUserParam()
                        .param("answer_id", answer.getId()), new NetCallback() {
                    @Override
                    public void onSuccess(JSONBean data_like) {
                        isChangingLike = false;
                        if (((BaseBean) data_like).isStatusOK()) {
                            likeV.setCompoundDrawables(light, null, null, null);
                            answer.setIs_praised(1);
                            answer.setPraise_num(answer.getPraise_num() + 1);
                            likeV.setText(String.valueOf(answer.getPraise_num()));
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
                        .param("answer_id", answer.getId()), new NetCallback() {
                    @Override
                    public void onSuccess(JSONBean data_like) {
                        isChangingLike = false;
                        if (((BaseBean) data_like).isStatusOK()) {
                            likeV.setCompoundDrawables(dark, null, null, null);
                            answer.setIs_praised(0);
                            answer.setPraise_num(answer.getPraise_num() - 1);
                            likeV.setText(String.valueOf(answer.getPraise_num()));
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

        mask.setOnClickListener(v -> {
            commend.clearFocus();
        });
        commend.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager imm = (InputMethodManager) AnswerDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!hasFocus&&imm!=null) {
                imm.hideSoftInputFromWindow(commendCL.getWindowToken(), 0);
                commendCL.setVisibility(View.GONE);
                mask.setVisibility(View.GONE);
            }
        });
        sendV.setOnClickListener(v -> {
            if (isCommending) {
                return;
            }
            String commendStr = commend.getText().toString();
            if (commendStr.isEmpty()) {
                ToastUtils.show("评论为空");
            }
            isCommending = true;
            NetUtils.post(new BaseOption(NetUrls.COMMEND_ANSWER)
                    .addUserParam()
                    .param("answer_id", answer.getId())
                    .param("content", commendStr),
                    new NetCallback() {
                        @Override
                        public void onSuccess(JSONBean data) {
                            isCommending = false;
                            if (((BaseBean) data).isStatusOK()) {
                                commend.setText("");
                                commend.clearFocus();
                                initData();
                            } else {
                                ToastUtils.show("评论失败");
                            }
                        }

                        @Override
                        public void onFail(Exception e) {
                            isCommending = false;
                            ToastUtils.show("网络错误");
                        }
            });
        });
        commendV.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) AnswerDetailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                commendCL.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);
                commend.setFocusable(true);
                commend.requestFocus();
                imm.showSoftInput(commend,0);
            }
        });

        commendList.add(0, null);
        adapter = new BaseAdapter<QuestionDetail.AnswersBean.Commend>(commendList) {
            private static final int TYPE_HEAD = 1;

            @Override
            protected int getResId(int viewType) {
                switch (viewType) {
                    case TYPE_HEAD:
                        return R.layout.item_answer_detail_head;
                    default:
                        return R.layout.item_commend;
                }
            }

            @Override
            public int getItemViewType(int position) {
                return position == 0 ? TYPE_HEAD : 0;
            }

            @Override
            protected BaseHolder<QuestionDetail.AnswersBean.Commend> onCreateHolder(View itemView) {
                return new BaseHolder<QuestionDetail.AnswersBean.Commend>(itemView) {
                    @Override
                    public void bindData(QuestionDetail.AnswersBean.Commend data) {
                        switch (getItemViewType()) {
                            case TYPE_HEAD: {
                                if (kind.equals("情感")) {
                                    View sex = getView(R.id.v_sex);
                                    if (answer.getGender().equals("男")) {
                                        sex.setVisibility(View.VISIBLE);
                                        sex.setBackgroundResource(R.drawable.ic_sex_boy);
                                    } else if (answer.getGender().equals("女")) {
                                        sex.setVisibility(View.VISIBLE);
                                        sex.setBackgroundResource(R.drawable.ic_sex_girl);
                                    }
                                }
                                if (!answer.getPhoto_thumbnail_src().isEmpty()) {
                                    CircleImageView head = getView(R.id.civ_head);
                                    ImageLoader.with(AnswerDetailActivity.this)
                                            .place(R.drawable.ic_default_head)
                                            .error(R.drawable.ic_default_head)
                                            .load(answer.getPhoto_thumbnail_src().replace("http://", "https://"))
                                            .into(head);
                                }
                                TextView name = getView(R.id.tv_name),
                                        time = getView(R.id.tv_time),
                                        topic = getView(R.id.tv_topic),
                                        title = getView(R.id.tv_title),
                                        describe = getView(R.id.tv_describe);
                                title.setText(AnswerDetailActivity.this.title);
                                describe.setText(AnswerDetailActivity.this.answer.getContent());
                                String topicStr = "#" + kind + "#";
                                topic.setText(topicStr);
                                time.setText(answer.getCreated_at().split(" ")[0]);
                                name.setText(answer.getNickname());

                                List<String> photoList = answer.getPhoto_url();
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
                                    ImageLoader.with(AnswerDetailActivity.this).load(photoList.get(i).replace("http://", "https://")).into(photoVList.get(i));
                                }
                                TextView num = getView(R.id.tv_answer_num);
                                String numStr = "评论" + String.valueOf(commendList.size() - 1);
                                num.setText(numStr);
                                if (commendList.size() - 1 != 0) {
                                    RelativeLayout rl = getView(R.id.rl_no_answer);
                                    rl.setVisibility(View.GONE);
                                }
                                break;
                            }
                            case 0: {
                                if (kind.equals("情感")) {
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
                                    ImageLoader.with(AnswerDetailActivity.this)
                                            .place(R.drawable.ic_default_head)
                                            .error(R.drawable.ic_default_head)
                                            .load(data.getPhoto_thumbnail_src().replace("http://", "https://"))
                                            .into(head);
                                }
                                TextView name = getView(R.id.tv_name),
                                        describe = getView(R.id.tv_describe),
                                        time = getView(R.id.tv_time);
                                name.setText(data.getNickname());
                                describe.setText(data.getContent());
                                time.setText(data.getCreated_at().split(" ")[0]);
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
}
