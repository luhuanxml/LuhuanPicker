package com.luhuan.luhuanpicker.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView

class TouchCircleView : ImageView {

    private var mWidth: Float = 0.0f
    private var mHeight: Float = 0.0f

    private var startDistance = 0.0f

    private var mode = 0

    private val DRAG = 1
    private val ZOOM = 2

    private val currentMatrix = Matrix()
    private val startPointF = PointF()

    private lateinit var middlePointF: PointF

    private val mMatrix = Matrix()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        adjustViewBounds = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    @SuppressLint(value = ["ClickableViewAccessibility"])
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleType = ScaleType.MATRIX
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                               mode = DRAG
                currentMatrix.set(imageMatrix)
                startPointF.set(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG) {
                    val dx:Float = event.x - startPointF.x
                    val dy:Float = event.y - startPointF.y
                    mMatrix.set(currentMatrix)
                    mMatrix.postTranslate(dx, dy)
                } else if (mode == ZOOM) {
                    val endDistance: Float = distance(event)//结束距离
                    if (endDistance > 10f) {
                        val scale = endDistance / startDistance//放大倍数
                        mMatrix.set(currentMatrix)
                        mMatrix.postScale(scale, scale, middlePointF.x, middlePointF.y)
                    }
                }
            }
            MotionEvent.ACTION_UP -> mode = 0
            MotionEvent.ACTION_POINTER_UP -> mode = 0 //多根手指的情况，有手指离开屏幕，但是屏幕上还有手指触点
            MotionEvent.ACTION_POINTER_DOWN -> { //屏幕上已经有手指压在上面了，又添加了手指 这个时候变成缩放模式
                mode = ZOOM
                startDistance = distance(event) //获得起始两点之间的距离
                if (startDistance > 10) { //两个触点距离大于10是为了防止一个手指上有两个触点
                    middlePointF = middle(event)
                    currentMatrix.set(imageMatrix)
                }
            }
        }
        imageMatrix = mMatrix
        return true
    }

    private fun middle(event: MotionEvent): PointF {
        val midX: Float = (event.getX(1) + event.getX(0)) / 2
        val midY: Float = (event.getY(1) + event.getY(0)) / 2
        return PointF(midX, midY)
    }


    /**
     * 两点之间的距离
     */
    private fun distance(event: MotionEvent): Float {
        //两根手指间的距离
        val dxPow: Double = Math.pow(event.getX(1) - event.getX(0).toDouble(), 2.0)
        val dyPow: Double = Math.pow((event.getY(1) - event.getY(0)).toDouble(), 2.0)
        return Math.sqrt(dxPow + dyPow).toFloat()
    }

}
