package com.example.orchid.infra

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun wateringDaysAfter(plantSubType : String, localDate : LocalDate): String {

    if (plantSubType.isEmpty())
    {
        return ""
    }

    var today = localDate

    today = today.plusDays(plantSubType.toLong())

    Log.d("MyAfterDebug", "LocalDateToString(today) " + LocalDateToString(today))
    return LocalDateToString(today)

}
