package com.hosigus.askposts.item.jsonBean

import com.hosigus.tools.items.JSONBean

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by 某只机智 on 2018/5/26.
 */

class Question : JSONBean {
    /**
     * title : 又在写bug\\ue056
     * description : bug是真的多
     * kind : 其他
     * tags : PHP
     * reward : 2
     * answer_num : 0
     * disappear_at : 2019-02-27 01:11:20
     * created_at : 2018-05-19 17:35:14
     * is_anonymous : 0
     * id : 52
     * photo_thumbnail_src :
     * nickname : 。
     * gender : 女
     */

    var title: String? = null
    var description: String? = null
    var kind: String? = null
    var tags: String? = null
    var reward: Int = 0
    var answer_num: Int = 0
    var disappear_at: String? = null
    var created_at: String? = null
    var is_anonymous: Int = 0
    var id: Int = 0
    var photo_thumbnail_src: String? = null
    var nickname: String? = null
    var gender: String? = null

    constructor()

    constructor(question: JSONObject) {
        parser(question)
    }

    override fun parser(`object`: JSONObject) {
        try {
            title = `object`.getString("title")
            description = `object`.getString("description")
            kind = `object`.getString("kind")
            tags = `object`.getString("tags")
            reward = `object`.getInt("reward")
            answer_num = `object`.getInt("answer_num")
            disappear_at = `object`.getString("disappear_at")
            created_at = `object`.getString("created_at")
            is_anonymous = `object`.getInt("is_anonymous")
            id = `object`.getInt("id")
            photo_thumbnail_src = `object`.getString("photo_thumbnail_src")
            nickname = `object`.getString("nickname")
            gender = `object`.getString("gender")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}
