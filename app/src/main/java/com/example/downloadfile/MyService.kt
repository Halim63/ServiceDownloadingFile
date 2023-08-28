package com.example.downloadfile

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.downloadfile.ui.MainActivity

class MyService : Service() {
    private val CHANNEL_ID="x_channelId"

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this,"service Created",Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this,"service Started",Toast.LENGTH_SHORT).show()
        startForeground(1,getNotification())
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"service Destroyed",Toast.LENGTH_SHORT).show()

    }
    private fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Channel display name",
                NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
        val intent=Intent(this, MainActivity::class.java)
        val pendingIntent= PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_MUTABLE)
        val  builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("my title")
            .setContentText("plah lplfplflf dfffff")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_background,"Replay",pendingIntent)
        return builder.build()
    }
}