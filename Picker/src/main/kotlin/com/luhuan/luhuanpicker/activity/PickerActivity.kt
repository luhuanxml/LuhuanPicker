package com.luhuan.luhuanpicker.activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.luhuan.luhuanpicker.ItemPhoto
import com.luhuan.luhuanpicker.Photos
import com.luhuan.luhuanpicker.R
import com.luhuan.luhuanpicker.RxFus
import com.luhuan.luhuanpicker.adapter.PickerAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_picker.*


class PickerActivity : AppCompatActivity() {

    private lateinit var pickerAdapter: PickerAdapter

    private var photoList = ArrayList<ItemPhoto>()

    private var checkedList = ArrayList<ItemPhoto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        pickerRecycler.setHasFixedSize(true)
        pickerRecycler.layoutManager = GridLayoutManager(this, 4)
        pickerAdapter = PickerAdapter(this)
        pickerRecycler.adapter = pickerAdapter
        setListener()
        getAllPhotos().subscribe({
            photoList = it
            pickerAdapter.refresh(photoList)
        })
    }

    private fun setListener() {
        pickerAdapter.setOnListener(object : PickerAdapter.OnListener {
            override fun checked(isChecked: Boolean, itemPhoto: ItemPhoto) {
                if (isChecked) {
                    checkedList.add(itemPhoto)
                } else {
                    checkedList.remove(itemPhoto)
                }
            }

            override fun click(itemPhoto: ItemPhoto) {
                startActivity(Intent(applicationContext, PhotoActivity::class.java)
                        .putExtra("photo", itemPhoto.path))
            }

        })

        toolbar.setNavigationOnClickListener {
            finish()
        }
        cancel.setOnClickListener {
        }
        complete_btn.setOnClickListener {
            val photos = Photos(checkedList)
            RxFus.post(11111, photos)
            finish()
        }

    }

    private fun getAllPhotos(): Observable<ArrayList<ItemPhoto>> {
        return Observable.create<ArrayList<ItemPhoto>> {
            val contentResolver = contentResolver
            val columns: Array<String> = arrayOf(
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT)
            val cursor: Cursor = contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns,
                    null,
                    null,
                    MediaStore.Images.Media.DATE_MODIFIED)
            val photoList: ArrayList<ItemPhoto> = ArrayList()
            while (cursor.moveToNext()) {
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                val name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                photoList.add(ItemPhoto(path, name))
            }
            if (!cursor.isClosed)
                cursor.close()
            it.onNext(photoList)
        }
    }

}
