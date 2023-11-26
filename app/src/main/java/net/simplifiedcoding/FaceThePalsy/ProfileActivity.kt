package net.simplifiedcoding.FaceThePalsy

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceBox
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    companion object {
        var asymmetry: Float = 0F
        var faceScanHistory: MutableList<FaceScan> = mutableListOf()
        var totalTrainings: Int = 0
        var completedTrainings: Int = 0
        var sound: Boolean = false
    }

    private lateinit var asymetria: TextView
    private lateinit var faceScanHistoryRecyclerView: RecyclerView
    private lateinit var faceScanHistoryAdapter: FaceScanHistoryAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var trainingsTextView: TextView
    private lateinit var trainingResultRecyclerView: RecyclerView
    private lateinit var trainingResultAdapter: TrainingResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        asymetria = findViewById(R.id.asymetria)
        faceScanHistoryRecyclerView = findViewById(R.id.faceScanHistoryRecyclerView)
        faceScanHistoryAdapter = FaceScanHistoryAdapter(loadFaceScanHistoryFromJson())

        asymmetry = loadAsymmetryFromJson()
        loadTrainingDataFromJson()

        trainingResultRecyclerView = findViewById(R.id.trainingResultRecyclerView)
        trainingResultAdapter = TrainingResultAdapter(loadTrainingResultsFromJson())
        trainingResultRecyclerView.layoutManager = LinearLayoutManager(this)
        trainingResultRecyclerView.adapter = trainingResultAdapter

        if (asymmetry == 0F) {
            asymetria.text = "Nie zmierzono asymetrii!!"
        } else {
            asymetria.text = "Asymetria: $asymmetry"
            val newFaceScan = FaceScan(asymmetry, getCurrentDate())
            if (!faceScanHistory.contains(newFaceScan)) {
                faceScanHistory.add(newFaceScan)
            }
        }

        faceScanHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        faceScanHistoryRecyclerView.adapter = faceScanHistoryAdapter

        updateProgressBar()
    }

    private fun loadTrainingResultsFromJson(): List<TrainingResult> {
        val fileName = "training_result.json"
        val file = File(getExternalFilesDir(null), fileName)

        if (file.exists()) {
            try {
                val gson = Gson()
                val jsonString = file.readText()
                val jsonArray = gson.fromJson(jsonString, JsonArray::class.java)

                if (jsonArray.size() > 0) {
                    val trainingResultList = mutableListOf<TrainingResult>()
                    for (jsonElement in jsonArray) {
                        val jsonObject = jsonElement.asJsonObject
                        val trainingNumber = jsonObject.getAsJsonPrimitive("training_number").asInt
                        val trainingDate = jsonObject.getAsJsonPrimitive("training_date").asString
                        val trainingTime = jsonObject.getAsJsonPrimitive("training_time").asLong
                        trainingResultList.add(TrainingResult(trainingNumber, trainingDate, trainingTime))
                    }
                    return trainingResultList
                }
            } catch (e: Exception) {
                Log.e(TAG, "Błąd podczas odczytu wyników treningów z pliku JSON", e)
            }
        }

        return emptyList()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun updateProgressBar() {
        progressBar = findViewById(R.id.progress_bar)
        trainingsTextView = findViewById(R.id.trainingsTextView)

        val progress = (completedTrainings.toDouble() / totalTrainings.toDouble() * 100).toInt()
        //val progress = (7.toDouble() / totalTrainings.toDouble() * 100).toInt()

        progressBar.progress = progress

        trainingsTextView.text = "Postęp: $progress%"
        if(completedTrainings==totalTrainings && !sound)
        {
            playSound()
            sound=true
        }
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
                    // Pobierz asymetrię z pierwszego wpisu w pliku
                    val firstEntry = jsonArray.first().asJsonObject
                    return firstEntry.getAsJsonPrimitive("asymmetry").asFloat
                }
            } catch (e: Exception) {
                Log.e(TAG, "Błąd podczas odczytu asymetrii z pliku JSON", e)
            }
        }

        return 0F
    }

    private fun loadFaceScanHistoryFromJson(): List<FaceScan> {
        val fileName = "asymmetry_data.json"
        val file = File(getExternalFilesDir(null), fileName)

        if (file.exists()) {
            try {
                val gson = Gson()
                val jsonString = file.readText()
                val jsonArray = gson.fromJson(jsonString, JsonArray::class.java)

                if (jsonArray.size() > 0) {
                    val faceScanList = mutableListOf<FaceScan>()
                    for (jsonElement in jsonArray) {
                        val jsonObject = jsonElement.asJsonObject
                        val asymmetry = jsonObject.getAsJsonPrimitive("asymmetry").asFloat
                        val date = jsonObject.getAsJsonPrimitive("date").asString
                        faceScanList.add(FaceScan(asymmetry, date))
                    }
                    return faceScanList
                }
            } catch (e: Exception) {
                Log.e(TAG, "Błąd podczas odczytu historii skanów z pliku JSON", e)
            }
        }

        return emptyList()
    }

    private fun loadTrainingDataFromJson() {
        val fileName = "training_data.json"
        val file = File(getExternalFilesDir(null), fileName)

        if (file.exists()) {
            try {
                val gson = Gson()
                val jsonString = file.readText()
                val jsonArray = gson.fromJson(jsonString, JsonArray::class.java)

                if (jsonArray.size() > 0) {
                    // Pobierz wartości total_training i completed_trainings z ostatniego wpisu w pliku
                    val lastEntry = jsonArray.last().asJsonObject
                    totalTrainings = lastEntry.getAsJsonPrimitive("total_training").asInt
                    completedTrainings = lastEntry.getAsJsonPrimitive("completed_trainings").asInt
                }
            } catch (e: Exception) {
                Log.e(TAG, "Błąd podczas odczytu danych treningowych z pliku JSON", e)
            }
        }
    }

    private fun playSound() {
        try {
            if (FaceBox.mediaPlayer == null) {
                FaceBox.mediaPlayer = MediaPlayer.create(this, R.raw.plan)
            }
            FaceBox.mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "Error playing sound", e)
        }
    }
}

data class FaceScan(
    val asymmetry: Float,
    val date: String
)

data class TrainingResult(
    val training_number: Int,
    val training_date: String,
    val training_time: Long
)




