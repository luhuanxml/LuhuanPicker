package com.luhuan.simple

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.luhuan.luhuanpicker.LuhuanPicker
import com.luhuan.luzxing.CaptureActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivity = this
        to_rq_bitmap.setOnClickListener {
            intent = Intent(this@MainActivity, ZxingActivity::class.java)
            startActivity(Intent(mainActivity, ZxingActivity::class.java))
        }
        to_rq_code.setOnClickListener {
            intent = Intent(this@MainActivity, CaptureActivity::class.java)
            startActivity(Intent(mainActivity, CaptureActivity::class.java))
        }
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
        val params = HashMap<String, String>()
        LuhuanPicker.eccept().subscribe({
            for (itemPhoto in it.paths)
                Log.d("AAA===>>>", itemPhoto.path + "--->name:" + itemPhoto.name)
        })
    }
}
