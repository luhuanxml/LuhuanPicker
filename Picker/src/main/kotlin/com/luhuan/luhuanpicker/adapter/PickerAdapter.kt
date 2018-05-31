package com.luhuan.luhuanpicker.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import com.luhuan.luhuanpicker.ItemPhoto
import com.luhuan.luhuanpicker.R
import com.luhuan.luhuanpicker.Tool
import org.jetbrains.annotations.NotNull

class PickerAdapter constructor(mContext: Context)
    : RecyclerView.Adapter<PickerAdapter.Holder>() {

    var inflater: LayoutInflater = LayoutInflater.from(mContext)
    var mList = ArrayList<ItemPhoto>()

    private lateinit var listener:OnListener

    fun setOnListener(@NotNull listener:OnListener){
        this. listener=listener
    }


    fun refresh(list: ArrayList<ItemPhoto>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = inflater.inflate(R.layout.item_photo, parent,false)
        return Holder(itemView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val itemPhoto:ItemPhoto=mList[position]
        Tool.load(itemPhoto.path).into(holder.photo_img)

        holder.photo_check.setOnCheckedChangeListener { _, b ->
            listener.checked(b,itemPhoto)  }
        holder.photo_img.setOnClickListener { listener.click(itemPhoto) }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo_img = itemView.findViewById<ImageView>(R.id.photo_img)
        val photo_check = itemView.findViewById<CheckBox>(R.id.check_photo)
    }

    interface OnListener{
        fun checked(isChecked:Boolean,itemPhoto: ItemPhoto)
        fun click(itemPhoto: ItemPhoto)
    }
}