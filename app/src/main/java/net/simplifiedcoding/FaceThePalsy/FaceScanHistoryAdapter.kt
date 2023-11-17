package net.simplifiedcoding.FaceThePalsy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class FaceScanHistoryAdapter(private val faceScans: List<FaceScan>) :
    RecyclerView.Adapter<FaceScanHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val asymmetryTextView: TextView = itemView.findViewById(R.id.faceScanAsymmetry)
        val timestampTextView: TextView = itemView.findViewById(R.id.faceScanTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_face_scan, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faceScan = faceScans[position]

        // Formatuj datę
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(faceScan.timestamp))

        holder.asymmetryTextView.text = "Asymetria: ${faceScan.asymmetry}"
        holder.timestampTextView.text = "Data: $formattedDate"
    }

    override fun getItemCount(): Int {
        return faceScans.size
    }
}
