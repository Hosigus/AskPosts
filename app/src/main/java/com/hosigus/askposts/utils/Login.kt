package com.hosigus.askposts.utils

import com.hosigus.askposts.config.INFO
import com.hosigus.askposts.config.LOGIN
import com.hosigus.askposts.item.Option.BaseNetOption
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.tools.interfaces.NetCallback
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.items.NetCallbackImpl
import com.hosigus.tools.utils.NetUtils
import java.lang.Exception

/**
 * Created by 某只机智 on 2018/5/30.
 */
fun login(id: String, pwd: String, callback: NetCallback) {
    NetUtils.post(BaseNetOption(LOGIN).param("stuNum", id).param("idNum", pwd), object : NetCallback{
        override fun onSuccess(data: JSONBean) {
            (data as BaseBean)
            if (data.isGoodJson()) {
                NetUtils.post(BaseNetOption(INFO).param("stuNum", id).param("idNum", pwd),object : NetCallbackImpl(){
                    override fun onSuccess(data: JSONBean?) {
                        callback.onSuccess(data)
                    }
                })
            } else {
                callback.onFail(Exception("用户名或密码错误"))
            }
        }

        override fun onFail(e: Exception?) {
            callback.onFail(Exception("网络错误"))
        }

    })
}

fun login(callback: NetCallback) {
    NetUtils.post(BaseNetOption(LOGIN).addUserParam(), callback)
}