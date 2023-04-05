package com.archi.orchard

import android.os.Build
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.security.AccessController.getContext

class CustomRecyclerAdapter(private val names: List<String>, private val notes: List<String>, private val watering_from_db: List<Int>, private val nextdays: List<String>) : RecyclerView
.Adapter<CustomRecyclerAdapter.MyViewPlantsHolder>() {

    class MyViewPlantsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val tv_note: TextView = itemView.findViewById(R.id.tv_note)
        val tv_watering: TextView = itemView.findViewById(R.id.tv_watering)
        val tv_date: TextView = itemView.findViewById(R.id.tv_date)

        val l_id: LinearLayout = itemView.findViewById(R.id.l_id)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewPlantsHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.plant_list, parent, false)
        return MyViewPlantsHolder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewPlantsHolder, position: Int) {
        holder.tv_name.text = names[position]
        holder.tv_note.text = notes[position]



        var wateringArray = holder.itemView.resources.getStringArray(R.array.watering_types)

        holder.tv_date.text = nextdays[position]

        // check_date(nextdays[position])


        //holder.l_id.setBackgroundColor(0xff6666)

        //holder.l_id.setBackgroundResource(R.drawable.rounded_red)

        if (check_date(nextdays[position])==-1) {holder.l_id.setBackgroundResource(R.drawable.rounded_red)}
        if (check_date(nextdays[position])==1) {holder.l_id.setBackgroundResource(R.drawable.rounded_yellow)}





        //Log.d("MyLog", wateringArray.size.toString())
        //Log.d("MyLog", wateringArray[position].toString() + " " + position)
        //Log.d("MyLog", wateringArray.size.toString())

        holder.tv_watering.text = wateringArray[watering_from_db[position]].toString()
        //holder.tv_watering.text = "meow"

    }

}