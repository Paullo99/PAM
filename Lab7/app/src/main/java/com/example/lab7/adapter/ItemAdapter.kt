package com.example.lab7.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab7.MyDBHandler
import com.example.lab7.Product
import com.example.lab7.R

class ItemAdapter(private val context: Context, private val dataset: List<Product>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val itemNameTextView: TextView = view.findViewById(R.id.itemName)
        val itemQuantityTextView: TextView = view.findViewById(R.id.itemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val dbHandler = MyDBHandler(context, null, null, 1)
        val items = dbHandler.findAllProducts()
        holder.itemNameTextView.text = items[position].productName
        holder.itemQuantityTextView.text = items[position].quantity.toString()
    }

    override fun getItemCount(): Int {
        val dbHandler = MyDBHandler(context, null, null, 1)
        return dbHandler.findAllProducts().size
    }
}