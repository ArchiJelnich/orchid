package com.archi.orchard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [my_plants_screen.newInstance] factory method to
 * create an instance of this fragment.
 */
class my_plants_screen : Fragment() {
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
        return inflater.inflate(R.layout.fragment_my_plants_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "garden").allowMainThreadQueries().build()
        val gardenDao = db.gardenDao()

        val names = gardenDao.listOfNames().toList()
        val notes = gardenDao.listOfNotes().toList()
        val watering_from_db = gardenDao.listOfWateringTypes().toList()




        val bt_new = view.findViewById<View>(R.id.bt_nav_new) as TextView
        //val bt_current = view.findViewById<View>(R.id.bt_nav_current) as TextView

        bt_new.setOnClickListener { onClick(bt_new) }
        //bt_current.setOnClickListener { onClick(bt_current) }


        val rv_plants = view.findViewById<View>(R.id.rv_plants) as RecyclerView
        rv_plants.layoutManager = LinearLayoutManager(context)


        rv_plants.adapter = CustomRecyclerAdapter(names, notes, watering_from_db)




    }


    private fun onClick(v: View) {
        when (v.id) {
            R.id.bt_nav_new -> {
                //Toast.makeText(activity, "Go to settings", Toast.LENGTH_SHORT).show()
                //view?.findNavController()?.navigate(R.id.action_main_screen_to_settings_screen)
                //findNavController().navigate(R.id.action_main_screen_to_settings_screen)

                findNavController().navigate(R.id.action_my_plants_screen_to_new_plants_screen)

            }

           /* R.id.bt_nav_current -> {
                //Toast.makeText(activity, "Go to garden", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_my_plants_screen_to_current_plant_screen)

            }
             */
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment my_plants_screen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            my_plants_screen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}