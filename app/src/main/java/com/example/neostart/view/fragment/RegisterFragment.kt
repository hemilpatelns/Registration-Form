package com.example.neostart.view.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.InputEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.neostart.R
import com.example.neostart.database.UserDatabase
import com.example.neostart.databinding.FragmentRegisterBinding
import com.example.neostart.model.Register
import com.example.neostart.repository.UserRepository
import com.example.neostart.viewmodel.UserViewModel
import com.example.neostart.viewmodel.UserViewModelFactory
import com.google.android.material.imageview.ShapeableImageView
import java.io.File
import java.io.FileOutputStream

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var pos = 0
    private var isPasswordVisible = false
    private lateinit var getImageContent: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var imageUri: Uri
    private val aspectRatio = 1.0f // Desired aspect ratio for cropping

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ActivityResultLauncher instances
        initializeActivityResultLaunchers()

        val toolbar: Toolbar = binding.tbRegister.tbApp
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
            }
        }

        binding.tbRegister.tvToolbarTitle.text = getString(R.string.title_register)

        setViewModel()

        binding.imbShowHide.setOnClickListener {
            if (isPasswordVisible) {
                binding.edtPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imbShowHide.setImageResource(R.drawable.ic_eye_open)
            } else {
                binding.edtPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.imbShowHide.setImageResource(R.drawable.ic_eye_close)
            }
            binding.edtPassword.setSelection(binding.edtPassword.text.length)
            isPasswordVisible = !isPasswordVisible
        }

        binding.btnRegisterNext.setOnClickListener {
            val register = Register(
                firstName = binding.edtFirstName.text.toString(),
                lastName = binding.edtLastName.text.toString(),
                phoneNumber = binding.edtPhoneNumber.text.toString(),
                email = binding.edtEmail.text.toString(),
                gender = when (binding.rgGender.checkedRadioButtonId) {
                    binding.rbMale.id -> "Male"
                    binding.rbFemale.id -> "Female"
                    else -> ""
                },
                password = binding.edtPassword.text.toString(),
                confirmPassword = binding.edtConfirmPassword.text.toString()
            )
            userViewModel.setRegisterData(register)
            findNavController().navigate(R.id.action_registerFragment_to_infoFragment)
        }

        binding.sivEditImage.setOnClickListener {
            showImageSourceDialog()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeActivityResultLaunchers() {
        takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    performCropping(imageUri)
                }
            }

        getImageContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { performCropping(it) }
            }
    }

    private fun performCropping(uri: Uri) {
        val bitmap = loadBitmapFromUri(requireContext(), uri)
        bitmap?.let {
            val cropRect = getCropRect(it, aspectRatio)
            val croppedBitmap = cropBitmap(it, cropRect)
            saveBitmap(requireContext(), croppedBitmap, "cropped_image_${pos}.png")
            binding.sivProfileImage.setImageBitmap(croppedBitmap)
        }
    }

    private fun handleImage(uri: Uri) {
        // This method is no longer needed since `performCropping` handles the image
    }

    private fun createImageUri(): Uri {
        val image = File(requireContext().applicationContext.filesDir, "camera_photo${pos++}.png")
        Log.d("Yoyo", image.path.toString())
        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "com.example.neostart.fragment.fileProvider",
            image
        )
    }

    private fun takeFromCamera() {
        imageUri = createImageUri()
        takePicture.launch(imageUri)
    }

    private fun chooseFromGallery() {
        getImageContent.launch("image/*")
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Image Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> takeFromCamera() // Option to take a photo
                    1 -> chooseFromGallery() // Option to choose from gallery
                }
            }
            .show()
    }

    // Load Bitmap from Uri
    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Calculate the crop rectangle
    private fun getCropRect(bitmap: Bitmap, aspectRatio: Float): Rect {
        val width = bitmap.width
        val height = bitmap.height

        val cropWidth: Int
        val cropHeight: Int

        if (width > height) {
            cropWidth = (height * aspectRatio).toInt()
            cropHeight = height
        } else {
            cropWidth = width
            cropHeight = (width / aspectRatio).toInt()
        }

        val xOffset = (width - cropWidth) / 2
        val yOffset = (height - cropHeight) / 2

        return Rect(xOffset, yOffset, xOffset + cropWidth, yOffset + cropHeight)
    }

    // Crop the Bitmap
    private fun cropBitmap(bitmap: Bitmap, cropRect: Rect): Bitmap {
        return Bitmap.createBitmap(
            bitmap,
            cropRect.left,
            cropRect.top,
            cropRect.width(),
            cropRect.height()
        )
    }

    // Save the Bitmap to a file
    private fun saveBitmap(context: Context, bitmap: Bitmap, fileName: String) {
        try {
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setViewModel() {
        val database = UserDatabase.getDatabase(requireContext())
        val repository =
            UserRepository(database.registerDao(), database.infoDao(), database.addressDao())
        val factory = ViewModelProvider(requireActivity(), UserViewModelFactory(repository))
        userViewModel = factory[UserViewModel::class.java]

        userViewModel.registerData.observe(viewLifecycleOwner) { registerEntity ->
            if (registerEntity != null) {
                binding.edtFirstName.setText(registerEntity.firstName)
                binding.edtLastName.setText(registerEntity.lastName)
                binding.edtPhoneNumber.setText(registerEntity.phoneNumber)
                binding.edtEmail.setText(registerEntity.email)
                when (registerEntity.gender) {
                    "Male" -> binding.rgGender.check(binding.rbMale.id)
                    "Female" -> binding.rgGender.check(binding.rbFemale.id)
                }
                binding.edtPassword.setText(registerEntity.password)
                binding.edtConfirmPassword.setText(registerEntity.confirmPassword)
            }
        }

    }
}

//class RegisterFragment : Fragment() {
//
//    private var _binding: FragmentRegisterBinding? = null
//    private val binding get() = _binding!!
//    private var pos = 0
//    private var isPasswordVisible = false
//    private lateinit var getImageContent: ActivityResultLauncher<String>
//    private lateinit var takePicture: ActivityResultLauncher<Uri>
//    private lateinit var imageUri: Uri
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize ActivityResultLauncher instances
//        initializeActivityResultLaunchers()
//
//        val toolbar: Toolbar = binding.tbRegister.tbApp
//        (activity as AppCompatActivity).apply {
//            setSupportActionBar(toolbar)
//            supportActionBar?.apply {
//                setDisplayShowTitleEnabled(false)
//            }
//        }
//
//        binding.tbRegister.tvToolbarTitle.text = getString(R.string.title_register)
//
//        binding.imbShowHide.setOnClickListener {
//            if (isPasswordVisible) {
//                binding.edtPassword.transformationMethod =
//                    PasswordTransformationMethod.getInstance()
//                binding.imbShowHide.setImageResource(R.drawable.ic_eye_open)
//            } else {
//                binding.edtPassword.transformationMethod =
//                    HideReturnsTransformationMethod.getInstance()
//                binding.imbShowHide.setImageResource(R.drawable.ic_eye_close)
//            }
//            binding.edtPassword.setSelection(binding.edtPassword.text.length)
//            isPasswordVisible = !isPasswordVisible
//        }
//
//        binding.btnRegisterNext.setOnClickListener {
//            findNavController().navigate(R.id.action_registerFragment_to_infoFragment)
//        }
//
//        binding.sivEditImage.setOnClickListener {
//            showImageSourceDialog()
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun initializeActivityResultLaunchers() {
//        takePicture =
//            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
//                if (isSuccess) {
//                    handleImage(imageUri)
//                }
//            }
//
//        getImageContent =
//            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//                uri?.let {
//                    handleImage(it)
//                }
//            }
//    }
//
//    private fun handleImage(uri: Uri) {
//        // Process the image URI (e.g., display it in an ImageView)
//        val imageView: ShapeableImageView = binding.sivProfileImage
//        imageView.setImageURI(uri)
//    }
//
//    private fun createImageUri(): Uri {
//        val image = File(requireContext().applicationContext.filesDir, "camera_photo${pos++}.png")
//        Log.d("Yoyo", image.path.toString())
//        return FileProvider.getUriForFile(
//            requireContext().applicationContext,
//            "com.example.neostart.fragment.fileProvider",
//            image
//        )
//    }
//
//    private fun takeFromCamera() {
//        imageUri = createImageUri()
//        takePicture.launch(imageUri)
//    }
//
//    private fun chooseFromGallery() {
//        getImageContent.launch("image/*")
//    }
//
//    private fun showImageSourceDialog() {
//        val options = arrayOf("Take Photo", "Choose from Gallery")
//        AlertDialog.Builder(requireContext())
//            .setTitle("Select Image Source")
//            .setItems(options) { _, which ->
//                when (which) {
//                    0 -> takeFromCamera() // Option to take a photo
//                    1 -> chooseFromGallery() // Option to choose from gallery
//                }
//            }
//            .show()
//    }
//}

