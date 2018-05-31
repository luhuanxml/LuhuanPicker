package com.luhuan.simple

import android.support.multidex.MultiDexApplication
import com.luhuan.luhuanpicker.LuhuanPicker


class TheApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        LuhuanPicker.init(this)
    }
}