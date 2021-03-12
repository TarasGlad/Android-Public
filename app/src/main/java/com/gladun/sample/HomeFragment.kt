package com.gladun.sample

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.gladun.sample.databinding.FragmentHomeBinding
import java.io.InputStream

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val requestCameraPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) createCameraIntent(imageLauncher)
            else
                Toast.makeText(
                    requireContext(), "Storage Permission Explanation", Toast.LENGTH_LONG
                ).show()
        }

    private val requestGalleryPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) createGalleryIntent(imageLauncher)
            else Toast.makeText(
                requireContext(), "Storage Permission Explanation", Toast.LENGTH_LONG
            ).show()
        }

    private var imageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val hasExtraData = result.data?.hasExtra("data")
                hasExtraData?.let { hasData ->
                    if (hasData) {
                        val image: Bitmap = result.data?.extras?.get("data") as Bitmap
                        binding.ivImage.setImageBitmap(image)
                    } else {
                        val imageUri: Uri? = result.data?.data
                        val inputStream: InputStream? = imageUri?.let { uri ->
                            context?.contentResolver?.openInputStream(uri)
                        }
                        val image: Bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.ivImage.setImageBitmap(image)
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.btnChooseImage.setOnClickListener {
            ImagePickerDialog(
                requestCameraPermissionLauncher, requestGalleryPermissionLauncher, imageLauncher
            ).show(requireActivity().supportFragmentManager, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
