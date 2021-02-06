package com.example.shoppinglist2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver: BroadcastReceiver() {

    private var id = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        createChannel(context)
        val geoEvent = GeofencingEvent.fromIntent(intent)
        for (geo in geoEvent.triggeringGeofences){
            Toast.makeText(context, "${geo.requestId}", Toast.LENGTH_SHORT).show()
        }
        if(geoEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            val noti = NotificationCompat.Builder(context, context.getString(R.string.channelID))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("You entered the store location")
                    .setAutoCancel(true)
                    .build()

            NotificationManagerCompat.from(context).notify(id++, noti)
        }else if(geoEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
            Log.i("geofence", "Wyjście: ${geoEvent.triggeringLocation.toString()}")
        }else{
            Log.e("geofence", "Error.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(context: Context){
        val notiChannel = NotificationChannel(
                context.getString(R.string.channelID),
                context.getString(R.string.channelName),
                NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(notiChannel)
    }
}