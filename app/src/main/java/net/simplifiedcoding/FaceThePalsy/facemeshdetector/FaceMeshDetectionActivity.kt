package net.simplifiedcoding.FaceThePalsy.facemeshdetector

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.Triangle
import com.google.mlkit.vision.facemesh.FaceMeshDetection
import com.google.mlkit.vision.facemesh.FaceMeshDetector
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions
import com.google.mlkit.vision.facemesh.FaceMeshPoint
import net.simplifiedcoding.FaceThePalsy.CameraXViewModel
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityFaceDetectionBinding
import java.util.concurrent.Executors

class FaceMeshDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceDetectionBinding
    private lateinit var cameraSelector: CameraSelector
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraPreview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private val cameraXViewModel = viewModels<CameraXViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFaceDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
//        val detector = FaceMeshDetection.getClient()

        val detector = FaceMeshDetection.getClient(
            FaceMeshDetectorOptions.Builder()
                .setUseCase(FaceMeshDetectorOptions.FACE_MESH)
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

//    @SuppressLint("UnsafeOptInUsageError")
//    private fun processImageProxy(detector: FaceMeshDetector, imageProxy: ImageProxy) {
//        val inputImage =
//            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
//        detector.process(inputImage).addOnSuccessListener { faces ->
//            faces.forEach { face ->
//
//            }
//        }.addOnFailureListener {
//            it.printStackTrace()
//        }.addOnCompleteListener {
//            imageProxy.close()
//        }
//    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(detector: FaceMeshDetector, imageProxy: ImageProxy) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        detector.process(inputImage).addOnSuccessListener { result ->
            for (faceMesh in result) {
                // Tutaj możesz wykorzystać informacje o twarzach
                val bounds: Rect = faceMesh.boundingBox
                val faceMeshpoints = faceMesh.allPoints
                for (faceMeshpoint in faceMeshpoints) {
                    val index: Int = faceMeshpoint.index
                    val position = faceMeshpoint.position
                    // Tutaj możesz wykorzystać punkty siatki twarzy
                }
                val triangles: List<Triangle<FaceMeshPoint>> = faceMesh.allTriangles
                for (triangle in triangles) {
                    val connectedPoints = triangle.allPoints
                    // Tutaj możesz wykorzystać informacje o trójkątach
                }
            }
            imageProxy.close()
        }.addOnFailureListener {
            it.printStackTrace()
            imageProxy.close()
        }
    }


    companion object {
        private val TAG = FaceMeshDetectionActivity::class.simpleName
        fun startActivity(context: Context) {
            Intent(context, FaceMeshDetectionActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}