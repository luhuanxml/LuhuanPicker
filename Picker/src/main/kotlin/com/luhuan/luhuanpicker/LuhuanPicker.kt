package com.luhuan.luhuanpicker

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import com.luhuan.luhuanpicker.activity.PickerActivity
import io.reactivex.Flowable

object LuhuanPicker{

    fun init(context:Context){
        Tool.init(context)
    }

    fun start(activity:Activity){
        activity.startActivity(Intent(activity,PickerActivity::class.java))
    }

    fun start(fragment: Fragment){
        fragment.startActivity(Intent(fragment.activity,PickerActivity::class.java))
    }

    fun eccept():Flowable<Photos>{
       return RxFus.except().map { it.photos }
    }

}
