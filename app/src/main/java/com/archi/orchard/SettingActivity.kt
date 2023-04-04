package com.archi.orchard

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)



        val languages = this.resources.getStringArray(R.array.language_array)

        val spinner: Spinner = findViewById(R.id.sp_lang)


        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                R.layout.spinner_item, languages)
            spinner.adapter = adapter

            adapter.setDropDownViewResource(R.layout.spinner_item_drop)

            var current = Locale.getDefault().language.toString()

            if (current=="ru") {spinner.setSelection(0)}
            else {spinner.setSelection(1)}


            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View?, position: Int, id: Long) {


                    //Toast.makeText(this@SettingActivity,Locale.getDefault().language, Toast.LENGTH_SHORT).show()

                    var lang = "eng"
                    when (position) {
                        0 -> lang = "ru"
                        else -> {
                            lang = "eng"
                        }
                    }


                    //Toast.makeText(this@SettingActivity,"lang=" + lang + " " + "current=" + current, Toast.LENGTH_SHORT).show()

                    if (lang != current) {
                        //Toast.makeText(this@SettingActivity,"not equal", Toast.LENGTH_SHORT).show()

                        var appLocale = LocaleListCompat.forLanguageTags(lang)

                       // Toast.makeText(this@SettingActivity,appLocale.toString(),Toast.LENGTH_SHORT)show()

                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }


                    // var appLocale = LocaleListCompat.forLanguageTags("eng_US")
                    // Toast.makeText(this@SettingActivity,"eng", Toast.LENGTH_SHORT).show()

                    // if (position==0)
                    // {
                    //     appLocale = LocaleListCompat.forLanguageTags("ru_RU")
                    //      Toast.makeText(this@SettingActivity,"ru", Toast.LENGTH_SHORT).show()
                    //  }

                    // AppCompatDelegate.setApplicationLocales(appLocale)

                    // }

                    //Toast.makeText(this@SettingActivity,languages[position] + " " + position, Toast.LENGTH_SHORT).show()

                }


                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }




    }
}



