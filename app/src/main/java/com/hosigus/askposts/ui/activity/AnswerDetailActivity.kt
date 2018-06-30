package com.hosigus.askposts.ui.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.hosigus.askposts.R
import com.hosigus.askposts.config.COMMEND_ANSWER
import com.hosigus.askposts.config.GET_COMMEND_LIST
import com.hosigus.askposts.config.LIKE
import com.hosigus.askposts.config.UNLIKE
import com.hosigus.askposts.item.Option.BaseNetOption
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.askposts.item.jsonBean.QuestionDetail
import com.hosigus.askposts.utils.setHeadImage
import com.hosigus.askposts.utils.setSex
import com.hosigus.imageloader.ImageLoader
import com.hosigus.simplerecycleadapter.BaseAdapter
import com.hosigus.simplerecycleadapter.BaseHolder
import com.hosigus.tools.interfaces.NetCallback
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.utils.NetUtils
import com.hosigus.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_answer_detail.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by 某只机智 on 2018/6/4.
 */
class AnswerDetailActivity : AppCompatActivity() {
    private lateinit var kind:String
    private lateinit var title:String

    private lateinit var answer:QuestionDetail.AnswersBean
    private var isChangingLike: Boolean = false
    private var adapter: BaseAdapter<QuestionDetail.AnswersBean.Commend> = object :BaseAdapter<QuestionDetail.AnswersBean.Commend>(){
        private val TYPE_HEAD = 0
        private val TYPE_DEFAULT = 100
        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> TYPE_HEAD
                else -> TYPE_DEFAULT
            }
        }

        override fun getResId(viewType: Int): Int {
            return when (viewType) {
                TYPE_DEFAULT -> R.layout.item_commend
                TYPE_HEAD -> R.layout.item_answer_detail_head
                else -> -1
            }
        }
        override fun onCreateHolder(itemView: View?): BaseHolder<QuestionDetail.AnswersBean.Commend> {
            return object : BaseHolder<QuestionDetail.AnswersBean.Commend>(itemView) {
                override fun bindData(data: QuestionDetail.AnswersBean.Commend) {
                    when (itemViewType) {
                        TYPE_DEFAULT -> {
                            setSex(getView(R.id.v_sex), data.gender!!, kind)
                            setHeadImage(this@AnswerDetailActivity,
                                    getView(R.id.civ_head), data.photo_thumbnail_src!!)
                            val name: TextView = getView(R.id.tv_name)
                            val describe: TextView = getView(R.id.tv_describe)
                            val time: TextView = getView(R.id.tv_time)
                            name.text = data.nickname
                            describe.text = data.content
                            time.text = data.created_at!!.split(" ")[0]
                        }
                        TYPE_HEAD -> {

                            setSex(getView(R.id.v_sex), answer.gender!!, kind)
                            setHeadImage(this@AnswerDetailActivity,
                                    getView(R.id.civ_head), answer.photo_thumbnail_src!!)

                            val name: TextView = getView(R.id.tv_name)
                            val time: TextView = getView(R.id.tv_time)
                            val topic: TextView = getView(R.id.tv_topic)
                            val title: TextView = getView(R.id.tv_title)
                            val describe: TextView = getView(R.id.tv_describe)

                            name.text = answer.nickname
                            time.text = answer.created_at!!.split(" ")[0]
                            val topicStr = "#$kind$#"
                            topic.text = topicStr
                            title.text = this@AnswerDetailActivity.title
                            describe.text = answer.content

                            val photoUrlList: ArrayList<String> = ArrayList()
                            val photoVList: ArrayList<ImageView> = ArrayList()

                            if (photoUrlList.size > 0) {
                                val p1 = getView<ImageView>(R.id.iv_1)
                                photoVList.add(p1)
                            }
                            if (photoUrlList.size > 1) {
                                val p2 = getView<ImageView>(R.id.iv_2)
                                photoVList.add(p2)
                            }
                            if (photoUrlList.size > 2) {
                                val p3 = getView<ImageView>(R.id.iv_3)
                                photoVList.add(p3)
                            }
                            if (photoUrlList.size > 3) {
                                val p4 = getView<ImageView>(R.id.iv_4)
                                photoVList.add(p4)
                            }

                            for (i in photoUrlList.indices) {
                                photoVList[i].visibility = View.VISIBLE
                                ImageLoader.with(this@AnswerDetailActivity).load(photoUrlList.get(i).replace("http://", "https://")).into(photoVList[i])
                            }
                            val num = getView<TextView>(R.id.tv_answer_num)
                            val numStr = "评论" + (itemCount - 1).toString()
                            num.text = numStr
                            if (itemCount != 1) {
                                val rl = getView<RelativeLayout>(R.id.rl_no_answer)
                                rl.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private lateinit var dialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_detail)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        initData()
        initView()
        refreshCommendData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share_report, menu)
        return true
    }

    private fun initData() {
        try {
            kind = intent.getStringExtra("kind")
            title = intent.getStringExtra("title")
            answer = QuestionDetail.AnswersBean(JSONObject(intent.getStringExtra("answerJson")))
        } catch (e:Exception) {
            ToastUtils.show("出错")
            finish()
        }
    }

    private fun initView() {
        val changeLikeBtn: TextView = findViewById(R.id.tv_left)
        val commendBtn: TextView = findViewById(R.id.tv_right)

        dialog = ProgressDialog.show(this, "正在提交评论", "请稍候")
        dialog.dismiss()

        val light: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_good_light)
        val dark: Drawable = ContextCompat.getDrawable(this, R.drawable.ic_good_dark2)
        light.setBounds(0, 0, light.minimumWidth, light.minimumHeight)
        dark.setBounds(0, 0, dark.minimumWidth, dark.minimumHeight)

        if (answer.isPraised()) {
            changeLikeBtn.setCompoundDrawables(light, null, null, null)
        }

        changeLikeBtn.setOnClickListener{
            if (isChangingLike) {
                return@setOnClickListener
            }
            isChangingLike = true
            if (!answer.isPraised()) {
                NetUtils.post(
                        BaseNetOption(LIKE)
                                .addUserParam()
                                .param("answer_id", answer.id),
                        object : NetCallback {
                            override fun onSuccess(data: JSONBean) {
                                isChangingLike = false
                                if ((data as BaseBean).isStatusOK()) {
                                    changeLikeBtn.setCompoundDrawables(light, null, null, null)
                                    answer.is_praised = 1
                                    answer.praise_num += 1
                                    changeLikeBtn.text = answer.praise_num.toString()
                                } else {
                                    ToastUtils.show("点赞失败")
                                }
                            }

                            override fun onFail(e: java.lang.Exception) {
                                isChangingLike = false
                                ToastUtils.show("服务器错误")
                            }
                        }
                )
            } else {
                NetUtils.post(
                        BaseNetOption(UNLIKE)
                                .addUserParam()
                                .param("answer_id", answer.id),
                        object : NetCallback {
                            override fun onSuccess(data: JSONBean?) {
                                isChangingLike = false
                                if ((data as BaseBean).isStatusOK()) {
                                    changeLikeBtn.setCompoundDrawables(dark, null, null, null)
                                    answer.is_praised = 0
                                    answer.praise_num -= 1
                                    changeLikeBtn.text = answer.praise_num.toString()
                                } else {
                                    ToastUtils.show("取消失败")
                                }
                            }

                            override fun onFail(e: java.lang.Exception?) {
                                isChangingLike = false
                                ToastUtils.show("服务器错误")
                            }

                        }
                )
            }
        }

        mask.setOnClickListener { et_commend.clearFocus() }
        et_commend.setOnFocusChangeListener { _, hasFocus ->
            val imm: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (!hasFocus) {
                imm.hideSoftInputFromWindow(cl_commend.windowToken, 0)
                cl_commend.visibility = View.GONE
                mask.visibility = View.GONE
            }
        }
        commendBtn.setOnClickListener {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            cl_commend.visibility = View.VISIBLE
            mask.visibility = View.VISIBLE
            et_commend.isFocusable = true
            et_commend.requestFocus()
            imm.showSoftInput(et_commend, 0)
        }
        btn_send.setOnClickListener {
            val commendStr = et_commend.text.toString()
            if (commendStr.isBlank()) {
                ToastUtils.show("评论为空")
                return@setOnClickListener
            }
            dialog.show()
            NetUtils.post(
                    BaseNetOption(COMMEND_ANSWER)
                            .addUserParam()
                            .param("answer_id", answer.id)
                            .param("content", commendStr),
                    object : NetCallback {
                        override fun onSuccess(data: JSONBean?) {
                            dialog.dismiss()
                            if ((data as BaseBean).isStatusOK()) {
                                et_commend.setText("")
                                et_commend.clearFocus()
                                refreshCommendData()
                            } else {
                                ToastUtils.show("评论失败")
                            }
                        }

                        override fun onFail(e: java.lang.Exception?) {
                            dialog.dismiss()
                            ToastUtils.show("网络错误")
                        }
                    }
            )
        }

        rv_question_detail.layoutManager = LinearLayoutManager(this)
        rv_question_detail.adapter = adapter
    }

    private fun refreshCommendData() {
        NetUtils.post(
                BaseNetOption(GET_COMMEND_LIST)
                    .addUserParam()
                    .param("answer_id", answer.id),
                object : NetCallback{
                    override fun onSuccess(data: JSONBean?) {
                        if ((data as BaseBean).isGoodJson()) {
                            refreshCommendView(data.data!!)
                        } else {
                            ToastUtils.show("服务器错误")
                            finish()
                        }
                    }
                    override fun onFail(e: java.lang.Exception?) {
                        ToastUtils.show("服务器错误")
                        finish()
                    }
                })
    }

    private fun refreshCommendView(data: String) {
        val commendList: ArrayList<QuestionDetail.AnswersBean.Commend> = ArrayList()
        commendList.add(QuestionDetail.AnswersBean.Commend())
        val commendArray = JSONArray(data)
        for (i: Int in 0 until commendArray.length()) {
            commendList.add(QuestionDetail.AnswersBean.
                    Commend(commendArray.getJSONObject(i)))
        }
        adapter.refresh(commendList)
    }

}