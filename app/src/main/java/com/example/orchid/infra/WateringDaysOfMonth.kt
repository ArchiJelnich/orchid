package com.example.orchid.infra

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun wateringDaysOfMonth(plantSubType : String): String {

    if (plantSubType.isEmpty())
    {
        return ""
    }

    val daysOfMonthArray: List<String> = plantSubType.split(",")
    val daysOfMonthInt: List<Int> = daysOfMonthArray.map { it.toInt() }

    var today = LocalDate.now()
    var count = 0
    var todayDayOfMonth = today.dayOfMonth
    var lengthThisMonth = today.lengthOfMonth()
    var lengthNextMonth = today.plusMonths(1).lengthOfMonth()


    for (i in todayDayOfMonth..lengthThisMonth)
    {
        if (daysOfMonthInt.contains(i))
        {
            count = 1
            break
        }

        today = today.plusDays(1)

    }

    if (count!=1)
    {
        for (i in 1..lengthNextMonth)
        {
            if (daysOfMonthInt.contains(i))
            {
                count = 1
                break
            }

            today = today.plusDays(1)

        }}

    if (count!=1)
    {
        today = today.minusDays(1)
    }


    return LocalDateToString(today)

}

