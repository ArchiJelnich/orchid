package com.example.orchid.infra

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
fun wateringDaysWeek(plantSubType : String): String {

    if (plantSubType.isEmpty())
    {
        return ""
    }

    val daysOfWeekArray: List<String> = plantSubType.split(",")
    val daysOfWeekInt: List<Int> = daysOfWeekArray.map { it.toInt() }



    var today = LocalDate.now()
    var count = 0



    var todayDayOfWeek = 0

    todayDayOfWeek = when (today.dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
        else -> TODO("A new day was added to the week?")
    }






        for (i in todayDayOfWeek..6)
        {
            if (daysOfWeekInt.contains(i))
            {
                count = 1
                break
            }

            today = today.plusDays(1)

        }

    if (count!=1)
    {
    for (i in 0..todayDayOfWeek)
    {
        if (daysOfWeekInt.contains(i))
        {
            count = 1
            break
        }

        today = today.plusDays(1)

    }}

    return LocalDateToString(today)

    }



