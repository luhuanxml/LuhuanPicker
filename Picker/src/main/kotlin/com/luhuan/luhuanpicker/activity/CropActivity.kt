package com.luhuan.luhuanpicker.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.luhuan.luhuanpicker.R
import com.luhuan.luhuanpicker.Tool
import kotlinx.android.synthetic.main.activity_crop.*

class CropActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        val path =intent.getStringExtra("PHOTO")
        Tool.loadBig(path).into(touchImageView)
    }
}
