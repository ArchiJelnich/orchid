package com.example.orchid.infra

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateToString(localDate : LocalDate): String {

    var strDayOfMonth = localDate.dayOfMonth.toString()
    var strMonth = 0

    if (localDate.dayOfMonth<10)
    {
        strDayOfMonth = "0" + localDate.dayOfMonth
    }

    strMonth = when (localDate.month) {
        Month.JANUARY -> 1
        Month.FEBRUARY -> 2
        Month.MARCH -> 3
        Month.APRIL -> 4
        Month.MAY -> 5
        Month.JUNE -> 6
        Month.JULY -> 7
        Month.AUGUST -> 8
        Month.SEPTEMBER -> 9
        Month.OCTOBER -> 10
        Month.NOVEMBER -> 11
        Month.DECEMBER -> 12
         else -> TODO()
    }


    val dateString = strDayOfMonth + "." + strMonth + "." + localDate.year


    return dateString

}