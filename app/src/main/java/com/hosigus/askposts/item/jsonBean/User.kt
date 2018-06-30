package com.hosigus.askposts.item.jsonBean

import com.hosigus.tools.items.JSONBean

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by 某只机智 on 2018/5/25.
 */

class User : JSONBean {

    /*
     "id": 2828,
     "stunum": "2016210xxx",
     "introduction": "An Android Developer",
     "username": "李吉",
     "nickname": "Jay",
     "gender": "男",
     "photo_thumbnail_src": "http://wx.idsbllp.cn/cyxbsMobile/Public/photo/thumbnail/1520430636_1133226083.png",
     "photo_src": "http://wx.idsbllp.cn/cyxbsMobile/Public/photo/1520430636_1133226083.png",
     "updated_time": "2018-05-05 23:15:35",
     "phone": "159235xxxxx",
     "qq": "14325xxxxx"
     */

    var id: Int = 0
    var stuNum: String? = null
    var introduction: String? = null
    var username: String? = null
    var nickname: String? = null
    var gender: String? = null
    var photo_thumbnail_src: String? = null
    var photo_src: String? = null
    var updated_time: String? = null
    var phone: String? = null
    var qq: String? = null

    constructor()

    constructor(`object`: JSONObject) {
        parser(`object`)
    }

    override fun parser(`object`: JSONObject) {
        try {
            id = `object`.getInt("id")
            stuNum = `object`.getString("stunum")
            introduction = `object`.getString("introduction")
            username = `object`.getString("username")
            nickname = `object`.getString("nickname")
            gender = `object`.getString("gender")
            photo_thumbnail_src = `object`.getString("photo_thumbnail_src")
            photo_src = `object`.getString("photo_src")
            updated_time = `object`.getString("updated_time")
            phone = `object`.getString("phone")
            qq = `object`.getString("qq")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}
