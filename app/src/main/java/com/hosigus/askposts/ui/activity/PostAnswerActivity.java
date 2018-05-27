package com.hosigus.askposts.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.hosigus.askposts.R;
import com.hosigus.askposts.config.NetUrls;
import com.hosigus.askposts.item.Option.BaseOption;
import com.hosigus.askposts.item.jsonBean.BaseBean;
import com.hosigus.tools.interfaces.NetCallback;
import com.hosigus.tools.items.JSONBean;
import com.hosigus.tools.utils.NetUtils;
import com.hosigus.tools.utils.ToastUtils;

public class PostAnswerActivity extends AppCompatActivity {

    public static final int POST_RESULT = 1;
    public static void start(Activity context, String qId) {
        Intent intent = new Intent(context, PostAnswerActivity.class);
        intent.putExtra("qId", qId);
        context.startActivityForResult(intent, POST_RESULT);
    }

    private String qId;
    private Intent result;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_answer);

        result = new Intent();
        result.putExtra("RESULT", false);
        qId = getIntent().getStringExtra("qId");
        if (qId.isEmpty()) {
            ToastUtils.show("未知问题");
            finish();
        }

        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle("");
        setSupportActionBar(bar);

        TextView cancel, submit,left;
        EditText input;

        cancel = findViewById(R.id.btn_cancel);
        submit = findViewById(R.id.btn_submit);
        input = findViewById(R.id.et_help);
        left = findViewById(R.id.tv_size_left);

        dialog = ProgressDialog.show(this, "尝试提交中", "请稍候");
        dialog.dismiss();
        cancel.setOnClickListener(v -> finish());
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int leftSize = 300 - s.toString().length();
                left.setText(String.valueOf(leftSize));
                if (leftSize < 0) {
                    left.setTextColor(getResources().getColor(R.color.red));
                } else {
                    left.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        submit.setOnClickListener(v ->{
            if (300 - input.getText().toString().length() < 0) {
                ToastUtils.show("字数过限了哦,精简一下答案吧");
            } else {
                dialog.show();
                NetUtils.post(new BaseOption(NetUrls.ANSWER_QUESTION)
                                .addUserParam()
                                .param("content", input.getText().toString())
                                .param("question_id",qId),
                        new NetCallback() {
                            @Override
                            public void onSuccess(JSONBean data) {
                                dialog.dismiss();
                                if (((BaseBean) data).isGoodJson()) {
                                    result.putExtra("RESULT", true);
                                    ToastUtils.show("回答成功");
                                    finish();
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                dialog.dismiss();
                                ToastUtils.show("网络错误");
                            }
                        });
            }
        });


    }

    @Override
    public void finish() {
        this.setResult(POST_RESULT,result);
        super.finish();
    }
}
