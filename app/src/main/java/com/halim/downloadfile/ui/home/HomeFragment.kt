package com.halim.downloadfile.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.downloadfile.R
import com.example.downloadfile.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.halim.downloadfile.services.DownloadService
import com.halim.downloadfile.State
import com.halim.downloadfile.extensions.gone
import com.halim.downloadfile.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel by viewModels<HomeViewModel>()
    lateinit var state: State
    private lateinit var binding: FragmentHomeBinding

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
        setUpDownloadBookObserver()
        binding.btnDownload.setOnClickListener {
            homeViewModel.downloadBook()
            startForegroundService()


        }
        binding.stopDownload.setOnClickListener {
            stopService()
        }


    }


    private fun stopService() {
        val intent = Intent(requireContext(), DownloadService::class.java)
        context?.stopService(intent)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            homeViewModel.downloadBookLiveData.observe(viewLifecycleOwner) { result ->
                when (result.state) {
                    State.LOADING ->  binding.tvStatus.text = State.LOADING.toString()
                    State.SUCCESS -> binding.tvStatus.text = State.SUCCESS.toString()
                    State.ERROR -> binding.tvStatus.text = State.ERROR.toString()
                }
            }

        }

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
        val intentFilter = IntentFilter("com.halim.EXAMPLE_ACTION")
        activity?.registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
    }

    private fun unRegisterReceiver() {
        activity?.unregisterReceiver(broadcastReceiver)
    }


    private fun startForegroundService() {
        val intent = Intent(context, DownloadService::class.java)
        context?.startForegroundService(intent)

    }

    private fun setUpDownloadBookObserver() {
        homeViewModel.downloadBookLiveData.observe(viewLifecycleOwner) { result ->
            when (result.state) {
                State.LOADING -> onDownloadingBookLoading()
                State.SUCCESS -> onDownloadingBookSuccess(result.result)
                State.ERROR -> onDownloadingBookError(result.errorMessage)
            }
        }


    }

//    private fun getFilePath(): String =
//        activity?.cacheDir?.absolutePath + "halim${System.currentTimeMillis()}.pdf"

    private fun onDownloadingBookSuccess(inputStream: InputStream?) {
        binding.progressBar.gone()
        binding.btnDownload.isEnabled = true
        if (inputStream == null) {
            showSnackBarMessage(getString(R.string.something_went_wrong))
            return
        }
//        val isFileSaved = saveFile(inputStream, getFilePath())

//        showSnackBarMessage(
//            message = getString(if (isFileSaved) R.string.success else R.string.error)
//        )

    }

    private fun onDownloadingBookLoading() {
        binding.progressBar.visible()
        binding.btnDownload.isEnabled = false
    }

    private fun onDownloadingBookError(errorMessage: String?) {
        binding.progressBar.gone()
        binding.btnDownload.isEnabled = true
        showSnackBarMessage(errorMessage ?: getString(R.string.something_went_wrong))


    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(
            binding.root, message, Snackbar.LENGTH_LONG
        ).show()
    }

//    private fun saveFile(input: InputStream, pathWhereYouWantToSaveFile: String): Boolean {
//        try {
//            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
//            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
//            fos.use { output ->
//                val buffer = ByteArray(4 * 1024) // or other buffer size
//                var read = 0
//                while (input.read(buffer).also {
//                        read = it
//                    } != -1) {
//                    output.write(buffer, 0, read)
//                }
//                output.flush()
//            }
//            return true
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            input.close()
//        }
//        return false
//    }


}