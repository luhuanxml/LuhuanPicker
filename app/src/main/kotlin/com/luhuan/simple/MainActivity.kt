package com.luhuan.simple

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.luhuan.luhuanpicker.LuhuanPicker
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivity = this
        btn.setOnClickListener {
            val rxPermissions = RxPermissions(mainActivity)
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it) LuhuanPicker.start(mainActivity)
                    })
        }
    }

    override fun onStart() {
        super.onStart()
        LuhuanPicker.eccept().subscribe({
            for (itemPhoto in it.paths)
                Log.d("AAA===>>>", itemPhoto.path+"--->name:"+itemPhoto.name)
        })
    }
}
