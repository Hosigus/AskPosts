package com.hosigus.askposts.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hosigus.askposts.App
import com.hosigus.askposts.R
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.askposts.utils.login
import com.hosigus.tools.interfaces.NetCallback
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

/**
 * Created by 某只机智 on 2018/6/3.
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dialog = ProgressDialog.show(this, "尝试登陆中", "请稍候")
        dialog.dismiss()

        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        if (!App.INSTANCE.pwd.isNullOrBlank()) {
            et_login_user.setText(App.INSTANCE.user!!.stuNum)
            et_login_pwd.setText(App.INSTANCE.pwd)
        }

        btn_login.setOnClickListener {
            val stuNum = et_login_user.text.toString()
            val pwd = et_login_pwd.text.toString()
            if (stuNum.isBlank() || pwd.isBlank()) {
                ToastUtils.show("用户名或密码为空")
                return@setOnClickListener
            }
            dialog.show()
            login(stuNum,pwd,object : NetCallback{
                override fun onSuccess(data: JSONBean) {
                    (data as BaseBean)
                    App.INSTANCE.loginSuccess(data.data!!, pwd)
                    ToastUtils.show("欢迎，"+App.INSTANCE.user!!.username)
                    dialog.dismiss()
                    finish()
                }

                override fun onFail(e: Exception) {
                    ToastUtils.show(e.message)
                    dialog.dismiss()
                }
            })
        }
    }
}