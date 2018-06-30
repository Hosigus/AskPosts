package com.hosigus.askposts.ui.view

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * Created by 某只机智 on 2018/6/4.
 */
class CircleImageView : AppCompatImageView {

    private var paint: Paint = Paint()
    private var pfdf: PaintFlagsDrawFilter
    private var path: Path? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.isAntiAlias = true
        pfdf = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (path == null) {
            path = Path()
            path!!.addCircle(width / 2f, height / 2f, Math.min(width / 2f, height / 2f), Path.Direction.CCW)
            path!!.close()
        }
        val saveCount = canvas.save()
        canvas.drawFilter = pfdf
        canvas.clipPath(path, Region.Op.INTERSECT)
        super.onDraw(canvas)
        canvas.restoreToCount(saveCount)
    }
}