package com.hosigus.askposts

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.askposts.item.jsonBean.User
import com.hosigus.askposts.utils.login
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.items.NetCallbackImpl
import com.hosigus.tools.utils.ToastUtils
import org.json.JSONObject

/**
 * Created by 某只机智 on 2018/5/30.
 */
class App : Application() {
    lateinit var context:Context
        private set
    var user: User? = null
        private set
    var pwd: String? = null
        private set
    var isLogin: Boolean = false
        private set

    private lateinit var sp: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        context = applicationContext
        ToastUtils.setContext(context)

        sp = getSharedPreferences("login", Context.MODE_PRIVATE)
        val lastUserID = sp.getString("last_login", null)
        if (!lastUserID.isNullOrBlank()) {
            user = User(JSONObject(sp.getString(lastUserID, "")))
            pwd = sp.getString(lastUserID + "_pwd", "")
            login(object :NetCallbackImpl(){
                override fun onSuccess(data: JSONBean?) {
                    data as BaseBean
                    if (data.isGoodJson()) {
                        isLogin = true
                    }
                }
            })
        }
    }

    fun loginSuccess(userJson: String, pwd: String) {
        this.user = User(JSONObject(userJson))
        this.pwd = pwd
        sp.edit().putString("last_login", user!!.stuNum)
                .putString(user!!.stuNum, userJson)
                .putString(user!!.stuNum + "_pwd", pwd)
                .apply()
        isLogin = true
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var INSTANCE:App
            private set
    }
}