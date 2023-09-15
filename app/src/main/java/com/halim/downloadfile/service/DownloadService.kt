package com.halim.downloadfile.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.halim.downloadfile.R
import com.halim.downloadfile.receivers.DownloadStatusReceiver
import com.halim.downloadfile.repository.books.BookRepo
import com.halim.downloadfile.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject


@AndroidEntryPoint
class DownloadService : Service() {
    private val CHANNEL_ID = "x_channelId"
    private val compositeDisposable = CompositeDisposable()


    @Inject
    lateinit var bookRepo: BookRepo


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("CheckResult")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(1, getNotification())
        updateDownloadStatus(DownloadStatusReceiver.Companion.DownloadStatus.LOADING.toString())

        fun onDownloadFileResponse(response: Response<ResponseBody>) {
            if (response.isSuccessful && response.body()?.byteStream() != null) {

                saveFile(getFilePath(), response.body()?.byteStream()!!)
                updateDownloadStatus(
                    status = DownloadStatusReceiver.Companion.DownloadStatus.SUCCESS.toString()
                )

                stopDownloadService()

            } else {
                updateDownloadStatus(
                    status = DownloadStatusReceiver.Companion.DownloadStatus.ERROR.toString()
                )
            }


        }

        val observable = bookRepo.multipleDownload()


        compositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .flatMapIterable { booksResponse ->
                    return@flatMapIterable booksResponse
                }


                .observeOn(AndroidSchedulers.mainThread())

                .flatMap { book ->

                    return@flatMap bookRepo.downloadBook(book.fileUrl)

                }
                .flatMap { response ->
                    onDownloadFileResponse(response)

                    return@flatMap Observable.just(true)
                }
                .toList()

                .subscribeBy(
                    onError = {
                        updateDownloadStatus(
                            status = DownloadStatusReceiver.Companion.DownloadStatus.ERROR.toString()
                        )
                    }
                )
        )



        return START_STICKY
    }


    private fun updateDownloadStatus(status: String) {
        val intent = Intent(DownloadStatusReceiver.DOWNLOAD_STATUS_BROAD_CAST_RECEIVER_ACTION)
        intent.putExtra(DownloadStatusReceiver.DOWNLOAD_STATUS_ARG, status)
        sendBroadcast(intent)

    }

    private fun stopDownloadService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


    private fun getFilePath(): String =
        cacheDir?.absolutePath + "halim${System.currentTimeMillis()}.pdf"


    private fun saveFile(path: String, input: InputStream?): Boolean {
        try {
            val fileOutputStream = FileOutputStream(path)
            fileOutputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read = 0
                while (input?.read(buffer)?.also {
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
            input?.close()
        }
        return false
    }

    private fun getNotification(): Notification {
        val channel = NotificationChannel(
            CHANNEL_ID,
            R.string.notification_channel_name.toString(),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(R.string.notification_title.toString())
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent)
        return builder.build()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


}