package com.hosigus.askposts.utils

import android.content.Context
import android.view.View
import com.hosigus.askposts.R
import com.hosigus.askposts.ui.view.CircleImageView
import com.hosigus.imageloader.ImageLoader

/**
 * Created by 某只机智 on 2018/6/4.
 */
fun setSex(sexV: View, sex: String, kind: String) {
    if (kind == "情感") {
        setSex(sexV, sex)
    }
}

fun setSex(sexV: View, sex: String) {
    if (sex == "男") {
        sexV.visibility = View.VISIBLE
        sexV.setBackgroundResource(R.drawable.ic_sex_boy)
    }else if (sex == "女") {
        sexV.visibility = View.VISIBLE
        sexV.setBackgroundResource(R.drawable.ic_sex_girl)
    }
}

fun setHeadImage(c:Context,v: CircleImageView, url: String) {
    if (!url.isEmpty()) {
        ImageLoader.with(c)
                .place(R.drawable.ic_default_head)
                .error(R.drawable.ic_default_head)
                .load(url.replace("http://", "https://"))
                .into(v)
    }
}