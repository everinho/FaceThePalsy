package net.simplifiedcoding.FaceThePalsy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TrainingDayAdapter(private val trainingDays: List<TrainingDay>) :
    RecyclerView.Adapter<TrainingDayAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayNumberTextView: TextView = itemView.findViewById(R.id.dayNumberTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val trainingIcon1: ImageView = itemView.findViewById(R.id.trainingIcon1)
        val trainingIcon2: ImageView = itemView.findViewById(R.id.trainingIcon2)
        val trainingIcon3: ImageView = itemView.findViewById(R.id.trainingIcon3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_day, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainingDay = trainingDays[position]

        holder.dayNumberTextView.text = "Dzień ${trainingDay.dayNumber}"
        val trainingDate = trainingDay.trainingDate
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val date = dateFormat.parse(trainingDate)
        calendar.time = date ?: Date()
        calendar.add(Calendar.DAY_OF_MONTH, trainingDay.dayNumber-1)
        val newDate = dateFormat.format(calendar.time)
        holder.dateTextView.text = "$newDate"

        val trainingIcons = listOf(
            holder.trainingIcon1,
            holder.trainingIcon2,
            holder.trainingIcon3
        )

        for (i in 0 until 3) { // Maksymalna ilość treningów na dzień
            val icon = trainingIcons[i]
            if (i < trainingDay.trainingCount) {
                if (i < trainingDay.completedTrainings) {
                    icon.setImageResource(R.drawable.checkmark) // Ustaw ikonę ptaszka, gdy trening został wykonany
                } else {
                    icon.setImageResource(R.drawable.checkbox) // Ustaw pusty kwadrat, gdy trening nie został wykonany
                }
            } else {
                icon.visibility = View.GONE // Ukryj nieużywane ikony
            }
        }
    }

    override fun getItemCount(): Int {
        return trainingDays.size
    }
}

