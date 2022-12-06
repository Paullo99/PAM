package com.example.lab7.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab7.FieldOfStudy
import com.example.lab7.MyDBHandler
import com.example.lab7.R

class ItemAdapter(private val context: Context, private val dataset: List<FieldOfStudy>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val specialisationTextView: TextView = view.findViewById(R.id.specialisationTextView)
        val amountOfStudentsTextView: TextView = view.findViewById(R.id.amountOfStudentsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val dbHandler = MyDBHandler(context, null, null, 1)
        val items = dbHandler.findAllFieldsOfStudy()

        holder.nameTextView.text = items[position].fieldOfStudyName
        holder.specialisationTextView.text = items[position].specialisation
        holder.amountOfStudentsTextView.text = items[position].amountOfStudents.toString()
    }

    override fun getItemCount(): Int {
        val dbHandler = MyDBHandler(context, null, null, 1)
        return dbHandler.findAllFieldsOfStudy().size
    }
}