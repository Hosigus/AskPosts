package com.hosigus.askposts.item.jsonBean

import com.hosigus.tools.items.JSONBean
import org.json.JSONObject


/**
 * Created by Hosigus on 2018/5/30.
 * "status": 200,
 * "info": "success",
 * "data": null
 */
class BaseBean : JSONBean() {
    var status:Int = 0
        private set
    lateinit var info: String
        private set
    var data: String? = null
        private set

    override fun parser(json: JSONObject) {
        status = json.getInt("status")
        info = json.getString("info")
        data = if(isStatusOK()&&json.has("data")) json.getString("data") else null
    }
    
    fun isStatusOK() = status == 200
    fun isGoodJson() = isStatusOK() && !data.isNullOrBlank()

}