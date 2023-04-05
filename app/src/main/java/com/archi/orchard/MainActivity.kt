package com.archi.orchard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.archi.orchard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }


}


/*
Roadmap:

! Debug problem with language change

! Make back navigation better

Adding of new plants:
add image loading
add image cropping

Main functional:
show watering for today
turn background on plants what need watering red (in plants + in main)
recalculate after watering
hide if user chose hide

Edit of plant:
recalculate if change watering
change attributes
delete + delete all history

Yandex api: weather based on Town

Notification by time and every hour
Chose town

Create room db fow watering history
Plant history with calendar
Red and green days

 */