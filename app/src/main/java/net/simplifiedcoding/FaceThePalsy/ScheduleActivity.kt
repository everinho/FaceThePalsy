package net.simplifiedcoding.FaceThePalsy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val asymmetryTextView: TextView = findViewById(R.id.asymetria)

        if (asymmetry == 0F) {
            asymmetryTextView.text = "Nie zmierzono asymetrii!!"
        } else {
            asymmetryTextView.text = "Asymetria: $asymmetry"
            generateAndDisplayTrainingDays()
        }
    }

    private fun generateAndDisplayTrainingDays() {
        val trainingDays = generateTrainingDays()
        val recyclerView: RecyclerView = findViewById(R.id.trainingDaysRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrainingDayAdapter(trainingDays)
    }

    private fun generateTrainingDays(): List<TrainingDay> {
        if (asymmetry == 0F) {
            // Asymetria nie została zmierzona, zwróć pustą listę dni treningowych
            return emptyList()
        }

        val trainingDays = mutableListOf<TrainingDay>()
        val currentDate = Calendar.getInstance()
        currentDate.time = Date()

        for (i in 0 until 7) { // Tworzymy harmonogram na 7 dni
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.time)
            val trainingCount = when {
                asymmetry < 2.2 -> 1
                asymmetry in 2.2..2.95 -> 2
                else -> 3
            }
            val isTrainingCompleted = List(trainingCount) { false }
            trainingDays.add(TrainingDay(i + 1, date, trainingCount, isTrainingCompleted))
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
        }

        return trainingDays
    }
}

data class TrainingDay(
    val dayNumber: Int,
    val date: String,
    val trainingCount: Int,
    val isTrainingCompleted: List<Boolean> = List(trainingCount) { false }
)
