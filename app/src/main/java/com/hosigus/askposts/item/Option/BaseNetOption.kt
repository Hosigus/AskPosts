package com.hosigus.askposts.item.Option

import com.hosigus.askposts.App
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.tools.options.NetOption

/**
 * Created by 某只机智 on 2018/5/30.
 */
class BaseNetOption(url: String) : NetOption(url) {
    init {
        beanClass(BaseBean::class.java)

    }

    fun addUserParam() =
            param("stuNum", App.INSTANCE.user!!.stuNum).param("idNum", App.INSTANCE.pwd!!)!!

}