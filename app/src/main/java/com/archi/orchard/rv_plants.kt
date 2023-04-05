package com.archi.orchard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerAdapter(private val names: List<String>, private val notes: List<String>, private val watering_from_db: List<Int>) : RecyclerView
.Adapter<CustomRecyclerAdapter.MyViewPlantsHolder>() {

    class MyViewPlantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_note: TextView = itemView.findViewById(R.id.tv_note)
        val tv_watering: TextView = itemView.findViewById(R.id.tv_watering)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewPlantsHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.plant_list, parent, false)
        return MyViewPlantsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size;
    }

    override fun onBindViewHolder(holder: MyViewPlantsHolder, position: Int) {
        holder.tv_name.text = names[position]
        holder.tv_note.text = notes[position]



        var wateringArray = holder.itemView.resources.getStringArray(R.array.watering_types)





        //Log.d("MyLog", wateringArray.size.toString())
        //Log.d("MyLog", wateringArray[position].toString() + " " + position)
        //Log.d("MyLog", wateringArray.size.toString())

        holder.tv_watering.text = wateringArray[watering_from_db[position]].toString()
        //holder.tv_watering.text = "meow"

    }

}