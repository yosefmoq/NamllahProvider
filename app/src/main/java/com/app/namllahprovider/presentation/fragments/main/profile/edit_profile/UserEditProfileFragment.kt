package com.app.namllahprovider.presentation.fragments.main.profile.edit_profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.databinding.FragmentUserEditProfileBinding
import com.app.namllahprovider.presentation.base.BottomSheetInputType
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import com.app.namllahprovider.presentation.fragments.main.profile.edit_profile.dialogs.UserEditPasswordFragment
import com.app.namllahprovider.presentation.utils.FileUtils
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.io.File


@AndroidEntryPoint
class UserEditProfileFragment : Fragment(), View.OnClickListener {

    private var fragmentUserEditProfileBinding: FragmentUserEditProfileBinding? = null
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var userDto: UserDto? = null
    private val SELECT_PHOTO: Int = 143

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserEditProfileBinding =
            FragmentUserEditProfileBinding.inflate(inflater, container, false)
        return fragmentUserEditProfileBinding?.apply {
            actionOnCLick = this@UserEditProfileFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getLoggedProfile()

        observeLiveData()
    }

    private fun observeLiveData() {
        profileViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("observeLiveData : Loading Status $it")
                if (it) {
                    SweetAlert.instance.showAlertDialog(
                        context = requireContext(),
                        alertType = SweetAlertType.PROGRESS_TYPE,
                        title = "Loading",
                        message = "",
                        confirmText = "",
                        confirmListener = {},
                        cancelText = "",
                        cancelListener = {},
                        cancelable = false,
                    )
                } else {
                    SweetAlert.instance.dismissAlertDialog(true)
                }
                profileViewModel.loadingLiveData.postValue(null)
            }
        })

        profileViewModel.errorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(
                    activity = requireActivity(),
                    message = it.message ?: "",
                    isJson = true,
                )
                it.printStackTrace()
            }
        })

        profileViewModel.updateProfileLiveData.observe(viewLifecycleOwner, {
            it?.let { updateUserProfileResponse ->
                Timber.tag(TAG)
                    .d("observeLiveData : updateUserProfileResponse $updateUserProfileResponse")
                if (updateUserProfileResponse.status) {
                    SweetAlert.instance.showSuccessAlert(requireActivity(),"Profile Updated Successfully")
                } else {
                    val errorMessage = updateUserProfileResponse.msg
                        ?: updateUserProfileResponse.error
                        ?: "Something error, Please try again later"
                    profileViewModel.changeErrorMessage(Throwable(errorMessage))
                }
                profileViewModel.updateProfileLiveData.postValue(null)
            }
        })
    }


    private fun getLoggedProfile() {
        profileViewModel.getLoggedUser()
        profileViewModel.getLoggedUserLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("getLoggedProfile : it $it")
                userDto = it
                fragmentUserEditProfileBinding?.userDto = userDto
                profileViewModel.getLoggedUserLiveData.postValue(null)
            }
        })
    }

    private fun initToolbar() {
        val toolbar = fragmentUserEditProfileBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.edit_profile)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "UserEditProfileFragment"
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentUserEditProfileBinding?.clUsername -> onClickEditUsername()
            fragmentUserEditProfileBinding?.clPhoneNumber -> onClickEditPhoneNumber()
            fragmentUserEditProfileBinding?.clPassword -> onClickEditPassword()
            fragmentUserEditProfileBinding?.clAddress -> onClickEditAddress()
            fragmentUserEditProfileBinding?.fabChangeImage -> onClickChangeImage()
            fragmentUserEditProfileBinding?.clNationality -> onClickEditNationality()
        }
    }

    private fun onClickChangeImage() {
        Timber.tag(TAG).d("onClickChangeImage : ")
        if (hasStoragePermission()) {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            someActivityResultLauncher!!.launch(intent)
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.take_image),
                SELECT_PHOTO,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }


    private val someActivityResultLauncher: ActivityResultLauncher<Intent?>? =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent = result.data!!
                fragmentUserEditProfileBinding?.ivServiceImage?.setImageURI(data.data)
                val realPath: String = FileUtils().getPath(requireContext(), data.data!!)!!
                val file = File(realPath)
                val requestBody: RequestBody =
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)

                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("image", file.name, requestBody)
                profileViewModel.updateUserImage(body)
            }
        }

    private fun hasStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun onClickEditUsername() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditBottomSheetFragment(
                title = getString(R.string.change_username_title),
                message = getString(R.string.change_username_message),
                hint = getString(R.string.name),
                currentValue = userDto?.name ?: "",
                inputType = BottomSheetInputType.NAME
            )
        )
    }

    private fun onClickEditPhoneNumber() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditBottomSheetFragment(
                title = getString(R.string.change_phone_number_title),
                message = getString(R.string.change_phone_number_message),
                hint = getString(R.string.phone_number),
                currentValue = userDto?.mobile ?: "",
                inputType = BottomSheetInputType.PHONE
            )
        )
    }

    private fun onClickEditPassword() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditPasswordFragment()
        )
    }

    private fun onClickEditAddress() {
        findNavController().navigate(
            UserEditProfileFragmentDirections.actionUserEditProfileFragmentToUserEditBottomSheetFragment(
                title = getString(R.string.change_address_title),
                message = getString(R.string.change_address_message),
                hint = getString(R.string.address),
                currentValue = userDto?.type ?: "",
                inputType = BottomSheetInputType.ADDRESS
            )
        )
    }

    private fun onClickEditNationality() {
        Timber.tag(TAG).d("onClickEditNationality    : ")
    }
}