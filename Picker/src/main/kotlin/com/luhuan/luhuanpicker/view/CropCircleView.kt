package com.luhuan.luhuanpicker.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class CropCircleView : View {

    private var cropWidth: Float = 0.0f
    private var cropHeight: Float = 0.0f
    private var mWidth = 0.0f
    private var mHeight = 0.0f
    private var cropStrokeWidth=0.0f //裁剪框的宽度
    private lateinit var xfermode: PorterDuffXfermode

    private lateinit var globalPath: Path //整个view的蒙版路径
    private lateinit var cropPath: Path //整个裁剪路径

    private lateinit var mPaint:Paint

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        globalPath = Path()
        cropPath = Path()
        cropStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2f, context.resources.displayMetrics)
        //关闭硬件加速，不然部分机型path的绘制会无效
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        val length = if (w > h) h else w
        cropWidth = (4 * length / 5).toFloat()
        cropHeight = (4 * length / 5).toFloat()
        globalPath.addRect(0f,0f,w.toFloat(),h.toFloat(),Path.Direction.CW)
        cropPath.addCircle(w/2f,h/2f,cropWidth/2f,Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制蒙版
        //canvas.translate(mWidth,mHeight)
        mPaint.color=Color.BLACK
        mPaint.alpha=255/3*2
        mPaint.style=Paint.Style.FILL
        canvas.drawPath(globalPath,mPaint)

        //擦掉裁剪框内的阴影
        mPaint.xfermode = xfermode //设置为擦除模式
        canvas.drawPath(cropPath,mPaint)
        mPaint.xfermode=null//使用完之后清除模式

        mPaint.color=Color.WHITE
        mPaint.alpha=255
        mPaint.strokeWidth=cropStrokeWidth
        mPaint.style=Paint.Style.STROKE
        canvas.drawPath(cropPath,mPaint)

    }
}
