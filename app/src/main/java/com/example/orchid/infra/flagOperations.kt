package com.example.orchid.infra

import android.content.Context
import android.preference.PreferenceManager

fun flagPut(context: Context, value : Int)  {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putInt("edit_flag", value).apply()
}

fun flagPutExtra(context: Context, value : Int)  {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.edit().putInt("edit_flag_extra", value).apply()
}

fun flagGetExtra(context: Context): Int {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editFlag = preferences.getInt("edit_flag_extra", 0)
    return editFlag
}

fun flagGet(context: Context): Int {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editFlag = preferences.getInt("edit_flag", 0)
    return editFlag
}

/// 0 - no action
/// 100 - plant was created
/// 102 - plant edit
/// 202 - plant edit photo