package net.simplifiedcoding.FaceThePalsy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HelpActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val zasadyList = listOf(
            "1: Aplikacja przystosowana jest do pracy z jednym użytkownikiem.",
            "2: Dla odpowiedniej pracy algorytmów ważna jest odpowiednio niewielka odległość twarzy od urządzenia.",
            "3: Oczywista jest konieczność odpowiedniego oświetlenia twarzy, aby umożliwić dokładne wykrycie i pomiar punktów na twarzy przez algorytmy.",
            "4: Najlepsze rezultaty dla skanowania twarzy zostaną uzyskane przy minie łagodnego uśmiechu.",
            "5: Przed rozpoczęciem każdego ćwiczenia ważne jest przyjęcie mimiki naturalnego, łagodnego uśmiechu."
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ZasadyAdapter(zasadyList)

    }
}