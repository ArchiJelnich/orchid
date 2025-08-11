package com.example.orchid

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.example.orchid.infra.CheckToMark
import com.example.orchid.infra.localeChecker
import com.example.orchid.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class WidgetProvider : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        CoroutineScope(Dispatchers.IO).launch {
            localeChecker(context)
            CheckToMark(context)
            val db = AppDatabase.getInstance(context)
            val count = db.PlantDao().getCount()
            withContext(Dispatchers.Main) {
                for (widgetId in appWidgetIds) {
                    updateWidget(context, appWidgetManager, widgetId, count)
                }
            }
        }

    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int, count : Int) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_status)
            var hasData = false
            if (count>0)
                {hasData = true}


            val colorRes = if (hasData) R.drawable.circle_shape_green else R.drawable.circle_shape


            remoteViews.setInt(
                R.id.statusCircle,
                "setBackgroundResource",
                colorRes
            )


            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            remoteViews.setOnClickPendingIntent(R.id.statusCircle, pendingIntent)

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }
}