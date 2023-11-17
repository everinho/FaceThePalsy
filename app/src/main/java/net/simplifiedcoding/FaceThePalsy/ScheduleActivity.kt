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
        var assymetry: Float = 0F
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

        val asymetria: TextView = findViewById(R.id.asymetria)
        if (assymetry == 0F) {
            asymetria.text = "Nie zmierzono asymetrii!!"
        } else {
            asymetria.text = "Asymetria: $assymetry"
        }

        val trainingDays = generateTrainingDays()
        val recyclerView: RecyclerView = findViewById(R.id.trainingDaysRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrainingDayAdapter(trainingDays)
    }

    private fun generateTrainingDays(): List<TrainingDay> {
        val trainingDays = mutableListOf<TrainingDay>()
        val currentDate = Calendar.getInstance()
        currentDate.time = Date()

        for (i in 0 until 7) { // Tworzymy harmonogram na 7 dni
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate.time)
            val trainingCount = if (assymetry > 3) 3 else 2
            trainingDays.add(TrainingDay(i + 1, date, trainingCount))
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
        }

        return trainingDays
    }
}

data class TrainingDay(
    val dayNumber: Int,
    val date: String,
    val trainingCount: Int
)
