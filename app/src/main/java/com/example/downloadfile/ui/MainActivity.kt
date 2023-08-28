package com.example.downloadfile.ui


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.downloadfile.MyService
import com.example.downloadfile.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeViewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestStoragePermission()
        setUpObserver()

        binding.btnDownload.setOnClickListener {
            homeViewModel.downloadFile()
            foregroundService()

        }
    }

    private fun foregroundService() {
        val intent = Intent(this, MyService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun setUpObserver() {
        homeViewModel.downloadFileLiveData.observe(this) { inputStream ->
            val filePath = cacheDir.absolutePath + "halim${System.currentTimeMillis()}.pdf"
            val saveFilePath = saveFile(inputStream, filePath)
            if (saveFilePath.isEmpty()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()

            }
        }

    }

    private fun saveFile(input: InputStream, pathWhereYouWantToSaveFile: String): String {
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
                publishProgress(Int.MAX_VALUE)
                output.flush()
            }
            return pathWhereYouWantToSaveFile
        } catch (e: Exception) {
            Toast.makeText(this, "error:${e}", Toast.LENGTH_LONG).show()
            Log.e("saveFile", e.toString())
        } finally {
            input.close()
        }
        return ""
    }

    private fun publishProgress(vararg progress: Int) {
        binding.progressBar.progress = progress[0]
    }
    private fun requestStoragePermission() {
        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        {}

}
