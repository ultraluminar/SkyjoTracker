package com.example.skyjotracker.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageStorageHelper {

    /** Create temporary file URI for camera capture using FileProvider */
    fun createImageFileUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "SKYJO_${timeStamp}.jpg"
        val storageDir = File(context.filesDir, "images")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val imageFile = File(storageDir, imageFileName)

        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    }

    /**
     * Copy an image from a URI to app's internal storage
     * @return The URI of the copied file, or null if copy failed
     */
    fun copyImageToInternalStorage(context: Context, sourceUri: Uri): Uri? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "SKYJO_${timeStamp}.jpg"
            val storageDir = File(context.filesDir, "images")
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            val destinationFile = File(storageDir, imageFileName)

            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(destinationFile).use { output -> input.copyTo(output) }
            }

            Uri.fromFile(destinationFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

//    /** Get the directory where images are stored */
//    fun getImagesDirectory(context: Context): File {
//        val storageDir = File(context.filesDir, "images")
//        if (!storageDir.exists()) {
//            storageDir.mkdirs()
//        }
//        return storageDir
//    }
}
