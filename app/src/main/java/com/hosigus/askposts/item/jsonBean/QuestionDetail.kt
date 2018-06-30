package com.hosigus.askposts.item.jsonBean

import com.hosigus.tools.items.JSONBean
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by 某只机智 on 2018/5/26.
 */

class QuestionDetail : JSONBean {
    /**
     * is_self : 0
     * title : 这个代码太难写了\\ue056
     * description : 代码是真的难
     * reward : 2
     * disappear_at : 2018-04-27 02:22:22
     * tags : PHP
     * kind : 其他
     * photo_urls : []
     * questioner_nickname : 。
     * questioner_photo_thumbnail_src :
     * questioner_gender : 女
     * answers : [{"id":"10","nickname":"Jay","photo_thumbnail_src":"http://wx.idsbllp.cn/cyxbsMobile/Public/photo/thumbnail/1503374869_593154551.png","gender":"男","content":"菜","created_at":"2018-04-22 14:08:50","praise_num":"0","comment_num":"0","is_adopted":"0","is_praised":0,"photo_url":[]}]
     */

    var is_self: Int = 0
    var title: String? = null
    var description: String? = null
    var reward: String? = null
    var disappear_at: String? = null
    var tags: String? = null
    var kind: String? = null
    var questioner_nickname: String? = null
    var questioner_photo_thumbnail_src: String? = null
    var questioner_gender: String? = null
    private var photo_urls: MutableList<String>? = null
    private var answers: MutableList<AnswersBean>? = null

    fun isSelf(): Boolean = is_self == 1

    constructor()

    constructor(data: JSONObject) {
        parser(data)
        json = data.toString()
    }

    override fun parser(`object`: JSONObject) {
        // TODO: 2018/5/26
        try {
            is_self = `object`.getInt("is_self")
            title = `object`.getString("title")
            description = `object`.getString("description")
            reward = `object`.getString("reward")
            disappear_at = `object`.getString("disappear_at")
            tags = `object`.getString("tags")
            kind = `object`.getString("kind")
            questioner_nickname = `object`.getString("questioner_nickname")
            questioner_photo_thumbnail_src = `object`.getString("questioner_photo_thumbnail_src")
            questioner_gender = `object`.getString("questioner_gender")
            val urlArray = `object`.getJSONArray("photo_urls")
            photo_urls = ArrayList()
            for (i in 0 until urlArray.length()) {
                photo_urls!!.add(urlArray.getString(i))
            }
            val answerArray = `object`.getJSONArray("answers")
            answers = ArrayList()
            for (i in 0 until answerArray.length()) {
                answers!!.add(AnswersBean(answerArray.getJSONObject(i)))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    fun getPhoto_urls(): List<String>? {
        return photo_urls
    }

    fun setPhoto_urls(photo_urls: MutableList<String>) {
        this.photo_urls = photo_urls
    }

    fun getAnswers(): List<AnswersBean>? {
        return answers
    }

    fun setAnswers(answers: MutableList<AnswersBean>) {
        this.answers = answers
    }

    class AnswersBean : JSONBean {
        /**
         * id : 10
         * nickname : Jay
         * photo_thumbnail_src : http://wx.idsbllp.cn/cyxbsMobile/Public/photo/thumbnail/1503374869_593154551.png
         * gender : 男
         * content : 菜
         * created_at : 2018-04-22 14:08:50
         * praise_num : 0
         * comment_num : 0
         * is_adopted : 0
         * is_praised : 0
         * photo_url : []
         */

        var id: String? = null
        var nickname: String? = null
        var photo_thumbnail_src: String? = null
        var gender: String? = null
        var content: String? = null
        var created_at: String? = null
        var praise_num: Int = 0
        var comment_num: Int = 0
        var is_adopted: Int = 0
        var is_praised: Int = 0
        private var photo_url: MutableList<String>? = null

        fun isPraised(): Boolean = is_praised == 1

        fun isAdopted(): Boolean = is_adopted == 1

        constructor()

        constructor(jsonObject: JSONObject) {
            parser(jsonObject)
            json = jsonObject.toString()
        }

        override fun parser(`object`: JSONObject) {
            try {
                id = `object`.getString("id")
                nickname = `object`.getString("nickname")
                photo_thumbnail_src = `object`.getString("photo_thumbnail_src")
                gender = `object`.getString("gender")
                content = `object`.getString("content")
                created_at = `object`.getString("created_at")
                praise_num = `object`.getInt("praise_num")
                comment_num = `object`.getInt("comment_num")
                is_adopted = `object`.getInt("is_adopted")
                is_praised = `object`.getInt("is_praised")
                photo_url = ArrayList()
                val photo_urlArray = `object`.getJSONArray("photo_url")
                for (i in 0 until photo_urlArray.length()) {
                    photo_url!!.add(photo_urlArray.getString(i))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        fun getPhoto_url(): List<String>? {
            return photo_url
        }

        fun setPhoto_url(photo_url: MutableList<String>) {
            this.photo_url = photo_url
        }

        class Commend : JSONBean {

            /**
             * content : 测试
             * created_at : 2018-02-24 01:45:56
             * nickname : 溟\\n濛
             * photo_thumbnail_src : http://wx.idsbllp.cn/cyxbsMobile/Public/photo/thumbnail/1503374918_2132490885.png
             * gender : 男
             */

            var content: String? = null
            var created_at: String? = null
            var nickname: String? = null
            var photo_thumbnail_src: String? = null
            var gender: String? = null

            constructor()

            constructor(`object`: JSONObject) {
                parser(`object`)
                json = `object`.toString()
            }

            override fun parser(`object`: JSONObject) {
                try {
                    content = `object`.getString("content")
                    created_at = `object`.getString("created_at")
                    nickname = `object`.getString("nickname")
                    photo_thumbnail_src = `object`.getString("photo_thumbnail_src")
                    gender = `object`.getString("gender")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

        }
    }
}
