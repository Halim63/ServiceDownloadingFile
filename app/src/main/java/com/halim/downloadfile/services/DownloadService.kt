package com.halim.downloadfile.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.downloadfile.R
import com.halim.downloadfile.Resource
import com.halim.downloadfile.State
import com.halim.downloadfile.repository.books.BookRepo
import com.halim.downloadfile.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class DownloadService : Service() {
    private val STOP_SERVICE = "STOP SERVICE"
    private val CHANNEL_ID = "x_channelId"
    private lateinit var input: InputStream
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var bookRepo: BookRepo
    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "service Created", Toast.LENGTH_SHORT).show()

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service Started", Toast.LENGTH_SHORT).show()
        startForeground(1, getNotification())
        if (STOP_SERVICE == intent.action) {
            stopSelf()
        }
        sendBroadCast()

        coroutineScope.launch {
            if (::bookRepo.isInitialized) {
                try {
                    val response = bookRepo.downloadBook(
                        bookUrl = "https://www.hsbc.co.uk/content/dam/hsbc/gb/pdf/hsbcuk-how-to-view-and-download-statements.pdf"
                    )
                    if (response.isSuccessful && response.body()?.byteStream() != null) {
                        saveFile(getFilePath())
                        Toast.makeText(this@DownloadService, R.string.success, Toast.LENGTH_SHORT)
                            .show()
                        stopSelf()

                    } else {
                        Toast.makeText(this@DownloadService, R.string.error, Toast.LENGTH_SHORT)
                            .show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()

                    Toast.makeText(this@DownloadService, R.string.error, Toast.LENGTH_SHORT).show()
                }

            }
        }

        return START_NOT_STICKY
    }

    private fun sendBroadCast() {
        val intent = Intent("com.halim.EXAMPLE_ACTION")
        sendBroadcast(intent)
    }


    private fun getFilePath(): String =
        cacheDir?.absolutePath + "halim${System.currentTimeMillis()}.pdf"

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "service Destroyed", Toast.LENGTH_SHORT).show()

    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
        Toast.makeText(this, "service Stoped", Toast.LENGTH_SHORT).show()

    }

    private fun saveFile(pathWhereYouWantToSaveFile: String): Boolean {
        try {
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read = 0
                while (input.read(buffer).also {
                        read = it
                    } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            input.close()
        }
        return false
    }

    private fun getNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Channel display name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val stopService = Intent(this, DownloadService::class.java)
        stopService.action = this.STOP_SERVICE
        val pStopService = PendingIntent.getService(
            this, 0, stopService, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading")
            .setContentText("................")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.baseline_stop_24, "Stop", pStopService)
        return builder.build()
    }
}