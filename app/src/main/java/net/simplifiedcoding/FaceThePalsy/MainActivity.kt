package net.simplifiedcoding.FaceThePalsy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts.*
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityMainBinding
import net.simplifiedcoding.FaceThePalsy.exercises.ExerciseActivity
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceDetectionActivity

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
            this.action = Action.FACE_DETECTION
            requestCameraAndStart()
        }

        val exercisesButton = findViewById<Button>(R.id.exercises)
        exercisesButton.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<Button>(R.id.settings)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
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
        finish()
    }

}