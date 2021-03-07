package tj.behruz.elon.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.header_layout.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import tj.behruz.elon.BuildConfig
import tj.behruz.elon.R
import tj.behruz.elon.databinding.FragmentProfileBinding
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils
import tj.behruz.elon.repositories.HomeRepository
import tj.behruz.elon.ui.ChooseFileBottomSheet
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment() : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var bitmap: Bitmap
    private val gallery = 1
    private val cameraRequest = 2
    private var mCurrentPhotoPath = ""
    private var photoFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val token = PreferenceHelper.getToken(requireContext())
        val number =
            Utils.getUnMaskedNumber(PreferenceHelper.getNumberPhone(requireContext()).toString())

        (activity as AppCompatActivity).supportActionBar?.show()

        try {
            binding.shimmerViewContainer.startShimmerAnimation()
            token.let {

                val currentNumber = number.plus("avitootiva").plus(number)

                viewModel.getUserInfo(number, Utils.hashing(currentNumber).toString())
                    .observe(viewLifecycleOwner, Observer { response ->
                        val userPayload = response.payload!!
                        if (response.code == 200) {

                            if (userPayload.code == 6) {
                                binding.shimmerViewContainer.stopShimmerAnimation()
                                binding.shimmerViewContainer.visibility = View.GONE
                                binding.profileLayout.visibility = View.VISIBLE
                                val fullName = userPayload.data.full_name.split(" ")

                                binding.username.setText(fullName[0])
                                binding.surname.setText(fullName[1])

                                Picasso.get().load(userPayload.data.img_src).placeholder(R.drawable.ic_user)
                                    .error(R.drawable.ic_user).noFade().into(binding.userAvatar)

                            } else {
                                Utils.showErrorMessage(binding.surname, userPayload.message)

                            }
                        } else {
                            Utils.showErrorMessage(binding.surname, "неизвестная ошибка")
                        }

                    })


            }

            //chooseImageHandler
            binding.changeImage.setOnClickListener {

                val bottomSheet = ChooseFileBottomSheet() { handler(it) }
                bottomSheet.show(this.parentFragmentManager, "picture")
            }
            //save profile handler
            binding.saveProfile.setOnClickListener {
                //parsing date to formdata
                Utils.showProgressDialog(requireContext(), true)
                val name =
                    binding.username.text.toString().plus(" ").plus(binding.surname.text.toString())
                val fullName = name.toRequestBody(MultipartBody.FORM)
                val userToken = token.toString().toRequestBody(MultipartBody.FORM)
                val action = "home".toRequestBody(MultipartBody.FORM)
                val method = "editProfile".toRequestBody(MultipartBody.FORM)

                if (mCurrentPhotoPath.isEmpty()) {
                    viewModel.uploadImage(action, fullName, userToken, method)
                        .observe(viewLifecycleOwner, Observer {
                            Utils.showProgressDialog(requireContext(), false)

                            Utils.showSnackBar(binding.profileLayout, it.message)
                        })
                } else {
                    val image = compression(mCurrentPhotoPath)
                    val fileReqBody = image.asRequestBody("image/*".toMediaTypeOrNull())
                    val part =
                        MultipartBody.Part.createFormData("avatar", image.name, fileReqBody)
                    viewModel.editProfile(action, fullName, userToken, method, part)
                        .observe(viewLifecycleOwner, Observer {

                            Utils.showProgressDialog(requireContext(), false)
                            Utils.showSnackBar(binding.profileLayout, it.message)
                        })
                }
            }

            //handling error
            viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
                Utils.showErrorMessage(binding.changeImage, error)

            })

        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    //handeling image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == cameraRequest && resultCode == Activity.RESULT_OK) {
            val file = File(mCurrentPhotoPath)
            binding.userAvatar.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        } else if (requestCode == gallery && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mCurrentPhotoPath = getRealPathFromURI(requireContext(), uri!!)
                val file = File(mCurrentPhotoPath)
                binding.userAvatar.setImageBitmap(BitmapFactory.decodeFile(file.path));
            } else {

                if (uri != null) {
                    mCurrentPhotoPath = getRealPathFromURI(requireContext(), uri)
                    val file = File(mCurrentPhotoPath)
                    binding.userAvatar.setImageBitmap(BitmapFactory.decodeFile(file.path));

                }
            }

        }

    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } catch (e: Exception) {
            ""
        } finally {
            cursor?.run { close() }
        }
    }


    //compressing image for sending to api
    private fun compression(path: String): File {
        val file = File(path)
        return Compressor(context).setMaxWidth(200).setMaxHeight(200).setQuality(25)
            .compressToFile(file)
    }


    //choose photo from gallery
    private fun permissionForGallery() {

        Dexter.withActivity(activity).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val galleryIntent =
                        Intent(
                            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                    if (galleryIntent.resolveActivity(context?.packageManager!!) != null) {
                        startActivityForResult(galleryIntent, gallery)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
//                    AlifAlert.permissionDenied(
//                        context!!,
//                        getString(R.string.ord_cour_need_permission),
//                        getString(R.string.ord_cour_need_permission_for_use),
//                        getString(R.string.cancel),
//                        getString(R.string.ok),
//                        token!!
//                    )
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
//                    Toast.makeText(
//                        context,
//                        getString(R.string.ord_cour_permission_denied),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }).check()
    }

    private fun permissionForCamera() {
        Dexter.withActivity(activity).withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    if (cameraIntent.resolveActivity(context?.packageManager!!) != null) {
                        // Create the File where the photo should go
                        try {
                            photoFile = createImageFile()
                        } catch (ex: IOException) {
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            val photoURI = FileProvider.getUriForFile(
                                requireContext(),
                                "${BuildConfig.APPLICATION_ID}.provider",
                                photoFile!!
                            )
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(cameraIntent, cameraRequest)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                }
            }).check()
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "Iden_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    private fun handler(type: Int) {
        when (type) {
            1 -> {
                permissionForCamera()
            }
            2 -> {
                permissionForGallery()
            }
        }
    }
}