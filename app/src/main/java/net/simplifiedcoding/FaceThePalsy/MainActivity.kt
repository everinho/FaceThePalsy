package net.simplifiedcoding.FaceThePalsy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts.*
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityMainBinding
import net.simplifiedcoding.FaceThePalsy.exercises.ExerciseActivity
import net.simplifiedcoding.FaceThePalsy.exercises.FollowActivity
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

//        val settingsButton = findViewById<Button>(R.id.settings)
//        settingsButton.setOnClickListener {
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//        }

        val scheduleButton = findViewById<ImageButton>(R.id.schedule)
        scheduleButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<ImageButton>(R.id.profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val followButton = findViewById<Button>(R.id.follow)
        followButton.setOnClickListener {
            val intent = Intent(this, FollowActivity::class.java)
            startActivity(intent)
        }

        val helpButton = findViewById<Button>(R.id.help)
        helpButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
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