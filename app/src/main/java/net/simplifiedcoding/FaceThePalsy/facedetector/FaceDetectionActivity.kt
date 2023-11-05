package net.simplifiedcoding.FaceThePalsy.facedetector

import FaceLandmarks
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import net.simplifiedcoding.FaceThePalsy.CameraXViewModel
import net.simplifiedcoding.FaceThePalsy.MainActivity
import net.simplifiedcoding.FaceThePalsy.R
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityFaceDetectionBinding
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceBox
import java.util.concurrent.Executors

class FaceDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceDetectionBinding
    private lateinit var cameraSelector: CameraSelector
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private val cameraXViewModel = viewModels<CameraXViewModel>()
    private val previousAsymmetryResults: MutableList<Float> = mutableListOf()
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var asymmetryText: String = ""
    private var textColor: Int = Color.BLACK // Domyślny kolor tekstu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        val calculateAsymmetryButton = findViewById<Button>(R.id.calculateAsymmetryButton)


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
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    private fun bindInputAnalyser() {
        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .build()
        )
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        val cameraExecutor = Executors.newSingleThreadExecutor()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            processImageProxy(detector, imageProxy)
        }

        try {
            processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
        } catch (illegalStateException: IllegalStateException) {
            Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(detector: FaceDetector, imageProxy: ImageProxy) {
        val calculateAsymmetryButton = findViewById<Button>(R.id.calculateAsymmetryButton)
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        detector.process(inputImage).addOnSuccessListener { faces ->
            binding.graphicOverlay.clear()
            faces.forEach { face ->
                val faceBox = FaceBox(binding.graphicOverlay, face, imageProxy.image!!.cropRect)
                //val landmarks = FaceLandmarks(binding.graphicOverlay, face, imageProxy.image!!.cropRect)
                calculateAsymmetryButton.setOnClickListener {
                    if (startTime == 0L) {
                        // Rozpocznij pomiar czasu, jeśli to pierwszy raz
                        startTime = System.currentTimeMillis()
                        val asymmetry = faceBox.calculateAsymmetry()
                        previousAsymmetryResults.add(asymmetry)
                    } else {
                        // Jeśli upłynęły co najmniej 2 sekundy, oblicz średnią asymetrię
                        endTime = System.currentTimeMillis()
                        if (endTime - startTime >= 2000) {
                            val averageAsymmetry = calculateAverageAsymmetry()
                            faceBox.setAsymmetry(averageAsymmetry) // Ustaw asymetrię w obiekcie FaceBox
//                            asymmetryText = when {
//                                averageAsymmetry < 1.15 -> "Brak asymetrii"
//                                averageAsymmetry < 1.6 -> "Niewielka asymetria"
//                                else -> "Duża asymetria"
//                            }
//                            textColor = when {
//                                averageAsymmetry < 1.15 -> Color.GREEN
//                                averageAsymmetry < 1.6 -> Color.YELLOW
//                                else -> Color.RED
//                            }
                            asymmetryText = "Asymetria: $averageAsymmetry"
                        }
                    }
                }

                binding.graphicOverlay.add(faceBox)
                //binding.graphicOverlay.add(landmarks)
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }.addOnCompleteListener {
            imageProxy.close()
        }
    }

    private fun calculateAverageAsymmetry(): Float {
        if (previousAsymmetryResults.isEmpty()) {
            return 0.0f
        }
        val sum = previousAsymmetryResults.sum()
        return sum / previousAsymmetryResults.size
    }

    companion object {
        private val TAG = FaceDetectionActivity::class.simpleName
        fun startActivity(context: Context) {
            Intent(context, FaceDetectionActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}