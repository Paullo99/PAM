package com.example.mountaineer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mountaineer.R
import com.example.mountaineer.helper.ImageRotator
import com.example.mountaineer.model.MountainExpedition
import java.io.File
import kotlin.coroutines.coroutineContext

class MainActivityRVAdapter(private val dataset: List<MountainExpedition>, val onItemClickListener: OnItemClickListener, val context: Context) :
    RecyclerView.Adapter<MainActivityRVAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val mountainNameTextView: TextView = view.findViewById(R.id.mountainNameTextView)
        val conquerDateTextView: TextView = view.findViewById(R.id.conquerDateTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_main_activity, parent, false)
        return ItemViewHolder(adapterLayout, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.mountainNameTextView.text = dataset[position].mountainName
        holder.conquerDateTextView.text = dataset[position].conquerDate
        if (dataset[position].photoFileName != "") {
            val photoFile = File(context.getExternalFilesDir(null), dataset[position].photoFileName)
            val imageRotator = ImageRotator()
            holder.imageView.setImageBitmap(imageRotator.getImageOriginalOrientation(photoFile))
        }
        else{
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_photo_24)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}