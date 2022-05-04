package com.bonustrack02.parktp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MapAdapter (val context : Context, var responseItem: ResponseItem) : RecyclerView.Adapter<MapAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerTextName : TextView by lazy { itemView.findViewById(R.id.recycler_text_name) }
        val recyclerTextDistance : TextView by lazy { itemView.findViewById(R.id.recycler_text_distance) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.recyclerTextName.text = responseItem.documents[position].place_name
        holder.recyclerTextDistance.text = "${responseItem.documents[position].distance}m"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ParkDetailActivity::class.java)
            intent.putExtra("title", responseItem.documents[position].place_name)
            intent.putExtra("position_lat", responseItem.documents[position].y)
            intent.putExtra("position_lng", responseItem.documents[position].x)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = responseItem.documents.size
}