package com.bonustrack02.parktp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MapAdapter (val context : Context, var mapItems : MutableList<MapItem>) : RecyclerView.Adapter<MapAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerTextName : TextView by lazy { itemView.findViewById(R.id.recycler_text_name) }
        val recyclerTextConvenience : TextView by lazy { itemView.findViewById(R.id.recycler_text_convenience) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mapItem = mapItems.get(position)

        holder.recyclerTextName.text = mapItem.name
        holder.recyclerTextConvenience.text = mapItem.convinience

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WriteActivity::class.java)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mapItems.size
}