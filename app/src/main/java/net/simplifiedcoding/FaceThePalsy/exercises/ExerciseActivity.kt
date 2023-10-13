package net.simplifiedcoding.FaceThePalsy.exercises

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.simplifiedcoding.FaceThePalsy.MainActivity
import net.simplifiedcoding.FaceThePalsy.R
import pl.droidsonroids.gif.GifImageView
import androidx.appcompat.app.AlertDialog

class ExerciseActivity : AppCompatActivity() {

    private lateinit var gifImageView: GifImageView
    private lateinit var exerciseNameTextView: TextView

    private val exercises = listOf(
        Exercise("Unoszenie czoła - 10 powtórzeń", R.drawable.film1),
        Exercise("Marszczenie czoła - 10 powtórzeń", R.drawable.film2),
        Exercise("Uśmiech - 10 powtórzeń", R.drawable.film3)
    )

    private var currentExerciseIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        gifImageView = findViewById(R.id.gifImageView)
        exerciseNameTextView = findViewById(R.id.exerciseName)

        val prevButton = findViewById<Button>(R.id.prevButton)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        prevButton.setOnClickListener {
            if (currentExerciseIndex > 0) {
                currentExerciseIndex--
                updateExerciseView()
                updateButtonStates()
            }
        }

        nextButton.setOnClickListener {
            if (currentExerciseIndex < exercises.size - 1) {
                currentExerciseIndex++
                updateExerciseView()
                updateButtonStates()
            } else {
                // Jeżeli to ostatnie ćwiczenie, wyświetl komunikat o ukończeniu zestawu treningowego
                showTrainingCompletedDialog()
            }
        }

        // Wyświetl pierwsze ćwiczenie na starcie
        updateExerciseView()
        updateButtonStates()
    }
    private fun showTrainingCompletedDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Zestaw treningowy ukończony")
        alertDialogBuilder.setMessage("Gratulacje! Ukończyłeś zestaw treningowy.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Przenieś użytkownika do głównej aktywności
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun updateExerciseView() {
        val exercise = exercises[currentExerciseIndex]
        exerciseNameTextView.text = exercise.name
        gifImageView.setImageResource(exercise.gifResourceId)
    }

    private fun updateButtonStates() {
        val prevButton = findViewById<Button>(R.id.prevButton)
        prevButton.isEnabled = currentExerciseIndex > 0

        //val nextButton = findViewById<Button>(R.id.nextButton)
        //nextButton.isEnabled = currentExerciseIndex < exercises.size - 1
    }
}

data class Exercise(val name: String, val gifResourceId: Int)