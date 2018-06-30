package com.hosigus.askposts.ui.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.hosigus.askposts.R
import com.hosigus.askposts.config.*
import com.hosigus.askposts.item.Option.BaseNetOption
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.askposts.item.jsonBean.QuestionDetail
import com.hosigus.askposts.ui.view.CircleImageView
import com.hosigus.askposts.utils.POST_ANSWER_RESULT
import com.hosigus.askposts.utils.getDateDifferenceDescribe
import com.hosigus.askposts.utils.startAnswerDetailActivity
import com.hosigus.askposts.utils.startPostAnswerActivity
import com.hosigus.imageloader.ImageLoader
import com.hosigus.simplerecycleadapter.BaseAdapter
import com.hosigus.simplerecycleadapter.BaseHolder
import com.hosigus.tools.interfaces.NetCallback
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.options.NetOption
import com.hosigus.tools.utils.NetUtils
import com.hosigus.tools.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_question_detail.*
import org.json.JSONObject
import java.lang.Exception
import java.util.*

/**
 * Created by 某只机智 on 2018/6/30.
 */
class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var id: String
    private lateinit var refreshNetOption: NetOption
    private lateinit var cancelNetOption: NetOption
    private lateinit var detail: QuestionDetail
    private val adapter by lazy{
        object :BaseAdapter<QuestionDetail.AnswersBean>(listOf()){
            private val TYPE_HEAD = 1

            override fun getResId(viewType: Int) = when (viewType) {
                TYPE_HEAD -> R.layout.item_question_detail_head
                else -> R.layout.item_answer
            }

            override fun getItemViewType(position: Int) = if (position==0) TYPE_HEAD else -1

            override fun onCreateHolder(itemView: View?) = object : BaseHolder<QuestionDetail.AnswersBean>(itemView){
                private var isChangingLike = false
                private var isChangingAdopt = false

                override fun bindData(data: QuestionDetail.AnswersBean) {
                    when (itemViewType) {
                        TYPE_HEAD -> {
                            if (detail.kind == "情感") {
                                val sex = getView<View>(R.id.v_sex)
                                if (detail.questioner_gender == "男") {
                                    sex.visibility = View.VISIBLE
                                    sex.setBackgroundResource(R.drawable.ic_sex_boy)
                                } else if (detail.questioner_gender == "女") {
                                    sex.visibility = View.VISIBLE
                                    sex.setBackgroundResource(R.drawable.ic_sex_girl)
                                }
                            }
                            if (!detail.questioner_photo_thumbnail_src.isNullOrBlank()
                                    && detail.questioner_photo_thumbnail_src != "null") {
                                imageBuilder.load(detail.questioner_photo_thumbnail_src!!.replace("http://", "https://"))
                                        .into(getView<CircleImageView>(R.id.civ_head))
                            }

                            val name = getView<TextView>(R.id.tv_name)
                            val time = getView<TextView>(R.id.tv_time)
                            val coin = getView<TextView>(R.id.tv_coin)
                            val topic = getView<TextView>(R.id.tv_topic)
                            val title = getView<TextView>(R.id.tv_title)
                            val describe = getView<TextView>(R.id.tv_describe)
                            title.text = detail.title
                            describe.text = detail.description
                            coin.text = detail.reward.toString()
                            val topicStr = "#" + detail.kind + "#"
                            topic.text = topicStr
                            time.text = getDateDifferenceDescribe(detail.disappear_at!!)
                            name.text = detail.questioner_nickname

                            val photoList = detail.getPhoto_urls()!!
                            val photoVList = ArrayList<ImageView>()
                            if (photoList.isNotEmpty()) {
                                val p1 = getView<ImageView>(R.id.iv_1)
                                photoVList.add(p1)
                                if (photoList.size > 1) {
                                    val p2 = getView<ImageView>(R.id.iv_2)
                                    photoVList.add(p2)
                                    if (photoList.size > 2) {
                                        val p3 = getView<ImageView>(R.id.iv_3)
                                        photoVList.add(p3)
                                        if (photoList.size > 3) {
                                            val p4 = getView<ImageView>(R.id.iv_4)
                                            photoVList.add(p4)
                                        }
                                    }
                                }
                            }

                            imageBuilder.place(null)
                            for (i in photoList.indices) {
                                photoVList[i].visibility = View.VISIBLE
                                imageBuilder.load(photoList[i].replace("http://", "https://")).into(photoVList[i])
                            }
                            imageBuilder.place(R.drawable.ic_default_head)

                            val num = getView<TextView>(R.id.tv_answer_num)
                            val numStr = (detail.getAnswers()!!.size - 1).toString() + "个回答"
                            num.text = numStr
                            if (detail.getAnswers()!!.size - 1 != 0) {
                                val rl = getView<RelativeLayout>(R.id.rl_no_answer)
                                rl.visibility = View.GONE
                            }
                        }
                        else -> {
                            if (detail.kind == "情感") {
                                val sex = getView<View>(R.id.v_sex)
                                if (data.gender == "男") {
                                    sex.visibility = View.VISIBLE
                                    sex.setBackgroundResource(R.drawable.ic_sex_boy)
                                } else if (data.gender == "女") {
                                    sex.visibility = View.VISIBLE
                                    sex.setBackgroundResource(R.drawable.ic_sex_girl)
                                }
                            }
                            if (!data.photo_thumbnail_src.isNullOrBlank()
                                    && data.photo_thumbnail_src != "null") {
                                val head = getView<CircleImageView>(R.id.civ_head)
                                imageBuilder.load(data.photo_thumbnail_src!!.replace("http://", "https://"))
                                        .into(head)
                            }
                            val name = getView<TextView>(R.id.tv_name)
                            val describe = getView<TextView>(R.id.tv_describe)
                            val time = getView<TextView>(R.id.tv_time)
                            val comment = getView<TextView>(R.id.tv_comment)
                            val like = getView<TextView>(R.id.tv_like)
                            val likeBtn = getView<View>(R.id.v_like)
                            name.text = data.nickname
                            describe.text = data.content
                            time.text = data.created_at!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                            comment.text = data.comment_num.toString()
                            like.text = data.praise_num.toString()
                            if (data.isAdopted()) {
                                getView<LinearLayout>(R.id.ll_accept).visibility = View.VISIBLE
                            } else if (detail.isSelf()) {
                                val btn = getView<TextView>(R.id.btn_accept)
                                btn.visibility = View.VISIBLE
                                btn.setOnClickListener {
                                    if (isChangingAdopt) {
                                        return@setOnClickListener
                                    }
                                    isChangingAdopt = true
                                    NetUtils.post(BaseNetOption(ADOPTED)
                                            .addUserParam()
                                            .param("answer_id", data.id)
                                            .param("question_id", id), object : NetCallback {
                                        override fun onSuccess(data_adopt: JSONBean) {
                                            if ((data_adopt as BaseBean).isStatusOK()) {
                                                btn.visibility = View.GONE
                                                getView<LinearLayout>(R.id.ll_accept).visibility = View.VISIBLE
                                                data.is_adopted = 1
                                            }
                                        }

                                        override fun onFail(e: Exception) {
                                            isChangingAdopt = false
                                            ToastUtils.show("网络出错")
                                        }
                                    })
                                }
                            }
                            if (data.isPraised()) {
                                likeBtn.setBackgroundResource(R.drawable.ic_good_light)
                                likeBtn.isClickable = false
                            } else {
                                likeBtn.setOnClickListener {
                                    if (isChangingLike) {
                                        return@setOnClickListener
                                    }
                                    isChangingLike = true
                                    if (!data.isPraised()) {
                                        NetUtils.post(BaseNetOption(LIKE)
                                                .addUserParam()
                                                .param("answer_id", data.id), object : NetCallback {
                                            override fun onSuccess(data_like: JSONBean) {
                                                isChangingLike = false
                                                if ((data_like as BaseBean).isStatusOK()) {
                                                    likeBtn.setBackgroundResource(R.drawable.ic_good_light)
                                                    data.is_praised = 1
                                                    data.praise_num = data.praise_num + 1
                                                    like.text = data.praise_num.toString()
                                                } else {
                                                    ToastUtils.show("点赞失败")
                                                }
                                            }

                                            override fun onFail(e: Exception) {
                                                isChangingLike = false
                                                ToastUtils.show("服务器错误")
                                            }
                                        })
                                    } else {
                                        NetUtils.post(BaseNetOption(UNLIKE)
                                                .addUserParam()
                                                .param("answer_id", data.id), object : NetCallback {
                                            override fun onSuccess(data_like: JSONBean) {
                                                isChangingLike = false
                                                if ((data_like as BaseBean).isStatusOK()) {
                                                    likeBtn.setBackgroundResource(R.drawable.ic_good_dark)
                                                    data.is_praised = 0
                                                    data.praise_num = data.praise_num - 1
                                                    like.text = data.praise_num.toString()
                                                } else {
                                                    ToastUtils.show("取消失败")
                                                }
                                            }

                                            override fun onFail(e: Exception) {
                                                isChangingLike = false
                                                ToastUtils.show("服务器错误")
                                            }
                                        })
                                    }

                                }
                            }
                            val unfold = getView<TextView>(R.id.unfold)
                            unfold.setOnClickListener {
                                startAnswerDetailActivity(this@QuestionDetailActivity, data.json, detail)
                            }
                        }
                    }
                }

            }

        }
    }
    private val refreshCallback by lazy { object : NetCallback {
        override fun onSuccess(data: JSONBean?) {
            data as BaseBean
            srl_question_detail.isRefreshing = false
            if (!data.isGoodJson()) {
                ToastUtils.show("服务器错误")
                finish()
                return
            }
            if (tv_left.text.isBlank()) {
                detail = QuestionDetail(JSONObject(data.data))
                initView()
            } else {
                detail = QuestionDetail(JSONObject(data.data))
                adapter.refresh(detail.getAnswers())
            }
        }
        override fun onFail(e: Exception?) {
            ToastUtils.show("网络错误")
            srl_question_detail.isRefreshing = false
            finish()
        }
    }}
    private val cancelCallback by lazy { object : NetCallback {
        override fun onSuccess(data: JSONBean) {
            if ((data as BaseBean).isStatusOK()) {
                ToastUtils.show("问题已取消")
                finish()
            } else {
                ToastUtils.show("取消失败")
            }
        }
        override fun onFail(e: Exception) {
            ToastUtils.show("服务器错误")
        }
    }}
    private val imageBuilder by lazy { ImageLoader.with(this).place(R.drawable.ic_default_head) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        id = intent.getStringExtra("questionId")
        refreshNetOption = BaseNetOption(QUESTION_DETAIL).addUserParam().param("question_id", id)
        cancelNetOption = BaseNetOption(CANCEL_ASK).addUserParam().param("question_id", id)
        refresh()
    }

    private fun initView() {
        lateinit var leftD: Drawable
        lateinit var rightD: Drawable
        lateinit var leftListener:View.OnClickListener
        lateinit var rightListener:View.OnClickListener

        if (detail.isSelf()) {
            tv_left.text = "加价"
            tv_right.text = "取消提问"
            leftD = ContextCompat.getDrawable(this, R.drawable.ic_add_coin)
            rightD = ContextCompat.getDrawable(this, R.drawable.ic_cancel)
            leftListener = View.OnClickListener {
                // TODO: 加价
            }
            rightListener = View.OnClickListener { NetUtils.post(cancelNetOption, cancelCallback) }
        } else {
            tv_left.text = "忽略"
            tv_right.text = "帮助"
            leftD = ContextCompat.getDrawable(this, R.drawable.ic_ignore)
            rightD = ContextCompat.getDrawable(this, R.drawable.ic_help)
            leftListener = View.OnClickListener {
                // TODO: 忽略
            }
            rightListener = View.OnClickListener {
                startPostAnswerActivity(this, id)
            }
        }

        leftD.setBounds(0, 0, leftD.minimumWidth, leftD.minimumHeight)
        rightD.setBounds(0, 0, rightD.minimumWidth, rightD.minimumHeight)
        tv_left.setCompoundDrawables(leftD, null, null, null)
        tv_right.setCompoundDrawables(rightD, null, null, null)
        tv_left.setOnClickListener(leftListener)
        tv_right.setOnClickListener(rightListener)

        rv_question_detail.layoutManager = LinearLayoutManager(this)
        rv_question_detail.adapter = adapter

        srl_question_detail.setOnRefreshListener {
            if (srl_question_detail.isRefreshing) {
                refresh()
            }
        }
    }

    private fun refresh() {
        NetUtils.post(refreshNetOption,refreshCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share_report, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            POST_ANSWER_RESULT -> if (data != null && data.getBooleanExtra("RESULT", false)) {
                refresh()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}