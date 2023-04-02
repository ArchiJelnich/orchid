package com.archi.orchard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.archi.orchard.databinding.ActivityMainBinding
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [main_screen.newInstance] factory method to
 * create an instance of this fragment.
 */
class main_screen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment








        return inflater.inflate(R.layout.fragment_main_screen, container, false)




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bt_settings = view.findViewById<View>(R.id.bt_nav_settings) as ImageView
        val bt_garden = view.findViewById<View>(R.id.bt_nav_garden) as TextView

        bt_settings.setOnClickListener { onClick(bt_settings) }
        bt_garden.setOnClickListener { onClick(bt_garden) }
    }

    private fun onClick(v: View) {
        when (v.id) {
            R.id.bt_nav_settings -> {
                //Toast.makeText(activity, "Go to settings", Toast.LENGTH_SHORT).show()
                //view?.findNavController()?.navigate(R.id.action_main_screen_to_settings_screen)
                //findNavController().navigate(R.id.action_main_screen_to_settings_screen)

                findNavController().navigate(R.id.action_main_screen_to_settings_screen)

            }

            R.id.bt_nav_garden -> {
                //Toast.makeText(activity, "Go to garden", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_main_screen_to_my_plants_screen)

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
         * @return A new instance of fragment main_screen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            main_screen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}