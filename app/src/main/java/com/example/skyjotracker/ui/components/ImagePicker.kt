package com.example.skyjotracker.ui.components

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.skyjotracker.R
import com.example.skyjotracker.util.ImageStorageHelper

@Composable
fun ImagePickerDialog(show: Boolean, onImageSelected: (Uri) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // Camera launcher using intent - must be called unconditionally
    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                cameraImageUri?.let { uri -> onImageSelected(uri) }
            }
            onDismiss()
        }

    // Gallery launcher using intent - must be called unconditionally
    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Copy the selected image to internal storage
                    ImageStorageHelper.copyImageToInternalStorage(context, uri)?.let { copiedUri
                        ->
                        onImageSelected(copiedUri)
                    }
                }
            }
            onDismiss()
        }

    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.image_picker_title)) },
            text = { Text(stringResource(R.string.image_picker_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraImageUri = ImageStorageHelper.createImageFileUri(context)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri)
                        cameraLauncher.launch(intent)
                    }
                ) { Text(stringResource(R.string.image_picker_camera)) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        val intent =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        galleryLauncher.launch(intent)
                    }
                ) { Text(stringResource(R.string.image_picker_gallery)) }
            }
        )
    }
}
