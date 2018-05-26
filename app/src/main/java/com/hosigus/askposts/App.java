package com.hosigus.askposts;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.hosigus.askposts.item.jsonBean.User;
import com.hosigus.tools.utils.ToastUtils;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by 某只机智 on 2018/5/25.
 */

public class App extends Application {
    private static Context mContext;
    private static User me;
    private static boolean login;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        ToastUtils.setContext(mContext);
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        login = preferences.getBoolean("isLogin", false);
        if (isLogin()) {
            try {
                JSONObject o = new JSONObject(preferences.getString("me", null));
                me = new User(o);
            } catch (Exception e) {
                e.printStackTrace();
                preferences.edit().putBoolean("isLogin", false).apply();
                login = false;
            }
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static User getMe() {
        return me;
    }

    public static void setMe(User me, String pwd) {
        App.me = me;
        if (me == null) {
            App.login = false;
            return;
        }
        App.login = true;
        SharedPreferences preferences = mContext.getSharedPreferences("login", MODE_PRIVATE);
        preferences.edit()
                .putBoolean("isLogin", true)
                .putString("me", me.getJson())
                .putString(me.getStuNum(), pwd)
                .apply();
    }

    public static void setLogin(boolean isLogin) {
        login = isLogin;
        mContext.getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("isLogin", isLogin).apply();
    }

    public static boolean isLogin() {
        return login;
    }

    public static String getDateDifferenceDescribe(String now) {
        try {
            long d = dateFormat.parse(now).getTime() - Calendar.getInstance().getTime().getTime();
            d /= 1000;
            int day, hour;
            day = (int) d / (24 * 3600);
            hour = (int) d % (24 * 3600) / 3600;
            String re = (day > 0 ? day + "天" : "") + (hour > 0 ? hour + "小时" : "");
            return re.isEmpty() ? "已过时" : re + "后消失";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getPwd(String stuNum) {
        return mContext.getSharedPreferences("login", MODE_PRIVATE).getString(stuNum, "");
    }

}
