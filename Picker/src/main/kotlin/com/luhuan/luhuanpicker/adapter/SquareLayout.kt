package com.luhuan.luhuanpicker.adapter

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

class SquareLayout : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}