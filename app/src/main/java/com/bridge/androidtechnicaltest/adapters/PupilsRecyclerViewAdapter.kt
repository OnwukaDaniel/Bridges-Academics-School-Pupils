package com.bridge.androidtechnicaltest.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.interfaces.PupilClickCallback
import com.bridge.androidtechnicaltest.interfaces.PupilDeleteCallback
import com.bumptech.glide.Glide
import java.io.File

class PupilsRecyclerViewAdapter :
    RecyclerView.Adapter<PupilsRecyclerViewAdapter.PupilsViewModel>() {
    private lateinit var context: Context
    var dataset: List<Pupil> = ArrayList()
    lateinit var pupilClickCallback: PupilClickCallback
    lateinit var pupilDeleteCallback: PupilDeleteCallback

    inner class PupilsViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.pupil_image)
        private var name: TextView = itemView.findViewById(R.id.pupil_name)
        private var country: TextView = itemView.findViewById(R.id.pupil_country)
        private var delete: ImageView = itemView.findViewById(R.id.pupil_delete)

        fun setData(position: Int) {
            val data = dataset[position]
//            if(data.image.isNotBlank()) {
//
//            }
            println("Glide ****************** ${data.image}")
            Glide.with(context)
                .load(File(data.image))
                .centerCrop()
                .error(R.drawable.error_image)
                .into(image)
            name.text = data.name
            country.text = data.country
            itemView.setOnClickListener {
                pupilClickCallback.onPupilClick(data)
            }
            delete.setOnClickListener {
                deleteDialog(data)
            }
        }

        private fun deleteDialog(data: Pupil) {
            val inflater = LayoutInflater.from(context)
            val dialogView: View = inflater.inflate(R.layout.dialog_delete_confirmation, null)
            dialogView.setPadding(46, 46, 46, 46)
            val dialog: AlertDialog =
                AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .setPositiveButton("Delete") { dialogInterface, _ ->
                        pupilDeleteCallback.onDeleteClicked(data)
                        dialogInterface.dismiss()
                    }.create()
            dialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PupilsViewModel {
        context = parent.context
        return PupilsViewModel(
            LayoutInflater.from(context).inflate(R.layout.pupil_detail_item, parent, false)
        )
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: PupilsViewModel, position: Int) {
        holder.setData(position)
    }
}