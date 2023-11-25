package net.simplifiedcoding.FaceThePalsy

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    companion object {
        var asymmetry: Float = 0F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
        asymmetry = loadAsymmetryFromJson()

        val asymmetryTextView: TextView = findViewById(R.id.asymetria)

        if (asymmetry == 0F) {
            asymmetryTextView.text = "Nie zmierzono asymetrii!!"
        } else {
            asymmetryTextView.text = "Asymetria: $asymmetry"
            generateAndDisplayTrainingDays()
        }
    }

    private fun generateAndDisplayTrainingDays() {
        val trainingDays = loadTrainingDaysFromJson()
        val recyclerView: RecyclerView = findViewById(R.id.trainingDaysRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrainingDayAdapter(trainingDays)
    }

    private fun loadTrainingDaysFromJson(): List<TrainingDay> {
        if (asymmetry == 0F) {
            return emptyList()
        }

        val trainingDays = mutableListOf<TrainingDay>()

        try {
            val fileName = "training_data.json"
            val file = File(getExternalFilesDir(null), fileName)

            if (file.exists()) {
                val gson = Gson()
                val jsonString = file.readText()
                val jsonArray = gson.fromJson(jsonString, JsonArray::class.java)

                val totalTrainings = jsonArray.map { it.asJsonObject.getAsJsonPrimitive("total_training").asInt }.maxOrNull() ?: 7
                val jsonObject = jsonArray.firstOrNull()?.asJsonObject

                var completedTrainings = jsonObject?.getAsJsonPrimitive("completed_trainings")?.asInt ?: 0
                var trainingCount = jsonObject?.getAsJsonPrimitive("daily_training")?.asInt ?: 1
                var trainingDate = jsonObject?.getAsJsonPrimitive("date")?.asString
                trainingDate = changeDateFormat(trainingDate)

                for (i in 1..7) {

                    var isTrainingCompleted = (0 until trainingCount).map { it < completedTrainings }
                    trainingDays.add(TrainingDay(i, trainingCount, completedTrainings, isTrainingCompleted, trainingDate))
                    completedTrainings -= trainingCount
                }
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Błąd podczas odczytu danych treningowych z pliku JSON", e)
        }

        return trainingDays
    }

    private fun changeDateFormat(inputDate: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val date: Date = inputFormat.parse(inputDate) ?: Date()
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
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
                Log.e(ContentValues.TAG, "Błąd podczas odczytu asymetrii z pliku JSON", e)
            }
        }

        return 0F
    }
}

data class TrainingDay(
    val dayNumber: Int,
    val trainingCount: Int,
    val completedTrainings: Int,
    val isTrainingCompleted: List<Boolean> = List(trainingCount) { false },
    val trainingDate: String
)
