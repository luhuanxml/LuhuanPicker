package com.luhuan.simple

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.luhuan.luzxing.encoding.EncodingHandler
import io.reactivex.annotations.NonNull
import kotlinx.android.synthetic.main.activity_zxing.*

class ZxingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing)
    }

    override fun onStart() {
        super.onStart()
        val b=EncodingHandler.createQRCode("你好我是二维码",320,320,
                BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
        rq_code.setImageBitmap(b)
       // rq_code.setImageBitmap(bitMatrixToBitmap(encode("你好我是二维码")))
    }

    private fun encode(@NonNull contents:String):BitMatrix{
        val pair1=Pair(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H)
        val pair2=Pair(EncodeHintType.CHARACTER_SET,"utf-8")
        val hints:Map<EncodeHintType,Any> = mapOf(pair1,pair2)
        return QRCodeWriter().encode(contents,BarcodeFormat.QR_CODE,320,320,hints)
    }

    private fun bitMatrixToBitmap(bitMatrix:BitMatrix ):Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height

         val pixels = IntArray(width*height)
        for (y:Int in 0 until height){
            for (x:Int in 0 until width){
                pixels[y*width+x]=(if(bitMatrix[x,y]) 0xFF000000 else 0xFFFFFFFF).toInt()
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}
