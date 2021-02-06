package com.example.shoppinglist2


import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable

var imgToggle = 0
var audioStreamId = 0

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val soundPool = SoundPool.Builder()
        .setAudioAttributes(audioAttributes)
        .setMaxStreams(1)
        .build()

val prio = 1
val leftVol = 0.85F
val rightVol = 0.9F
val loop = 0
val speed = 1F
var sampleName = R.raw.short_sound

/**
 * Implementation of App Widget functionality.
 */
class MediaAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        val mgr: AppWidgetManager = AppWidgetManager.getInstance(context)
        val views = RemoteViews(context.packageName, R.layout.media_app_widget)

        val sampleId = soundPool.load(context, sampleName, prio)
        soundPool.setOnLoadCompleteListener{soundPool, sampleId, status ->
            if(intent?.action == context.getString(R.string.playAction)){
                if(audioStreamId != 0){
                    soundPool.resume(audioStreamId)
                }else{
                    val streamId = soundPool.play(sampleId, leftVol, rightVol, prio, loop, speed)
                    audioStreamId = streamId
                }
                mgr.updateAppWidget(intent.getIntExtra("appWidgetId", 0), views)
            }else if(intent?.action == context.getString(R.string.pauseAction)){
                soundPool.pause(audioStreamId)
                mgr.updateAppWidget(intent.getIntExtra("appWidgetId", 0), views)
            }else if(intent?.action == context.getString(R.string.stopAction)){
                soundPool.stop(audioStreamId)
                audioStreamId = 0
                mgr.updateAppWidget(intent.getIntExtra("appWidgetId", 0), views)
            }
        }

        if(intent?.action == context.getString(R.string.nextAction)){
            audioStreamId = 0
            if (sampleName == R.raw.short_sound) {
                sampleName = R.raw.file_example
            } else {
                sampleName = R.raw.short_sound
            }
        }


        if (intent?.action == context.getString(R.string.imageAction)) {
            Toast.makeText(context, "Spaghett", Toast.LENGTH_SHORT).show()

            if (imgToggle == 0) {
                imgToggle = 1
                views.setImageViewResource(R.id.imageView, R.drawable.spagett2)
            } else if (imgToggle == 1) {
                imgToggle = 0
                views.setImageViewResource(R.id.imageView, R.drawable.spagett)
            }
            mgr.updateAppWidget(intent.getIntExtra("appWidgetId", 0), views)
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    val views = RemoteViews(context.packageName, R.layout.media_app_widget)
    views.setTextViewText(R.id.widget_tv, widgetText)

    val intentWWW = Intent(Intent.ACTION_VIEW)
    intentWWW.data = Uri.parse("https://google.com")
    val pendingWWW = PendingIntent.getActivity(
            context,
            0,
            intentWWW,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt1, pendingWWW)

    val intentImage = Intent(context.getString(R.string.imageAction))
    intentImage.component = ComponentName(context, MediaAppWidget::class.java)
    intentImage.putExtra("appWidgetId", appWidgetId)

    val pendingImage = PendingIntent.getBroadcast(
            context,
            0,
            intentImage,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.imageView, pendingImage)

    val intentPlay = Intent(context.getString(R.string.playAction))
    intentPlay.component = ComponentName(context, MediaAppWidget::class.java)
    intentPlay.putExtra("appWidgetId", appWidgetId)

    val pendingPlay = PendingIntent.getBroadcast(
            context,
            0,
            intentPlay,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt2, pendingPlay)

    val intentPause = Intent(context.getString(R.string.pauseAction))
    intentPause.component = ComponentName(context, MediaAppWidget::class.java)
    intentPause.putExtra("appWidgetId", appWidgetId)
    val pendingPause = PendingIntent.getBroadcast(
            context,
            0,
            intentPause,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt3, pendingPause)

    val intentStop = Intent(context.getString(R.string.stopAction))
    intentStop.component = ComponentName(context, MediaAppWidget::class.java)
    intentStop.putExtra("appWidgetId", appWidgetId)
    val pendingStop = PendingIntent.getBroadcast(
            context,
            0,
            intentStop,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt4, pendingStop)

    val intentNext = Intent(context.getString(R.string.nextAction))
    intentNext.component = ComponentName(context, MediaAppWidget::class.java)
    intentNext.putExtra("appWidgetId", appWidgetId)
    val pendingNext = PendingIntent.getBroadcast(
            context,
            0,
            intentNext,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    views.setOnClickPendingIntent(R.id.widget_bt5, pendingNext)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}