package com.hosigus.askposts.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.hosigus.askposts.item.jsonBean.QuestionDetail
import com.hosigus.askposts.ui.activity.AnswerDetailActivity
import com.hosigus.askposts.ui.activity.LoginActivity
import com.hosigus.askposts.ui.activity.PostAnswerActivity
import com.hosigus.askposts.ui.activity.QuestionDetailActivity

/**
 * Created by 某只机智 on 2018/6/4.
 */

fun startAnswerDetailActivity(context: Context, answerJson: String, detail: QuestionDetail) {
    val intent = Intent(context, AnswerDetailActivity::class.java)
    intent.putExtra("answerJson", answerJson)
    intent.putExtra("kind", detail.kind)
    intent.putExtra("title", detail.title)
    context.startActivity(intent)
}

fun startQuestionDetailActivity(context: Context, id: String) {
    val intent = Intent(context, QuestionDetailActivity::class.java)
    intent.putExtra("questionId", id)
    context.startActivity(intent)
}

const val POST_ANSWER_RESULT = 0x1000
fun startPostAnswerActivity(activity: Activity, qId: String) {
    val intent: Intent = Intent(activity, PostAnswerActivity::class.java)
    intent.putExtra("qId", qId)
    activity.startActivityForResult(intent, POST_ANSWER_RESULT)
}

fun startLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
}