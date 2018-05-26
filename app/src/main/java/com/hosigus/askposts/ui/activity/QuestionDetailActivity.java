package com.hosigus.askposts.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hosigus.askposts.App;
import com.hosigus.askposts.R;
import com.hosigus.askposts.config.NetUrls;
import com.hosigus.askposts.item.Option.BaseOption;
import com.hosigus.askposts.item.jsonBean.QuestionDetail;
import com.hosigus.askposts.ui.view.CircleImageView;
import com.hosigus.tools.interfaces.NetCallback;
import com.hosigus.tools.items.JSONBean;
import com.hosigus.tools.utils.NetUtils;
import com.hosigus.tools.utils.ToastUtils;

public class QuestionDetailActivity extends AppCompatActivity {
    public static void start(Context context,int id) {
        Intent intent = new Intent(context,QuestionDetailActivity.class);
        intent.putExtra("questionId", id);
        context.startActivity(intent);
    }

    private QuestionDetail data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle("");
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(v-> finish());

        initData(getIntent().getIntExtra("id", -1));
    }

    private void initData(int id) {
        if (id == -1) {
            ToastUtils.show("找不到该问题");
            finish();
            return;
        }
        NetUtils.post(new BaseOption(NetUrls.QUESTION_DETAIL)
                .param("stuNum", App.getMe().getStuNum())
                .param("idNum",App.getPwd(App.getMe().getStuNum())), new NetCallback() {
            @Override
            public void onSuccess(JSONBean data) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

//    private void initView() {
//        TextView name = getView(R.id.tv_name),
//                time = getView(R.id.tv_time),
//                coin = getView(R.id.tv_coin),
//                topic = getView(R.id.tv_topic),
//                title = getView(R.id.tv_title),
//                describe = getView(R.id.tv_describe);
//        if (kind.equals("情感")) {
//            View sex = getView(R.id.v_sex);
//            if (data.getGender().equals("男")) {
//                sex.setVisibility(View.VISIBLE);
//                sex.setBackgroundResource(R.drawable.ic_sex_boy);
//            } else if (data.getGender().equals("女")) {
//                sex.setVisibility(View.VISIBLE);
//                sex.setBackgroundResource(R.drawable.ic_sex_girl);
//            }
//        }
//        if (!data.getPhoto_thumbnail_src().isEmpty()) {
//            CircleImageView head = getView(R.id.civ_head);
//            image.load(data.getPhoto_thumbnail_src().replace("http://", "https://")).into(head);
//        }
//        title.setText(data.getTitle());
//        describe.setText(data.getDescription());
//        coin.setText(String.valueOf(data.getReward()));
//        String topicStr = "#" + data.getKind() + "#";
//        topic.setText(topicStr);
//        time.setText(getDateDifferenceDescribe(data.getDisappear_at()));
//        name.setText(data.getNickname());
//    }
}
