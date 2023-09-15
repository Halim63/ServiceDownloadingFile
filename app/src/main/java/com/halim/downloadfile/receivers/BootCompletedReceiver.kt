package com.halim.downloadfile.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.halim.downloadfile.service.DownloadService

private const val BOOT_COMPLETED_RECEIVER_ACTION = "android.intent.action.BOOT_COMPLETED"

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (BOOT_COMPLETED_RECEIVER_ACTION == intent.action) {
            val intentService = Intent(context, DownloadService::class.java)
            context.startForegroundService(intentService)

        }

    }
}