package com.hosigus.askposts.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.hosigus.askposts.R
import com.hosigus.askposts.config.ANSWER_QUESTION
import com.hosigus.askposts.item.Option.BaseNetOption
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.tools.interfaces.NetCallback
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.utils.NetUtils
import com.hosigus.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_post_answer.*
import java.lang.Exception

/**
 * Created by 某只机智 on 2018/6/30.
 */
class PostAnswerActivity : AppCompatActivity() {
    val result: Intent = Intent()
    lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_answer)

        result.putExtra("RESULT", false)
        val qId = intent.getStringExtra("qId")
        if (qId.isBlank()) {
            ToastUtils.show("未知问题")
            finish()
        }

        toolbar.title = ""
        setSupportActionBar(toolbar)

        dialog = ProgressDialog.show(this, "尝试提交中", "请稍候")
        dialog.dismiss()

        btn_cancel.setOnClickListener{
            finish()
        }

        et_help.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val leftSize = 300 - s.toString().length
                tv_size_left.text = leftSize.toString()
                if (leftSize < 0) {
                    tv_size_left.setTextColor(ContextCompat.getColor(this@PostAnswerActivity, R.color.red))
                } else {
                    tv_size_left.setTextColor(ContextCompat.getColor(this@PostAnswerActivity, R.color.black))
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btn_submit.setOnClickListener{
            if (300 - et_help.text.toString().length < 0) {
                ToastUtils.show("字数过限了哦，精简一下吧")
                return@setOnClickListener
            }
            dialog.show()
            NetUtils.post(BaseNetOption(ANSWER_QUESTION)
                    .addUserParam()
                    .param("content",et_help.text.toString())
                    .param("question_id",qId),
                    object : NetCallback {
                        override fun onSuccess(data: JSONBean?) {
                            data as BaseBean
                            if (data.isGoodJson()) {
                                result.putExtra("RESULT", true)
                                ToastUtils.show("回答成功")
                                finish()
                            }
                            dialog.dismiss()
                        }

                        override fun onFail(e: Exception?) {
                            ToastUtils.show("网络错误")
                            dialog.dismiss()
                        }
                    })
        }
    }

    override fun finish() {
        this.setResult(com.hosigus.askposts.utils.POST_ANSWER_RESULT,result)
        super.finish()
    }
}