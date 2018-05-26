package com.hosigus.askposts.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hosigus.askposts.App;
import com.hosigus.askposts.R;
import com.hosigus.askposts.item.Option.BaseOption;
import com.hosigus.askposts.item.jsonBean.BaseBean;
import com.hosigus.askposts.item.jsonBean.Question;
import com.hosigus.askposts.config.NetUrls;
import com.hosigus.askposts.ui.activity.QuestionDetailActivity;
import com.hosigus.askposts.ui.view.CircleImageView;
import com.hosigus.imageloader.ImageLoader;
import com.hosigus.simplerecycleadapter.BaseAdapter;
import com.hosigus.simplerecycleadapter.BaseHolder;
import com.hosigus.simplerecycleadapter.SimpleAdapterBuilder;
import com.hosigus.tools.interfaces.NetCallback;
import com.hosigus.tools.items.JSONBean;
import com.hosigus.tools.options.NetOption;
import com.hosigus.tools.utils.NetUtils;
import com.hosigus.tools.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by 某只机智 on 2018/5/26.
 */

public class QuestionTopicFragment extends Fragment {
    private ImageLoader.ImageLoaderBuilder image;
    private String kind;
    private int page;
    private NetOption loadOption;
    private BaseAdapter<Question> adapter;

    private LinearLayout load;
    private SwipeRefreshLayout srl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_question_topic,container,false);
        initView(v);
        initData();
        refresh();
        return v;
    }

    private void initView(View v) {
        load = v.findViewById(R.id.ll_load);
        adapter = new SimpleAdapterBuilder<Question>(R.layout.item_question,
                itemView -> new BaseHolder<Question>(itemView) {
                    @Override
                    public void bindData(Question data) {
                        TextView name = getView(R.id.tv_name),
                                time = getView(R.id.tv_time),
                                coin = getView(R.id.tv_coin),
                                topic = getView(R.id.tv_topic),
                                title = getView(R.id.tv_title),
                                describe = getView(R.id.tv_describe);
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
                            image.load(data.getPhoto_thumbnail_src().replace("http://", "https://")).into(head);
                        }
                        title.setText(data.getTitle());
                        describe.setText(data.getDescription());
                        coin.setText(String.valueOf(data.getReward()));
                        String topicStr = "#" + data.getKind() + "#";
                        topic.setText(topicStr);
                        time.setText(App.getDateDifferenceDescribe(data.getDisappear_at()));
                        name.setText(data.getNickname());
                    }
                }
        ).onLoad(() -> {
            load.setVisibility(View.VISIBLE);
            NetUtils.post(loadOption.param("page", String.valueOf(page)), new NetCallback() {
                @Override
                public void onSuccess(JSONBean data) {
                    load.setVisibility(View.GONE);
                    BaseBean baseBean = (BaseBean) data;
                    if (baseBean.isStatusOK() && baseBean.getData() != null && !baseBean.getData().isEmpty()) {
                        try {
                            JSONArray qArray = new JSONArray(baseBean.getData());
                            List<Question> newQList = new ArrayList<>();
                            for (int i = 0; i < qArray.length(); i++) {
                                newQList.add(new Question(qArray.getJSONObject(i)));
                            }
                            adapter.addData(newQList);
                            page++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.show("刷新失败，服务器错误");
                        }
                    } else {
                        ToastUtils.show("下面没有了");
                    }
                }

                @Override
                public void onFail(Exception e) {
                    load.setVisibility(View.GONE);
                    ToastUtils.show("网络错误");
                }
            });
        }).onClick(data -> QuestionDetailActivity.start(getContext(), data.getId())).build();

        RecyclerView rv = v.findViewById(R.id.rv_question);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        srl = v.findViewById(R.id.srl_question);
        srl.setOnRefreshListener(this::refresh);
    }

    private void initData() {
        image = ImageLoader
                .with(getContext())
                .place(R.drawable.ic_default_head)
                .error(R.drawable.ic_default_head);
        if (kind == null) {
            kind = "全部";
        }
        page = 1;
        loadOption = new BaseOption(NetUrls.QUESTION_LIST).param("size","6").param("kind",kind);
    }

    private void refresh() {
        NetUtils.post(loadOption.param("page", "1"), new NetCallback() {
            @Override
            public void onSuccess(JSONBean data) {
                srl.setRefreshing(false);
                BaseBean baseBean = (BaseBean) data;
                if (baseBean.isStatusOK() && baseBean.getData() != null && !baseBean.getData().isEmpty()) {
                    try {
                        JSONArray qArray = new JSONArray(baseBean.getData());
                        List<Question> newQList = new ArrayList<>();
                        for (int i = 0; i < qArray.length(); i++) {
                            newQList.add(new Question(qArray.getJSONObject(i)));
                        }
                        adapter.refresh(newQList);
                        page = 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtils.show("刷新失败，服务器错误");
                    }
                } else {
                    ToastUtils.show("刷新失败，服务器错误");
                }
            }

            @Override
            public void onFail(Exception e) {
                srl.setRefreshing(false);
                ToastUtils.show("网络错误");
            }
        });
    }

    public QuestionTopicFragment setKind(String kind) {
        this.kind = kind;
        if (loadOption != null) {
            loadOption.param("kind", kind);
        }
        return this;
    }
}
