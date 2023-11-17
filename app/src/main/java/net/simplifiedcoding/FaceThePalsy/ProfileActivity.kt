package net.simplifiedcoding.FaceThePalsy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {

    companion object{
        var assymetry: Float = 0F
        var faceScanHistory: MutableList<FaceScan> = mutableListOf()
    }

    lateinit var asymetria: TextView
    lateinit var faceScanHistoryRecyclerView: RecyclerView
    lateinit var faceScanHistoryAdapter: FaceScanHistoryAdapter

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

        if (ScheduleActivity.assymetry == 0F) {
            asymetria.text = "Nie zmierzono asymetrii!!"
        } else {
            asymetria.text = "Asymetria: ${ScheduleActivity.assymetry}"

            // Dodaj nowy skan do historii
            val currentTimestamp = System.currentTimeMillis()
            val newFaceScan = FaceScan(ScheduleActivity.assymetry, currentTimestamp)
            faceScanHistory.add(0, newFaceScan)

            // Powiadom adapter o aktualizacji danych
            faceScanHistoryAdapter.notifyDataSetChanged()
        }

        faceScanHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        faceScanHistoryRecyclerView.adapter = faceScanHistoryAdapter

    }
}

data class FaceScan(
    val asymmetry: Float,
    val timestamp: Long
)
