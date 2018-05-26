package com.hosigus.askposts.ui.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.hosigus.askposts.App;
import com.hosigus.askposts.R;
import com.hosigus.askposts.item.jsonBean.BaseBean;
import com.hosigus.askposts.item.Option.BaseOption;
import com.hosigus.askposts.item.jsonBean.User;
import com.hosigus.askposts.config.NetUrls;
import com.hosigus.tools.interfaces.NetCallback;
import com.hosigus.tools.items.JSONBean;
import com.hosigus.tools.items.NetCallbackImpl;
import com.hosigus.tools.utils.NetUtils;
import com.hosigus.tools.utils.ThreadUtils;
import com.hosigus.tools.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private AppCompatEditText userET, pwdET;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = ProgressDialog.show(this, "尝试登陆中", "请稍候");
        dialog.dismiss();

        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitle("");
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(v-> finish());

        userET = findViewById(R.id.et_login_user);
        pwdET = findViewById(R.id.et_login_pwd);
        Button loginBtn = findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(v-> login());

        if (App.isLogin()) {
            String id = App.getMe().getStuNum();
            String pwd = App.getPwd(App.getMe().getStuNum());
            userET.setText(id);
            pwdET.setText(pwd);
            login();
        }
    }

    private void login() {
        dialog.show();
        NetUtils.post(new BaseOption(NetUrls.LOGIN)
                        .param("stuNum", userET.getText().toString())
                        .param("idNum", pwdET.getText().toString())
                , new NetCallback() {
                    @Override
                    public void onSuccess(JSONBean data) {
                        dialog.dismiss();
                        if (!((BaseBean) data).isStatusOK()) {
                            ToastUtils.show("用户名或密码错误");
                            App.setLogin(false);
                            return;
                        }
                        if (!App.isLogin()) {
                            NetUtils.post(new BaseOption(NetUrls.INFO)
                                    .param("stuNum", userET.getText().toString())
                                    .param("idNum", pwdET.getText().toString()), new NetCallbackImpl(){
                                @Override
                                public void onSuccess(JSONBean data) {
                                    ToastUtils.show("欢迎！");
                                    User me = null;
                                    try {
                                        me = new User(new JSONObject(((BaseBean) data).getData()));
                                        me.setJson(((BaseBean) data).getData());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    App.setMe(me, pwdET.getText().toString());
                                    MainActivity.start(LoginActivity.this);
                                    finish();
                                }
                            });
                        }else {
                            MainActivity.start(LoginActivity.this);
                            finish();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        ThreadUtils.getInstance().post(()->{
                            e.printStackTrace();
                            dialog.dismiss();
                            ToastUtils.show("网络错误");
                        });
                    }
                });
    }
}
