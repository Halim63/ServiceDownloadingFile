package com.halim.downloadfile.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadStatusReceiver(
    private val onStatusChanged: (DownloadStatus) -> Unit,
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
            intent?.extras?.getString(DOWNLOAD_STATUS_ARG)?.let { status ->
                when (status) {
                    DownloadStatus.LOADING.toString() -> onStatusChanged(DownloadStatus.LOADING)
                    DownloadStatus.SUCCESS.toString() -> onStatusChanged(DownloadStatus.SUCCESS)
                    DownloadStatus.ERROR.toString() -> onStatusChanged(DownloadStatus.ERROR)
                }
            }
    }

    companion object {
        const val DOWNLOAD_STATUS_BROAD_CAST_RECEIVER_ACTION =
            "com.halim.downloadfile.download_status"
        const val DOWNLOAD_STATUS_ARG = "download_status"

        enum class DownloadStatus {
            LOADING, SUCCESS, ERROR
        }
    }
}