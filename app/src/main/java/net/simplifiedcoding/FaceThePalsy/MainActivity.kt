package net.simplifiedcoding.FaceThePalsy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.*
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityMainBinding
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceDetectionActivity
import net.simplifiedcoding.FaceThePalsy.facemeshdetector.FaceMeshDetectionActivity

class MainActivity : AppCompatActivity() {

    private val cameraPermission = android.Manifest.permission.CAMERA
    private lateinit var binding: ActivityMainBinding
    private var action = Action.FACE_DETECTION

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonFaceDetect.setOnClickListener {
            //this.action = Action.FACE_DETECTION
            this.action = Action.FACE_MESH_DETECTION
            requestCameraAndStart()
        }
    }

    private fun requestCameraAndStart() {
        if (isPermissionGranted(cameraPermission)) {
            startCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun startCamera() {
        FaceDetectionActivity.startActivity(this)
        //FaceMeshDetectionActivity.startActivity(this)
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                cameraPermissionRequest(
                    positive = { openPermissionSetting() }
                )
            }
            else -> {
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }
    fun onCloseButtonClick(view: View) {
        finish() // Zamknij aktywność
    }

}