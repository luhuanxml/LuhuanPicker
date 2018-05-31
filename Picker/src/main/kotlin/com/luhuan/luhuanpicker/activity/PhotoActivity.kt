package com.luhuan.luhuanpicker.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.luhuan.luhuanpicker.R
import com.luhuan.luhuanpicker.Tool
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {

    private lateinit var path: String
    private lateinit var photoActivity: PhotoActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        photoActivity = this
        photo_detail.setOnClickListener {
            val intent = Intent(photoActivity,CropActivity::class.java)
            intent.putExtra("PHOTO",path)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        path = intent.getStringExtra("photo")
        Tool.loadBig(path).into(photo_detail)
    }
}
