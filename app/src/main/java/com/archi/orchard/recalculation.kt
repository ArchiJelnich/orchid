package com.archi.orchard

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.Room
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.coroutines.coroutineContext

@RequiresApi(Build.VERSION_CODES.O)
fun recalc (watertype: Int): String {
               val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
               val current_date = LocalDate.now().format(formatter)
               var next_date = current_date.toString()
               var day_of_week = LocalDate.now().dayOfWeek
               //Log.d("MyLog", day_of_week.toString())
    when (watertype) {
                    0 -> {
                         next_date = LocalDate.now().plusDays(1).format(formatter)
                    }
                    1 -> {
                        next_date = LocalDate.now().plusDays(2).format(formatter)
                    }
                    2 -> {
                        next_date = LocalDate.now().plusDays(3).format(formatter)
                    }
                    3 ->
                        {
                            when (day_of_week) {
                                DayOfWeek.SUNDAY -> {
                                    next_date = LocalDate.now().plusDays(0).format(formatter)
                                }
                                DayOfWeek.MONDAY -> {
                                    next_date = LocalDate.now().plusDays(1).format(formatter)
                                }
                                DayOfWeek.TUESDAY -> {
                                    next_date = LocalDate.now().plusDays(2).format(formatter)
                                }
                                DayOfWeek.WEDNESDAY -> {
                                    next_date = LocalDate.now().plusDays(3).format(formatter)
                                }
                                DayOfWeek.THURSDAY -> {
                                    next_date = LocalDate.now().plusDays(4).format(formatter)
                                }
                                DayOfWeek.FRIDAY -> {
                                    next_date = LocalDate.now().plusDays(5).format(formatter)
                                }
                                DayOfWeek.SATURDAY -> {
                                    next_date = LocalDate.now().plusDays(6).format(formatter)
                                }
                                else -> {
                                    Log.d("MyLog", "date error")
                                }
                            }

                        }






                    4 -> {
                        when (day_of_week) {
                            DayOfWeek.SUNDAY -> {
                                next_date = LocalDate.now().plusDays(1).format(formatter)
                            }
                            DayOfWeek.MONDAY -> {
                                next_date = LocalDate.now().plusDays(2).format(formatter)
                            }
                            DayOfWeek.TUESDAY -> {
                                next_date = LocalDate.now().plusDays(3).format(formatter)
                            }
                            DayOfWeek.WEDNESDAY -> {
                                next_date = LocalDate.now().plusDays(4).format(formatter)
                            }
                            DayOfWeek.THURSDAY -> {
                                next_date = LocalDate.now().plusDays(5).format(formatter)
                            }
                            DayOfWeek.FRIDAY -> {
                                next_date = LocalDate.now().plusDays(6).format(formatter)
                            }
                            DayOfWeek.SATURDAY -> {
                                next_date = LocalDate.now().plusDays(0).format(formatter)
                            }
                            else -> {
                                Log.d("MyLog", "date error")
                            }
                        }

                    }
                    else -> {
                        when (day_of_week) {
                            DayOfWeek.SUNDAY -> {
                                next_date = LocalDate.now().plusDays(8).format(formatter)
                            }
                            DayOfWeek.MONDAY -> {
                                next_date = LocalDate.now().plusDays(9).format(formatter)
                            }
                            DayOfWeek.TUESDAY -> {
                                next_date = LocalDate.now().plusDays(10).format(formatter)
                            }
                            DayOfWeek.WEDNESDAY -> {
                                next_date = LocalDate.now().plusDays(11).format(formatter)
                            }
                            DayOfWeek.THURSDAY -> {
                                next_date = LocalDate.now().plusDays(12).format(formatter)
                            }
                            DayOfWeek.FRIDAY -> {
                                next_date = LocalDate.now().plusDays(13).format(formatter)
                            }
                            DayOfWeek.SATURDAY -> {
                                next_date = LocalDate.now().plusDays(7).format(formatter)
                            }
                            else -> {
                                Log.d("MyLog", "date error")
                            }
                        }

                    }
                }


    return next_date

}


@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
fun check_date(nextDate: String): Int {

    var result = 0

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val current_date = LocalDate.now().format(formatter)



    val split_current = current_date.split('/')
    val split_next = nextDate.split('/')


        if (split_current[2]>split_next[2]) {result=-1}
        if (split_current[2]>=split_next[2] && split_current[1]>split_next[1]) {result=-1}
        if (split_current[2]>=split_next[2] && split_current[1]>=split_next[1] && split_current[0]>split_next[0]) {result=-1}
        if (split_current[2]==split_next[2] && split_current[1]==split_next[1] && split_current[0]==split_next[0]) {result=1}




    return result

    }



