package com.example.orchid.infra

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.orchid.R
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
    Log.d("MyDate", "dateString" + dateString)

    return dateString

}


@RequiresApi(Build.VERSION_CODES.O)
fun StringToLocalDate(string: String ): LocalDate {

    val stringDate: List<String> = string.split(".")
    return LocalDate.of(stringDate[2].toInt(), stringDate[1].toInt(), stringDate[0].toInt())

}

@RequiresApi(Build.VERSION_CODES.O)
fun StringToNiceString(string: String, context : Context ): String {

    if (string.isEmpty())
    {
        return ""
    }

    val stringDate: List<String> = string.split(".")
    var date = LocalDate.of(stringDate[2].toInt(), stringDate[1].toInt(), stringDate[0].toInt())

    var strDayOfMonth = date.dayOfMonth.toString()


    if (date.dayOfMonth<10)
    {
        strDayOfMonth = "0" + date.dayOfMonth
    }

    var niceMonth = when (date.month) {
        Month.JANUARY -> context.getString(R.string.JANUARY)
        Month.FEBRUARY -> context.getString(R.string.FEBRUARY)
        Month.MARCH -> context.getString(R.string.MARCH)
        Month.APRIL -> context.getString(R.string.APRIL)
        Month.MAY -> context.getString(R.string.MAY)
        Month.JUNE -> context.getString(R.string.JUNE)
        Month.JULY -> context.getString(R.string.JULY)
        Month.AUGUST -> context.getString(R.string.AUGUST)
        Month.SEPTEMBER -> context.getString(R.string.SEPTEMBER)
        Month.OCTOBER -> context.getString(R.string.OCTOBER)
        Month.NOVEMBER -> context.getString(R.string.NOVEMBER)
        Month.DECEMBER -> context.getString(R.string.DECEMBER)
        else -> TODO()
    }

    Log.d("NiceDate","strDayOfMonth " + strDayOfMonth)
    Log.d("NiceDate","niceMonth " + niceMonth)
    Log.d("NiceDate","date.year " + date.year)
    val niceString = strDayOfMonth + " " + niceMonth + " " + date.year

    return niceString

}