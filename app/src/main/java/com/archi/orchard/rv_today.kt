package com.archi.orchard

import android.app.Activity
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.security.AccessController.getContext

class CustomRecyclerAdapter_main(private val names: List<String>, private val nextdays: List<String>) : RecyclerView
.Adapter<CustomRecyclerAdapter_main.MyViewMainHolder>() {

    class MyViewMainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val r_name: TextView = itemView.findViewById(R.id.r_name)
        val r_date: TextView = itemView.findViewById(R.id.r_date)
        val l_id: LinearLayout = itemView.findViewById(R.id.l_id)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewMainHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.plant_today, parent, false)
        return MyViewMainHolder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewMainHolder, position: Int) {



       holder.r_name.text = names[position]
       holder.r_date.text = nextdays[position]







        if (check_date(nextdays[position])==-1) {holder.l_id.setBackgroundResource(R.drawable.rounded_red)}



        //var wateringArray = holder.itemView.resources.getStringArray(R.array.watering_types)

        //holder.tv_date.text = nextdays[position]

        // check_date(nextdays[position])


        //holder.l_id.setBackgroundColor(0xff6666)

        //holder.l_id.setBackgroundResource(R.drawable.rounded_red)





        //Log.d("MyLog", wateringArray.size.toString())
        //Log.d("MyLog", wateringArray[position].toString() + " " + position)
        //Log.d("MyLog", wateringArray.size.toString())

        //holder.tv_watering.text = wateringArray[watering_from_db[position]].toString()
        //holder.tv_watering.text = "meow"




    }





}

