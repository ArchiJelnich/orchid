package com.archi.orchard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [new_plants_screen.newInstance] factory method to
 * create an instance of this fragment.
 */
class new_plants_screen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_plants_screen, container, false)









    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add image
        // add name
        // add type
        // add note
        // add watering type
        // sanitize text
        // move to garden


        val tv_add = view.findViewById<View>(R.id.tv_add) as TextView
        val tv_name = view.findViewById<View>(R.id.tv_name) as TextView
        val tv_note = view.findViewById<View>(R.id.tv_note) as TextView

        var watering_position = 0

        tv_add.setOnClickListener { onClick(tv_add, watering_position) }
        tv_name.setOnClickListener { onClick(tv_name) }
        tv_note.setOnClickListener { onClick(tv_note) }

        val watering = this.resources.getStringArray(R.array.watering_types)
        val spinner = view.findViewById(R.id.sp_water) as Spinner



        if (spinner != null) {
            var adapter = context?.let { ArrayAdapter(it, R.layout.spinner_item_tr, watering) }
            spinner.adapter = adapter

            if (adapter != null) {
                adapter.setDropDownViewResource(R.layout.spinner_item_drop)
            }


            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View?, position: Int, id: Long) {

                    watering_position = position

                }


                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }




    }



    private fun onClick(v: View, watering_position: Int = 0) {
        when (v.id) {
            R.id.tv_add -> {



                val tv_name = view?.findViewById<View>(R.id.tv_name) as TextView
                val tv_note = view?.findViewById<View>(R.id.tv_note) as TextView

                val re = Regex("[^a-zA-Z0-9А-Яа-я ]")




                var plant_name = re.replace(tv_name.text.toString(), "").take(15)
                var plant_note = re.replace(tv_note.text.toString(), "").take(15)


                val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "garden").allowMainThreadQueries().build()
                val gardenDao = db.gardenDao()
                val newPlant = Plant(plant_name, plant_note, "Placeholder_image", "Placeholder_date", watering_position)
                gardenDao.insertAll(newPlant)

                findNavController().navigate(R.id.action_new_plants_screen_to_my_plants_screen)

                //Log.v("MyLog", "All " + gardenDao.getAll())


            }

            R.id.tv_name -> {
                val tv_name = view?.findViewById<View>(R.id.tv_name) as TextView
                tv_name.text=""
            }
            R.id.tv_note -> {
                val tv_note = view?.findViewById<View>(R.id.tv_note) as TextView
                tv_note.text=""
            }

        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment new_plants_screen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            new_plants_screen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}