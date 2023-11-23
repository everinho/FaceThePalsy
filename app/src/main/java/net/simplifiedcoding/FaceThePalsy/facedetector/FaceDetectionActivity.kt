package net.simplifiedcoding.FaceThePalsy.facedetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import net.simplifiedcoding.FaceThePalsy.CameraXViewModel
import net.simplifiedcoding.FaceThePalsy.MainActivity
import net.simplifiedcoding.FaceThePalsy.R
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityFaceDetectionBinding
import net.simplifiedcoding.FaceThePalsy.exercises.FollowActivity
import net.simplifiedcoding.FaceThePalsy.exercises.FollowBox
import java.util.concurrent.Executors

class FaceDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceDetectionBinding
    private lateinit var cameraSelector: CameraSelector
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var progressBar: ProgressBar
    private lateinit var alertDialog: AlertDialog
    private var isDialogShown = false



    private val cameraXViewModel = viewModels<CameraXViewModel>()

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

        val resetButton = findViewById<Button>(R.id.reset)
        resetButton.setOnClickListener {
            FaceBox.usrednianie = 0
            //val distance_A = distance(point8,point9) //useless
            FaceBox.distance_Bl = 0F
            FaceBox.distance_Br = 0F
            //val distance_C = distance(point10,point7) //useless
            FaceBox.distance_D = 0F
            FaceBox.distance_E = 0F
            FaceBox.distance_F = 0F
            FaceBox.distance_G = 0F
            FaceBox.distance_H = 0F
            FaceBox.distance_I = 0F
            FaceBox.distance_J = 0F
            FaceBox.distance_K = 0F
            FaceBox.distance_R = 0F
            FaceBox.distance_S = 0F
            //FaceBox.distance_X = distance(point21,point22) //useless
            FaceBox.distance_T = 0F
            FaceBox.distance_U = 0F
            FaceBox.distance_Vl = 0F
            FaceBox.distance_Vr = 0F
            //FaceBox.distance_W = distance(point23,point24) //useless
            FaceBox.distance_Nl = 0F
            FaceBox.distance_Nr = 0F
            FaceBox.distance_Ol = 0F
            FaceBox.distance_Or = 0F
            FaceBox.distance_Pi = 0F
            FaceBox.distance_Pu = 0F
            FaceBox.distance_Qi = 0F
            FaceBox.distance_Qu = 0F
            FaceBox.calculated = false
            FaceBox.asymmetry = 0f
            isDialogShown = false
            FaceBox.saved = false
        }

        cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
        cameraXViewModel.value.processCameraProvider.observe(this) { provider ->
            processCameraProvider = provider
            bindCameraPreview()
            bindInputAnalyser()
        }

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Przyjmij odpowiednią mimikę i wykadruj twarz.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                isDialogShown = true
                startFaceDetection()
            }

        alertDialog = dialogBuilder.create()

        // Wywołanie dialogu po otwarciu aktywności
        alertDialog.show()
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

    private fun startFaceDetection() {
        if (isDialogShown) {
            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
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
                processImageProxy(detector, imageProxy, progressBar)
            }

            try {
                processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis)
            } catch (illegalStateException: IllegalStateException) {
                Log.e(TAG, illegalStateException.message ?: "IllegalStateException")
            } catch (illegalArgumentException: IllegalArgumentException) {
                Log.e(TAG, illegalArgumentException.message ?: "IllegalArgumentException")
            }
        }
    }

    private fun bindInputAnalyser() {
        startFaceDetection()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(detector: FaceDetector, imageProxy: ImageProxy, progressBar: ProgressBar) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        detector.process(inputImage).addOnSuccessListener { faces ->
            binding.graphicOverlay.clear()
            faces.forEach { face ->
                val faceBox = FaceBox(binding.graphicOverlay, this,face, imageProxy.image!!.cropRect)
                //val landmarks = FaceLandmarks(binding.graphicOverlay, face, imageProxy.image!!.cropRect)
                binding.graphicOverlay.add(faceBox)
                //binding.graphicOverlay.add(landmarks)
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }.addOnCompleteListener {
            imageProxy.close()
        }
        progressBar.progress = ((FaceBox.usrednianie.toFloat()/ 30)*100).toInt()
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