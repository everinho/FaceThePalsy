package net.simplifiedcoding.FaceThePalsy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainingResultAdapter(private val trainingResults: List<TrainingResult>) :
    RecyclerView.Adapter<TrainingResultAdapter.TrainingResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training_result, parent, false)
        return TrainingResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingResultViewHolder, position: Int) {
        val trainingResult = trainingResults[position]
        holder.bind(trainingResult)
    }

    override fun getItemCount(): Int {
        return trainingResults.size
    }

    class TrainingResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trainingNumberTextView: TextView = itemView.findViewById(R.id.trainingNumberTextView)
        private val trainingDateTextView: TextView = itemView.findViewById(R.id.trainingDateTextView)
        private val trainingTimeTextView: TextView = itemView.findViewById(R.id.trainingTimeTextView)

        fun bind(trainingResult: TrainingResult) {
            trainingNumberTextView.text = "Numer treningu: ${trainingResult.training_number}"
            trainingDateTextView.text = "Data: ${trainingResult.training_date}"
            trainingTimeTextView.text = "Czas: ${formatTime(trainingResult.training_time)}"
        }

        private fun formatTime(timeInMillis: Long): String {
            val seconds = (timeInMillis / 1000).toInt()
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("%d:%02d", minutes, remainingSeconds)
        }
    }
}

