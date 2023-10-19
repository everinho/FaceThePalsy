package net.simplifiedcoding.FaceThePalsy.exercises

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import net.simplifiedcoding.FaceThePalsy.CameraXViewModel
import net.simplifiedcoding.FaceThePalsy.MainActivity
import net.simplifiedcoding.FaceThePalsy.R
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityFollowBinding
import java.util.concurrent.Executors

class FollowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowBinding
    private lateinit var cameraSelector: CameraSelector
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private val cameraXViewModel = viewModels<CameraXViewModel>()

    private val exercises = listOf(
        ExerciseModel(
            "Ćwiczenie 1",
            "Unoszenie czoła (jedna strona) - 10 powtórzeń"
        ),
        ExerciseModel(
            "Ćwiczenie 2",
            "Unoszenie czoła (druga strona) - 10 powtórzeń"
        ),
        ExerciseModel(
            "Ćwiczenie 3",
            "Uśmiech - 10 powtórzeń"
        ),
        ExerciseModel(
            "Ćwiczenie 4",
            "Marszczenie czoła (jedna strona) - 10 powtórzeń"
        ),
        ExerciseModel(
            "Ćwiczenie 5",
            "Marszczenie czoła (druga strona) - 10 powtórzeń"
        )
    )

    private var currentExerciseIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val prevButton = findViewById<Button>(R.id.prevButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        prevButton.setOnClickListener {
            if (currentExerciseIndex > 0) {
                currentExerciseIndex--
                updateExerciseView()
                updateButtonStates()
            }
        }

        nextButton.setOnClickListener {
            if (currentExerciseIndex < exercises.size - 1) {
                currentExerciseIndex++
                updateExerciseView()
                updateButtonStates()
            } else {
                showTrainingCompletedDialog()
            }
        }

        cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
        cameraXViewModel.value.processCameraProvider.observe(this) { provider ->
            processCameraProvider = provider
            bindCameraPreview()
            bindInputAnalyser()
        }
    }

    private fun bindCameraPreview() {
        cameraPreview = Preview.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()
        cameraPreview.setSurfaceProvider(binding.previewView.surfaceProvider)
        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview)
        } catch (illegalStateException: IllegalStateException) {
            Log.e(ContentValues.TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(ContentValues.TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    private fun bindInputAnalyser() {
        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .build()
        )
        val classificator = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .build()
        )
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        val cameraExecutor = Executors.newSingleThreadExecutor()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            processImageProxy(detector,classificator, imageProxy)
        }

        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
        } catch (illegalStateException: IllegalStateException) {
            Log.e(ContentValues.TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(ContentValues.TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(detector: FaceDetector,classificator: FaceDetector, imageProxy: ImageProxy) {
        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        val currentExercise = exercises[currentExerciseIndex]

        if(currentExerciseIndex==2)
        {
            classificator.process(inputImage).addOnSuccessListener { faces ->
                binding.graphicOverlay.clear()
                faces.forEach { face ->
                    val faceBox = FollowBox(binding.graphicOverlay, face, imageProxy.image!!.cropRect, currentExerciseIndex)
                    binding.graphicOverlay.add(faceBox)
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
        else{
            detector.process(inputImage).addOnSuccessListener { faces ->
                binding.graphicOverlay.clear()
                faces.forEach { face ->
                    val faceBox = FollowBox(binding.graphicOverlay, face, imageProxy.image!!.cropRect, currentExerciseIndex)
                    binding.graphicOverlay.add(faceBox)
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }

    private fun updateExerciseView() {
        val exercise = exercises[currentExerciseIndex]
        binding.graphicOverlay.clear()
    }

    private fun updateButtonStates() {
        val prevButton = findViewById<Button>(R.id.prevButton)
        val nextButton = findViewById<Button>(R.id.nextButton)

        prevButton.isEnabled = currentExerciseIndex > 0
        nextButton.isEnabled = currentExerciseIndex <= exercises.size - 1
    }

    private fun showTrainingCompletedDialog() {
        FollowBox.left_repeats = 0
        FollowBox.right_repeats = 0
        FollowBox.smile_repeats = 0
        FollowBox.isExercising_left = false
        FollowBox.isExercising_right = false
        FollowBox.isSmiling = false

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Zestaw treningowy ukończony")
        alertDialogBuilder.setMessage("Gratulacje! Ukończyłeś zestaw treningowy.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Przenieś użytkownika do głównej aktywności
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        private val TAG = FollowActivity::class.simpleName
        fun startActivity(context: Context) {
            Intent(context, FollowActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}

data class ExerciseModel(val name: String, val exerciseDescription: String)
