package com.bonustrack02.parktp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class InfoAdapter(var context: Context, var infoItems: MutableList<InfoItem>) : BaseAdapter() {
    override fun getCount(): Int {
        return infoItems.size
    }

    override fun getItem(p0: Int): Any {
        return infoItems[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var v = p1
        if (v == null) {
            val inflater = LayoutInflater.from(context)
            v = inflater.inflate(R.layout.listview_item, p2, false)
        }

        var listIcon = v!!.findViewById<ImageView>(R.id.list_icon)
        var listTitle = v!!.findViewById<TextView>(R.id.list_title)

        var infoItem = infoItems[p0]
        listIcon.setImageResource(infoItem.icon)
        listTitle.text = infoItem.title
        return v
    }

}