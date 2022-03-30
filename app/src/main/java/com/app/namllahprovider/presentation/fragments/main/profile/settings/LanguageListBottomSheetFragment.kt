package com.app.namllahprovider.presentation.fragments.main.profile.settings

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentLanguageListBottomSheetBinding
import com.app.namllahprovider.presentation.MainActivity
import com.app.namllahprovider.presentation.base.ContextUtils
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class LanguageListBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var fragmentUserEditBottomSheetBinding: FragmentLanguageListBottomSheetBinding? = null

    private var currentLanguage: Int = -1
    var newLanguage = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentLanguage = LanguageListBottomSheetFragmentArgs.fromBundle(it).currentLanguage
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserEditBottomSheetBinding =
            FragmentLanguageListBottomSheetBinding.inflate(inflater, container, false)
        return fragmentUserEditBottomSheetBinding?.apply {
            actionOnClick = this@LanguageListBottomSheetFragment
            Timber.tag(TAG).d("onCreateView : currentLanguage ${this@LanguageListBottomSheetFragment.currentLanguage}")
            currentLanguage = this@LanguageListBottomSheetFragment.currentLanguage
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                        title = getString(R.string.loading),
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
                    SweetAlert.instance.showSuccessAlert(
                        requireActivity(),
                        "Language Changed Successfully"
                    )
                    LocaleHelper.setLocale(requireActivity() as MainActivity, "en");
                    requireActivity().finish()
                    startActivity(Intent(context, MainActivity::class.java))
                    /*
                        ContextUtils.updateLocale(requireContext(), Locale(newLanguage))

                        dismiss()*/
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


    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentUserEditBottomSheetBinding?.ibClose -> onClickClose()
            fragmentUserEditBottomSheetBinding?.btnSave -> onClickSave()
        }
    }

    private fun onClickClose() {
        dismiss()
    }

    private fun onClickSave() {
        newLanguage = when (fragmentUserEditBottomSheetBinding?.rgLanguages?.checkedRadioButtonId) {
            fragmentUserEditBottomSheetBinding?.rbArabicLanguage?.id -> {
                "ar"
            }
            fragmentUserEditBottomSheetBinding?.rbEnglishLanguage?.id -> {
                "en"
            }
            else -> {
                "en"
            }
        }
        Timber.tag(TAG).d("onClickSave : newLanguage $newLanguage")
        profileViewModel.updateLanguage(newLanguage = newLanguage)
    }

    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    companion object {
        private const val TAG = "LanguageListBottomSheet"
    }
}