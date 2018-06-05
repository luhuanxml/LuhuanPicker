package com.luhuan.luzxing

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.hardware.Camera
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Menu
import android.view.SurfaceHolder
import android.view.SurfaceHolder.Callback
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.luhuan.luzxing.camera.CameraManager
import com.luhuan.luzxing.decoding.CaptureActivityHandler
import com.luhuan.luzxing.decoding.InactivityTimer
import com.luhuan.luzxing.view.ViewfinderView
import kotlinx.android.synthetic.main.activity_capture.*
import java.io.IOException
import java.util.*

/**
 * Initial the camera
 * 二维码扫描界面
 * @author Ryan.Tang
 */
class CaptureActivity : AppCompatActivity(), Callback {

    companion object {
        private const val BEEP_VOLUME = 0.10f
        private const val VIBRATE_DURATION = 200L
    }

    private var isOpen = false

    private var handler: CaptureActivityHandler? = null
    private var hasSurface: Boolean = false
    private var decodeFormats: Vector<BarcodeFormat>? = null
    private var characterSet: String? = null
    private lateinit var inactivityTimer: InactivityTimer
    private var mediaPlayer: MediaPlayer? = null
    private var playBeep: Boolean = false
    private var vibrate: Boolean = false

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private val beepListener = OnCompletionListener { mediaPlayer -> mediaPlayer.seekTo(0) }

    /**
     * Called when the activity is first created.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_capture)
        CameraManager.init(application)
        hasSurface = false
        inactivityTimer = InactivityTimer(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onStart() {
        super.onStart()
        toolbar.setNavigationOnClickListener { finish() }
        open_light.setOnClickListener { clickLight() }
    }


    //打开\关闭手电筒  额外功能，与zxing无关
    private fun clickLight() {
        val camera: Camera = CameraManager.get().camera
        val params: Camera.Parameters = camera.parameters
        when (isOpen) {
            true -> {
                params.flashMode = Camera.Parameters.FLASH_MODE_OFF
                camera.parameters = params
                isOpen = false
            }
            false -> {
                params.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                camera.parameters = params
                isOpen = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val surfaceHolder: SurfaceHolder = preview_view.holder
        if (hasSurface) {
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(this)
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
        decodeFormats = null
        characterSet = null

        playBeep = true
        val audioService: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioService.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false
        }
        initBeepSound()
        vibrate = true
    }

    fun getViewfinderView(): ViewfinderView {
        return viewfinderView
    }

    override fun onPause() {
        super.onPause()
        if (handler != null) {
            handler!!.quitSynchronously()
            handler = null
        }
        CameraManager.get().closeDriver()
    }

    override fun onDestroy() {
        inactivityTimer.shutdown()
        super.onDestroy()
    }

    /**
     * Handler scan result
     * 二维码扫描成功回调
     * @param result
     * @param barcode
     */
    fun handleDecode(result: Result, barcode: Bitmap) {
        inactivityTimer.onActivity()
        playBeepSoundAndVibrate()
        val resultString = result.text
        if (TextUtils.isEmpty(resultString) || "我是充电桩" != resultString) {
            Toast.makeText(this, "请确认是否是充电桩二维码", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder)
        } catch (ioe: IOException) {
            return
        } catch (ioe: RuntimeException) {
            return
        }

        if (handler == null) {
            handler = CaptureActivityHandler(this, decodeFormats, characterSet)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder)
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        hasSurface = false

    }

    fun getHandler(): Handler? {
        return handler
    }

    fun drawViewfinder() {
        viewfinderView.drawViewfinder()

    }

    private fun initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setOnCompletionListener(beepListener)

            val file: AssetFileDescriptor = resources.openRawResourceFd(R.raw.beep)
            try {
                mediaPlayer!!.setDataSource(file.fileDescriptor,
                        file.startOffset, file.length)
                file.close()
                mediaPlayer!!.setVolume(BEEP_VOLUME, BEEP_VOLUME)
                mediaPlayer!!.prepare()
            } catch (e: IOException) {
                mediaPlayer = null
            }

        }
    }

    private fun playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer!!.start()
        }
        if (vibrate) {
            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VIBRATE_DURATION)
        }
    }
}