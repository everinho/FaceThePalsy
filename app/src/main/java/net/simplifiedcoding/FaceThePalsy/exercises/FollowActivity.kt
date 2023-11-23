package net.simplifiedcoding.FaceThePalsy.exercises

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import net.simplifiedcoding.FaceThePalsy.CameraXViewModel
import net.simplifiedcoding.FaceThePalsy.MainActivity
import net.simplifiedcoding.FaceThePalsy.ProfileActivity
import net.simplifiedcoding.FaceThePalsy.R
import net.simplifiedcoding.FaceThePalsy.databinding.ActivityFollowBinding
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceDetectionActivity
import java.io.File
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
            "Unoszenie czoła (jedna strona)"
        ),
        ExerciseModel(
            "Ćwiczenie 2",
            "Unoszenie czoła (druga strona)"
        ),
        ExerciseModel(
            "Ćwiczenie 3",
            "Marszczenie czoła (jedna strona)"
        ),
        ExerciseModel(
            "Ćwiczenie 4",
            "Marszczenie czoła (druga strona)"
        ),
        ExerciseModel(
            "Ćwiczenie 5",
            "Uśmiech"
        )
    )

    private var currentExerciseIndex = 0
    private var success = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentExerciseIndex = intent.getIntExtra("currentExerciseIndex", 0)

        asymmetry = loadAsymmetryFromJson()

        if (asymmetry == 0F) {
            showFaceScanDialog()
        }

        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val resetButton = findViewById<Button>(R.id.reset)
        resetButton.setOnClickListener {
            when (currentExerciseIndex) {
                0 -> {
                    FollowBox.usrednianie_1 = 0
                    FollowBox.suma_1=0F
                    FollowBox.prog_1=0F
                }
                1 -> {
                    FollowBox.usrednianie_2 = 0
                    FollowBox.suma_2=0F
                    FollowBox.prog_2=0F
                }
                2 -> {
                    FollowBox.usrednianie_3 = 0
                    FollowBox.suma_3=0F
                    FollowBox.prog_3=0F
                }
                3 -> {
                    FollowBox.usrednianie_4 = 0
                    FollowBox.suma_4=0F
                    FollowBox.prog_4=0F
                }
            }
        }

        val playVideoButton = findViewById<Button>(R.id.PlayVideo)
        playVideoButton.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("currentExerciseIndex", currentExerciseIndex)
            startActivity(intent)
        }

        nextButton.setOnClickListener {
            when (currentExerciseIndex) {
                0 -> {
                    if(FollowBox.left_repeats>= repeats) success=true
                }
                1 -> {
                    if(FollowBox.right_repeats>=repeats) success=true
                }
                2 -> {
                    if(FollowBox.left_repeats_2>=repeats) success=true
                }
                3 -> {
                    if(FollowBox.right_repeats_2>=repeats) success=true
                }
                4 -> {
                    if(FollowBox.smile_repeats>=repeats) success=true
                }
            }
            if (currentExerciseIndex < exercises.size - 1 && success) {
                currentExerciseIndex+=1
                updateExerciseView()
                updateButtonStates()
            } else if(currentExerciseIndex == exercises.size -1 && success){
                showTrainingCompletedDialog()
                ProfileActivity.completedTrainings += 1
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
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
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
            Log.e(ContentValues.TAG, illegalStateException.message ?: "IllegalStateException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            Log.e(ContentValues.TAG, illegalArgumentException.message ?: "IllegalArgumentException")
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(detector: FaceDetector, imageProxy: ImageProxy, progressBar: ProgressBar) {
        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        detector.process(inputImage).addOnSuccessListener { faces ->
            binding.graphicOverlay.clear()
            faces.forEach { face ->
                val faceBox = FollowBox(binding.graphicOverlay, face, imageProxy.image!!.cropRect, currentExerciseIndex, repeats)
                binding.graphicOverlay.add(faceBox)
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }.addOnCompleteListener {
            imageProxy.close()
        }

        when (currentExerciseIndex) {
            0 -> {
                progressBar.progress = ((FollowBox.left_repeats.toFloat()/ repeats)*100).toInt()
            }
            1 -> {
                progressBar.progress =((FollowBox.right_repeats.toFloat()/ repeats)*100).toInt()
            }
            2 -> {
                progressBar.progress =((FollowBox.left_repeats_2.toFloat()/ repeats)*100).toInt()
            }
            3 -> {
                progressBar.progress =((FollowBox.right_repeats_2.toFloat()/ repeats)*100).toInt()
            }
            4 -> {
                progressBar.progress =((FollowBox.smile_repeats.toFloat()/ repeats)*100).toInt()
            }
        }

    }

    private fun updateExerciseView() {
        val exercise = exercises[currentExerciseIndex]
        success = false
        binding.graphicOverlay.clear()

//        val exerciseFilm = exercises_films[currentExerciseIndex]
//        gifImageView.setImageResource(exerciseFilm.gifResourceId)
    }

    private fun updateButtonStates() {
        val nextButton = findViewById<Button>(R.id.nextButton)

        nextButton.isEnabled = currentExerciseIndex <= exercises.size - 1
    }

    private fun showTrainingCompletedDialog() {
        FollowBox.smile_repeats = 0
        FollowBox.isSmiling = false
        FollowBox.left_repeats = 0
        FollowBox.right_repeats = 0
        FollowBox.isExercising_left = false
        FollowBox.isExercising_right = false
        FollowBox.left_repeats_2 = 0
        FollowBox.right_repeats_2 = 0
        FollowBox.isExercising_left_2 = false
        FollowBox.isExercising_right_2 = false
        FollowBox.prog_1 = 0F
        FollowBox.prog_2 = 0F
        FollowBox.prog_3 = 0F
        FollowBox.prog_4 = 0F
        FollowBox.suma_1 = 0F
        FollowBox.suma_2 = 0F
        FollowBox.suma_3 = 0F
        FollowBox.suma_4 = 0F
        FollowBox.usrednianie_1 = 0
        FollowBox.usrednianie_2 = 0
        FollowBox.usrednianie_3 = 0
        FollowBox.usrednianie_4 = 0
        FollowBox.sumat = 0F

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Zestaw treningowy ukończony")
        alertDialogBuilder.setMessage("Gratulacje! Ukończyłeś zestaw treningowy.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun loadAsymmetryFromJson(): Float {
        val fileName = "asymmetry_data.json"
        val file = File(getExternalFilesDir(null), fileName)

        if (file.exists()) {
            try {
                val gson = Gson()
                val jsonString = file.readText()
                val jsonArray = gson.fromJson(jsonString, JsonArray::class.java)

                if (jsonArray.size() > 0) {
                    val firstEntry = jsonArray.first().asJsonObject
                    asymmetry = firstEntry.getAsJsonPrimitive("asymmetry").asFloat
                    repeats = when {
                        asymmetry < 2.2 -> 8
                        asymmetry in 2.2..2.95 -> 10
                        else -> 12
                    }
                    return asymmetry
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Błąd podczas odczytu asymetrii z pliku JSON", e)
            }
        }
        return 0F
    }

    private fun showFaceScanDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Konieczne jest wykonanie skanu twarzy")
        alertDialogBuilder.setMessage("Aby rozpocząć trening, musisz wykonać skan twarzy i dobrać odpowiedni trening.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Tutaj możesz uruchomić aktywność do skanowania twarzy lub inny odpowiedni ekran
            val intent = Intent(this, FaceDetectionActivity::class.java)
            startActivity(intent)
            finish()
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
        var repeats: Int = 0
        var asymmetry: Float = 0f
    }
}

data class ExerciseModel(val name: String, val exerciseDescription: String)
