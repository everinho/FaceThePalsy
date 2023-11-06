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
        Exercise("Unoszenie czoła", R.drawable.film1),
        Exercise("Unoszenie czoła", R.drawable.film1),
        Exercise("Marszczenie czoła", R.drawable.film2),
        Exercise("Marszczenie czoła", R.drawable.film2),
        Exercise("Uśmiech", R.drawable.film3)
    )

    private var currentExerciseIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        gifImageView = findViewById(R.id.gifImageView)
        exerciseNameTextView = findViewById(R.id.exerciseName)

        val backButton = findViewById<Button>(R.id.backButton)

        currentExerciseIndex = intent.getIntExtra("currentExerciseIndex", 0)

        backButton.setOnClickListener {
            val intent = Intent(this, FollowActivity::class.java)
            intent.putExtra("currentExerciseIndex", currentExerciseIndex)
            startActivity(intent)
            finish()
        }

        updateExerciseView()
        updateButtonStates()
    }

    private fun updateExerciseView() {
        val exercise = exercises[currentExerciseIndex]
        exerciseNameTextView.text = exercise.name
        gifImageView.setImageResource(exercise.gifResourceId)
    }

    private fun updateButtonStates() {
//        val prevButton = findViewById<Button>(R.id.prevButton)
//        prevButton.isEnabled = currentExerciseIndex > 0

        //val nextButton = findViewById<Button>(R.id.nextButton)
        //nextButton.isEnabled = currentExerciseIndex < exercises.size - 1
    }
}

data class Exercise(val name: String, val gifResourceId: Int)