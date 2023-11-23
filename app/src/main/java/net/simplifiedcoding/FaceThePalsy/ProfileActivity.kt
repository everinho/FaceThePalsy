package net.simplifiedcoding.FaceThePalsy

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    companion object {
        var asymmetry: Float = 0F
        var faceScanHistory: MutableList<FaceScan> = mutableListOf()
        var totalTrainings: Int = 0
        var completedTrainings: Int = 0
    }

    lateinit var asymetria: TextView
    lateinit var faceScanHistoryRecyclerView: RecyclerView
    lateinit var faceScanHistoryAdapter: FaceScanHistoryAdapter
    lateinit var progressBar: ProgressBar
    lateinit var trainingsTextView: TextView

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
        faceScanHistoryAdapter = FaceScanHistoryAdapter(faceScanHistory)

        if (asymmetry == 0F) {
            asymetria.text = "Nie zmierzono asymetrii!!"
        } else {
            asymetria.text = "Asymetria: $asymmetry"

            // Dodaj nowy skan do historii
            val currentTimestamp = System.currentTimeMillis()
            val newFaceScan = FaceScan(asymmetry, currentTimestamp, getCurrentDate())
            faceScanHistory.add(0, newFaceScan)

            // Powiadom adapter o aktualizacji danych
            faceScanHistoryAdapter.notifyDataSetChanged()
        }

        faceScanHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        faceScanHistoryRecyclerView.adapter = faceScanHistoryAdapter

        updateTrainings()
        updateProgressBar()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
    private fun updateTrainings() {
        if (asymmetry < 2.2) {
            totalTrainings = 7
        } else if (asymmetry in 2.2..2.95) {
            totalTrainings = 14
        } else {
            totalTrainings = 21
        }
    }

    private fun updateProgressBar() {
        progressBar = findViewById(R.id.progress_bar)
        trainingsTextView = findViewById(R.id.trainingsTextView)

        val progress = (completedTrainings.toDouble() / totalTrainings.toDouble() * 100).toInt()

        progressBar.progress = progress

        trainingsTextView.text = "Postęp: $progress%"
    }
}

data class FaceScan(
    val asymmetry: Float,
    val timestamp: Long,
    val date: String
)
