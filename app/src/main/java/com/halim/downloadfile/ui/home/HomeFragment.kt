package com.halim.downloadfile.ui.home

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.halim.downloadfile.databinding.FragmentHomeBinding
import com.halim.downloadfile.receivers.DownloadStatusReceiver
import com.halim.downloadfile.service.DownloadService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var downloadStatusReceiver = DownloadStatusReceiver(::onDownloadStatusChange)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }


    private fun setupViews() {
        binding.btnDownload.setOnClickListener {
            startForegroundService()
        }
        binding.stopDownload.setOnClickListener {
            stopService()
        }
    }

    private fun startForegroundService() {
        val intent = Intent(context, DownloadService::class.java)
        context?.startForegroundService(intent)
    }

    private fun stopService() {
        val intent = Intent(requireContext(), DownloadService::class.java)
        context?.stopService(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        registerReceiver()

    }

    override fun onStop() {
        super.onStop()
        unRegisterReceiver()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun registerReceiver() {
        val intentFilter =
            IntentFilter(DownloadStatusReceiver.DOWNLOAD_STATUS_BROAD_CAST_RECEIVER_ACTION)
        activity?.registerReceiver(
            downloadStatusReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED
        )
    }

    private fun unRegisterReceiver() {
        activity?.unregisterReceiver(downloadStatusReceiver)
    }


    private fun onDownloadStatusChange(status: DownloadStatusReceiver.Companion.DownloadStatus) {
        binding.tvStatus.text = status.toString()
    }
}