package com.gladun.sample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

fun openGalleryForImage(
    fragment: Fragment,
    galleryPermissionLauncher: ActivityResultLauncher<String>,
    launcher: ActivityResultLauncher<Intent>,
) = when {
    isStoragePermissionsGranted(fragment) -> createGalleryIntent(launcher)
    fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) ->
        Toast.makeText(
            fragment.requireContext(), "Storage Permission Explanation", Toast.LENGTH_LONG
        ).show()
    else -> requestStoragePermission(galleryPermissionLauncher)
}

fun createGalleryIntent(launcher: ActivityResultLauncher<Intent>) {
    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
        type = "image/*"
    }.also { launcher.launch(it) }
}

private fun isStoragePermissionsGranted(fragment: Fragment) = ActivityCompat
    .checkSelfPermission(
        fragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

private fun requestStoragePermission(permissionLauncher: ActivityResultLauncher<String>) =
    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

fun openCameraForImage(
    fragment: Fragment,
    cameraPermissionLauncher: ActivityResultLauncher<String>,
    launcher: ActivityResultLauncher<Intent>,
) = when {
    isCameraPermissionGranted(fragment) -> createCameraIntent(launcher)
    fragment.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ->
        Toast.makeText(
            fragment.requireContext(), "Camera Permission Explanation", Toast.LENGTH_LONG
        ).show()
    else -> requestCameraPermission(cameraPermissionLauncher)
}

fun createCameraIntent(launcher: ActivityResultLauncher<Intent>) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { launcher.launch(it) }
}

private fun isCameraPermissionGranted(fragment: Fragment): Boolean = ActivityCompat
    .checkSelfPermission(
        fragment.requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

private fun requestCameraPermission(permissionLauncher: ActivityResultLauncher<String>) =
    permissionLauncher.launch(Manifest.permission.CAMERA)
