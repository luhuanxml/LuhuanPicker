package com.luhuan.luhuanpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

@SuppressLint("StaticFieldLeak")
object Tool{
    private var mContext:Context?=null

    private val requestOptions=RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_launcher)
            .error(R.drawable.ic_launcher)
            .priority(Priority.NORMAL)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    private val requestOptionsBig=RequestOptions()
            .centerInside()
            .error(R.drawable.ic_launcher)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    fun init(context: Context){
        mContext=context
    }

    fun load(path:String): RequestBuilder<Drawable> {
        return Glide.with(mContext!!).load(path).apply(requestOptions)
                .transition(DrawableTransitionOptions().crossFade(100))
    }

    fun loadBig(path:String): RequestBuilder<Drawable> {
        return Glide.with(mContext!!).load(path).apply(requestOptionsBig)
                .transition(DrawableTransitionOptions().crossFade(100))
    }
}