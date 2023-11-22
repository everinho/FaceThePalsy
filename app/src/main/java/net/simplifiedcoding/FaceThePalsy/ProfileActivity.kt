package net.simplifiedcoding.FaceThePalsy

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class ProfileActivity : AppCompatActivity() {

    companion object {
        var asymmetry: Float = 0F
        var faceScanHistory: MutableList<FaceScan> = mutableListOf()
    }

    lateinit var asymetria: TextView
    lateinit var faceScanHistoryRecyclerView: RecyclerView
    lateinit var faceScanHistoryAdapter: FaceScanHistoryAdapter

    private fun loadAsymmetryData() {
        try {
            // Wczytaj dane z pliku
            val fileName = "asymmetry_data.json"
            val file = File(this.getExternalFilesDir(null), fileName)
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)

            // Tworzenie obiektu JSON za pomocą Gson
            val gson = Gson()

            // Sprawdzenie, czy to jest obiekt czy tablica
            val jsonElement = gson.fromJson(bufferedReader, JsonElement::class.java)
            if (jsonElement.isJsonArray) {
                // Parsowanie listy obiektów JSON
                val listType = object : TypeToken<List<FaceScan>>() {}.type
                val faceScanList: List<FaceScan> = gson.fromJson(jsonElement, listType)

                // Iterowanie po elementach listy
                for (faceScan in faceScanList) {
                    Log.d(TAG, "Wczytano dane z pliku JSON: Asymetria=${faceScan.asymmetry}, Data=${faceScan.date}")
                }
            } else if (jsonElement.isJsonObject) {
                // Parsowanie pojedynczego obiektu JSON
                val faceScan = gson.fromJson(jsonElement, FaceScan::class.java)
                Log.d(TAG, "Wczytano dane z pliku JSON: Asymetria=${faceScan.asymmetry}, Data=${faceScan.date}")
            }

            // Zamknij strumienie
            bufferedReader.close()
            fileReader.close()

        } catch (e: Exception) {
            Log.e(TAG, "Błąd podczas wczytywania danych z pliku JSON", e)
        }
    }


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

        loadAsymmetryData()

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
    }

    private fun getCurrentDate(): String {
        // Tutaj dodaj logikę uzyskiwania aktualnej daty w odpowiednim formacie (np. "yyyy-MM-dd HH:mm:ss")
        // Możesz użyć klasy SimpleDateFormat lub innych dostępnych narzędzi w Androidzie
        // Na przykład:
        // val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        // return sdf.format(Date())
        return "2023-11-19 21:00:00" // Tutaj zastąp odpowiednią datą w odpowiednim formacie
    }
}

data class FaceScan(
    val asymmetry: Float,
    val timestamp: Long,
    val date: String
)
