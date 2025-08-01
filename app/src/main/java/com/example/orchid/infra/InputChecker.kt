package com.example.orchid.infra

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString
import com.example.orchid.R

fun InputChecker(context: Context, input: String): String {

    var newString = input

    if (newString.isEmpty())
    {
        newString=getString(context, R.string.plant_planty)
    }

    newString = newString.replace("*", "")

    if (newString.length>15)
    {
        newString = newString.take(15)
    }

    return newString
}