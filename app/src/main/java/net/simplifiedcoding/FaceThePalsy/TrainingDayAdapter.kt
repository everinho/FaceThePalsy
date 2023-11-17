package net.simplifiedcoding.FaceThePalsy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainingDayAdapter(private val trainingDays: List<TrainingDay>) :
    RecyclerView.Adapter<TrainingDayAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayNumberTextView: TextView = itemView.findViewById(R.id.dayNumberTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val trainingCountTextView: TextView = itemView.findViewById(R.id.trainingCountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_day, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trainingDay = trainingDays[position]

        holder.dayNumberTextView.text = "Day ${trainingDay.dayNumber}"
        holder.dateTextView.text = "Date: ${trainingDay.date}"
        holder.trainingCountTextView.text = "Training count: ${trainingDay.trainingCount}"
    }

    override fun getItemCount(): Int {
        return trainingDays.size
    }
}
