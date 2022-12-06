package com.example.mountaineer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mountaineer.MainActivity
import com.example.mountaineer.R
import com.example.mountaineer.model.MountainExpedition

class MainActivityRVAdapter(private val dataset: List<MountainExpedition>, val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MainActivityRVAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val mountainNameTextView: TextView = view.findViewById(R.id.mountainNameTextView)
        val conquerDateTextView: TextView = view.findViewById(R.id.conquerDateTextView)

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
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}