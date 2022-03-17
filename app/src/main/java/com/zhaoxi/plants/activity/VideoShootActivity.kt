package com.zhaoxi.plants.activity

import com.zhaoxi.plants.view.CameraPreview
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.util.Log
import android.view.OrientationEventListener
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.zhaoxi.plants.R
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class VideoShootActivity: AppCompatActivity() {
    private val REQUEST_PERMISSIONS = 200
    private var previewWindow: CameraPreview? = null
    private var myOrientation: Int = 90
    private var orientationEventListener: OrientationEventListener? = null
    private var shootBtn: Button? = null
    private var mediaRecorder: MediaRecorder? = null
    private var camera: Camera? = null
    private var isRecording = false
    private var videoPath: String? = null
    private var plantName: String? = null
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_PERMISSIONS) {
            if (checkCameraHardware(this)){
                camera = getCameraInstance()
                camera!!.setDisplayOrientation(90)
            }
            previewWindow = camera?.let {
                CameraPreview(this,it)
            }
            previewWindow?.also {
                val preview:FrameLayout = findViewById(R.id.preview_window)
                preview.addView(it)
            }
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoot_video)
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener{
            this.finish()
        }
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS)
        plantName = intent.getStringExtra("plant_name")
        shootBtn = findViewById(R.id.shoot_video_btn)
        shootBtn?.setOnClickListener{
            if(isRecording){
                mediaRecorder?.stop()
                releaseMediaRecorder()
                camera?.lock()
                this.setResult(RESULT_OK)
                shootBtn?.text = "录制"
                isRecording = false
            }else{
                if(prepareVideoRecorder()){
                    mediaRecorder?.start()
                    shootBtn?.text = "结束录制"
                    isRecording = true
                }else{
                    releaseMediaRecorder()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        orientationEventListener = object: OrientationEventListener(this){
            override fun onOrientationChanged(orientation: Int) {
                val temp: Int = myOrientation
                if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN) return
                if(orientation > 350 || orientation < 10){
                    myOrientation = 90
                }else if(orientation in 81..99){
                    myOrientation = 180
                }else if(orientation in 171..189){
                    myOrientation = 270
                }else if(orientation in 261..279){
                    myOrientation = 0
                }
                if(temp != myOrientation)
                    shootBtn?.rotation = -(myOrientation - 90).toFloat()
            }
        }

        if((orientationEventListener as OrientationEventListener).canDetectOrientation()){
            (orientationEventListener as OrientationEventListener).enable()
        }else{
            (orientationEventListener as OrientationEventListener).disable()
        }
    }

    override fun onPause() {
        super.onPause()
        releaseMediaRecorder() // if you are using MediaRecorder, release it first
        releaseCamera() // release the camera immediately on pause event
        Log.d("Zhaoxi","pause")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("Zhaoxi", "change")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Zhaoxi","stop")
    }

    private fun prepareVideoRecorder():Boolean{
        camera!!.unlock()
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setCamera(camera)
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
        mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.CAMERA)
        mediaRecorder!!.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))
        videoPath = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString()
        mediaRecorder!!.setOutputFile(videoPath)
        mediaRecorder!!.setPreviewDisplay(previewWindow!!.holder.surface)
        mediaRecorder!!.setOrientationHint(myOrientation)
        return try {
            mediaRecorder!!.prepare()
            true
        } catch (e: IllegalStateException) {
            Log.d("ZHAOXI", "IllegalStateException preparing MediaRecorder: ${e.message}")
            releaseMediaRecorder()
            false
        } catch (e: IOException) {
            Log.d("ZHAOXI", "IOException preparing MediaRecorder: ${e.message}")
            releaseMediaRecorder()
            false
        }
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    /** Create a file Uri for saving an image or video */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        val mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)


        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir!!.path}${File.separator}IMG_$timeStamp.jpg")
            }
            MEDIA_TYPE_VIDEO -> {
                File("${mediaStorageDir!!.path}${File.separator}VID_$plantName.mp4")
            }
            else -> null
        }
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.reset() // clear recorder configuration
        mediaRecorder?.release() // release the recorder object
        mediaRecorder = null
        camera?.lock() // lock camera for later use
    }

    private fun releaseCamera() {
        camera?.release() // release the camera for other applications
        camera = null
    }
}