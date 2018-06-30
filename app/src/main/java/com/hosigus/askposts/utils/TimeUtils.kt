package com.hosigus.askposts.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 某只机智 on 2018/5/30.
 */
private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

fun getDateDifferenceDescribe(yourTime:String):String {
    try {
        var d: Long = dateFormat.parse(yourTime).time - Calendar.getInstance().time.time
        d /= 1000
        val day: Int
        val hour: Int
        day = d.toInt() / (24 * 3600)
        hour = d.toInt() % (24 * 3600) / 3600
        val re = (if (day > 0) day.toString() + "天" else "") + if (hour > 0) hour.toString() + "小时" else ""
        return if (re.isEmpty()) "已过时" else re + "后消失"
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}
