package com.luhuan.luhuanpicker

import java.util.ArrayList

class BusBean(var type: Int, var photos: Photos)

class Photos(val paths:ArrayList<ItemPhoto>)

class ItemPhoto(val path:String,val name:String)
