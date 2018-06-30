package com.hosigus.askposts.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by 某只机智 on 2018/6/3.
 */
abstract class BaseFragment : Fragment() {
    abstract fun getResID(): Int
    abstract fun init(v: View)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(getResID(), container, false)
        init(v)
        return v
    }
}