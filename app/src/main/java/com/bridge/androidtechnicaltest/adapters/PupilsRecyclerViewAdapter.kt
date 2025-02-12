package com.bridge.androidtechnicaltest.adapters

import android.app.Activity
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
import com.bumptech.glide.Glide
import java.io.File

class PupilsRecyclerViewAdapter: RecyclerView.Adapter<PupilsRecyclerViewAdapter.PupilsViewModel>() {
    private lateinit var context: Context
    var dataset: List<Pupil> = ArrayList()
    lateinit var pupilClickCallback: PupilClickCallback

    inner class PupilsViewModel(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var image: ImageView = itemView.findViewById(R.id.pupil_image)
        private var name: TextView = itemView.findViewById(R.id.pupil_name)
        private var country: TextView = itemView.findViewById(R.id.pupil_country)
        private var deletw: ImageView = itemView.findViewById(R.id.pupil_delete)

        fun setData(position: Int) {
            val data = dataset[position]
//            if(data.image.isNotBlank()) {
//
//            }
            println("Glide ****************** ${data.image}")
            Glide.with(context)
                .load(File(data.image))
                .into(image);
            name.text = data.name
            country.text = data.country
            itemView.setOnClickListener {
                pupilClickCallback.onPupilClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PupilsViewModel {
        context = parent.context;
        return PupilsViewModel(LayoutInflater.from(context).inflate(R.layout.pupil_detail_item, parent, false))
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: PupilsViewModel, position: Int) {
        holder.setData(position)
    }
}