package com.gladun.sample

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ImagePickerDialog(
        private val cameraPermissionLauncher: ActivityResultLauncher<String>,
        private val galleryPermissionLauncher: ActivityResultLauncher<String>,
        private val launcher: ActivityResultLauncher<Intent>,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = arrayOf("Open Camera", "Open Gallery")

        return MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme_Center)
                .setIcon(R.drawable.ic_camera)
                .setTitle("Choose image")
                .setItems(items) { _, which ->
                    when (which) {
                        0 -> openCameraForImage(this, cameraPermissionLauncher, launcher)
                        1 -> openGalleryForImage(this, galleryPermissionLauncher, launcher)
                    }
                }.create()
    }
}
