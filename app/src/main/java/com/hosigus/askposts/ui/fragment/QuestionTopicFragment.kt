package com.hosigus.askposts.ui.fragment

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.hosigus.askposts.R
import com.hosigus.askposts.config.QUESTION_LIST
import com.hosigus.askposts.item.Option.BaseNetOption
import com.hosigus.askposts.item.jsonBean.BaseBean
import com.hosigus.askposts.item.jsonBean.Question
import com.hosigus.askposts.ui.view.CircleImageView
import com.hosigus.askposts.utils.getDateDifferenceDescribe
import com.hosigus.askposts.utils.startQuestionDetailActivity
import com.hosigus.imageloader.ImageLoader
import com.hosigus.simplerecycleadapter.BaseAdapter
import com.hosigus.simplerecycleadapter.BaseHolder
import com.hosigus.simplerecycleadapter.SimpleAdapterBuilder
import com.hosigus.tools.interfaces.NetCallback
import com.hosigus.tools.items.JSONBean
import com.hosigus.tools.options.NetOption
import com.hosigus.tools.utils.NetUtils
import com.hosigus.tools.utils.ToastUtils
import org.json.JSONArray
import java.lang.Exception

/**
 * Created by 某只机智 on 2018/6/3.
 */
class QuestionTopicFragment : BaseFragment() {
    var kind:String = "全部"
        private set
    private var page = 1
    private lateinit var imageLoader:ImageLoader.ImageLoaderBuilder
    private val netOption: NetOption = BaseNetOption(QUESTION_LIST).param("size","12")

    private lateinit var adapter: BaseAdapter<Question>

    private lateinit var loadLL: LinearLayout
    private lateinit var srl: SwipeRefreshLayout


    override fun getResID(): Int {
        return R.layout.fragment_question_topic
    }

    override fun init(v: View) {
        imageLoader=ImageLoader.with(context)
                .place(R.drawable.ic_default_head)
                .error(R.drawable.ic_default_head)
        page = 2

        loadLL = v.findViewById(R.id.ll_load)
        srl = v.findViewById(R.id.srl_question)
        srl.setOnRefreshListener { refresh() }
        val rv: RecyclerView = v.findViewById(R.id.rv_question)

        adapter=SimpleAdapterBuilder<Question>(R.layout.item_question,
                {itemView-> object :BaseHolder<Question>(itemView){
                    override fun bindData(data: Question) {

                        val name = getView<TextView>(R.id.tv_name)
                        val time = getView<TextView>(R.id.tv_time)
                        val coin = getView<TextView>(R.id.tv_coin)
                        val topic = getView<TextView>(R.id.tv_topic)
                        val title = getView<TextView>(R.id.tv_title)
                        val describe = getView<TextView>(R.id.tv_describe)

                        title.text = data.title
                        describe.text = data.description
                        coin.text = data.reward.toString()
                        val topicStr = "#" + data.kind + "#"
                        topic.text = topicStr
                        time.text = getDateDifferenceDescribe(data.disappear_at!!)
                        name.text = data.nickname

                        if (kind.equals("情感")) {
                            val sex: View = getView(R.id.v_sex)
                            if (data.gender.equals("男")) {
                                sex.visibility = View.VISIBLE
                                sex.setBackgroundResource(R.drawable.ic_sex_boy)
                            }else if (data.gender.equals("女")) {
                                sex.visibility = View.VISIBLE
                                sex.setBackgroundResource(R.drawable.ic_sex_girl)
                            }
                        }

                        if (!data.photo_thumbnail_src.isNullOrBlank() && data.photo_thumbnail_src != "null") {
                            val head: CircleImageView = getView(R.id.civ_head)
                            imageLoader.load(data.photo_thumbnail_src!!.replace("http://", "https://"))
                                    .into(head)
                        }

                    }
                }})
                .onLoad { load() }
                .onClick { data ->  startQuestionDetailActivity(context,data.id.toString()) }
                .build()

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)

        refresh()
    }

    private fun load() {
        loadLL.visibility = View.VISIBLE
        NetUtils.post(netOption.param("page",page.toString()),object :NetCallback{
            override fun onSuccess(data: JSONBean) {
                loadLL.visibility=View.GONE
                if ((data as BaseBean).isGoodJson()) {
                    val arr: JSONArray = JSONArray(data.data)
                    val list: ArrayList<Question> = ArrayList()
                    for (i: Int in 0 until arr.length()) {
                        list.add(Question(arr.getJSONObject(i)))
                    }
                    adapter.addData(list)
                    page++
                } else {
                    ToastUtils.show("刷新失败,服务器错误")
                }
            }

            override fun onFail(e: Exception) {
                loadLL.visibility=View.GONE
                ToastUtils.show("刷新失败,网络错误")
            }
        })
    }

    private fun refresh() {
        NetUtils.post(netOption.param("page", "1"),object :NetCallback{
            override fun onSuccess(data: JSONBean?) {
                srl.isRefreshing=false
                if ((data as BaseBean).isGoodJson()) {
                    val arr = JSONArray(data.data)
                    val list: ArrayList<Question> = ArrayList()
                    for (i: Int in 0 until arr.length()) {
                        list.add(Question(arr.getJSONObject(i)))
                    }
                    adapter.refresh(list)
                    page = 2
                } else {
                    ToastUtils.show("刷新失败,服务器错误")
                }
            }

            override fun onFail(e: Exception) {
                e.printStackTrace()
                srl.isRefreshing=false
                ToastUtils.show("刷新失败,网络错误")
            }
        })

    }

    fun setKind(kind: String):QuestionTopicFragment {
        this.kind = kind
        netOption.param("kind", kind)
        return this
    }

}