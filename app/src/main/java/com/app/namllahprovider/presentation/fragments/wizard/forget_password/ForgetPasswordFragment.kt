package com.app.namllahprovider.presentation.fragments.wizard.forget_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.api.auth.forget_password.ForgetPasswordResponse
import com.app.namllahprovider.databinding.FragmentForgetPasswordBinding
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment(), View.OnClickListener {

    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()
    private var fragmentForgetPasswordBinding: FragmentForgetPasswordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentForgetPasswordBinding =
            FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return fragmentForgetPasswordBinding?.apply {
            actionOnClick = this@ForgetPasswordFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        observeLiveData()
    }


    private fun initToolbar() {
        val toolbar = fragmentForgetPasswordBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.forget_password)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "ForgetPasswordFragment"
    }

    private fun initViews() {
        val onTextChanged = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                fragmentForgetPasswordBinding?.tilPhoneNumber?.error = null
            }
        }
        fragmentForgetPasswordBinding?.etPhoneNumber?.addTextChangedListener(onTextChanged)
    }

    private fun observeLiveData() {
        forgetPasswordViewModel.loadingLiveData.observe(viewLifecycleOwner, {
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
            }
        })

        forgetPasswordViewModel.errorLiveData.observe(viewLifecycleOwner, {
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

        forgetPasswordViewModel.sendOTPCodeLiveData.observe(viewLifecycleOwner) {
            it?.let {
                handleSendOTPCodeResponse(it)

                //To Stop Livedata
                forgetPasswordViewModel.sendOTPCodeLiveData.postValue(null)
            }
        }
    }

    private fun handleSendOTPCodeResponse(forgetPasswordResponse: ForgetPasswordResponse) {
        Timber.tag(TAG)
            .d("handleSendOTPCodeResponse : forgetPasswordResponse $forgetPasswordResponse")
        if (forgetPasswordResponse.status) {
            SweetAlert.instance.showSuccessAlert(
                requireActivity(),
                forgetPasswordResponse.msg ?: ""
            )
            //Move to Verify OTP Code Screen

        } else {
            SweetAlert.instance.showFailAlert(
                activity = requireActivity(),
                message = forgetPasswordResponse.error ?: forgetPasswordResponse.msg ?: ""
            )
        }
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentForgetPasswordBinding?.btnSendOTPCode -> onClickSendOTPCode()
        }
    }

    private fun onClickSendOTPCode() {
        val errorMessage: String
        val phoneNumber = fragmentForgetPasswordBinding?.etPhoneNumber?.text?.toString() ?: ""
        if (phoneNumber.isEmpty() || phoneNumber.isBlank() || phoneNumber.length < 6) {
            errorMessage = "Invalid Phone number"
            fragmentForgetPasswordBinding?.tilPhoneNumber?.error = errorMessage
            return
        }
        forgetPasswordViewModel.sendOTPCode(phoneNumber)
    }
}