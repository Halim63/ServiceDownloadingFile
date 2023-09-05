package com.halim.downloadfile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.halim.downloadfile.services.DownloadService

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            val intent = Intent(context, DownloadService::class.java)
            context.startForegroundService(intent)
        }

    }
}