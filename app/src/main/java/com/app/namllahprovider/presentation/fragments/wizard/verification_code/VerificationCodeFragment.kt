package com.app.namllahprovider.presentation.fragments.wizard.verification_code

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.api.auth.check_reset_password.CheckResetPasswordResponse
import com.app.namllahprovider.data.api.auth.verification_code.VerificationCodeResponse
import com.app.namllahprovider.databinding.FragmentVerificationCodeBinding
import com.app.namllahprovider.presentation.MainActivity
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VerificationCodeFragment : Fragment(), View.OnClickListener {

    private val verificationCodeViewModel: VerificationCodeViewModel by viewModels()
    var code: String = ""

    private var fragmentVerificationCodeBinding: FragmentVerificationCodeBinding? = null
    private var phoneNumber = ""
    private var verifyType = VERIFY_TYPE_ACTIVATION_CODE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentVerificationCodeBinding =
            FragmentVerificationCodeBinding.inflate(inflater, container, false)
        return fragmentVerificationCodeBinding?.apply {
            actionOnClick = this@VerificationCodeFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        arguments?.let {
            phoneNumber = VerificationCodeFragmentArgs.fromBundle(it).phoneNumber
            verifyType = VerificationCodeFragmentArgs.fromBundle(it).verifyType
            fragmentVerificationCodeBinding?.tvPhoneNumber?.text = phoneNumber
        }
        observeLiveData()
    }

    private fun observeLiveData() {
        verificationCodeViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            Timber.tag(TAG).d("observeLiveData : Loading Status $it")
        })

        verificationCodeViewModel.errorLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        })

        verificationCodeViewModel.dialogLiveData.observe(viewLifecycleOwner, {
            Timber.tag(TAG).e("observeLiveData : Error Message $it")
        })

        verificationCodeViewModel.verificationCodeLiveData.observe(viewLifecycleOwner, {
            it?.let {
                handleVerifyCodeResponse(it)
                //To Stop Livedata
                verificationCodeViewModel.verificationCodeLiveData.postValue(null)
            }
        })

        verificationCodeViewModel.checkResetPasswordLiveData.observe(viewLifecycleOwner, {
            it?.let {
                handleResetPasswordResponse(it)
                //To Stop Livedata
                verificationCodeViewModel.verificationCodeLiveData.postValue(null)
            }
        })
    }

    private fun handleVerifyCodeResponse(verificationCodeResponse: VerificationCodeResponse) {
        if (verificationCodeResponse.userDto != null) {
            //Success Login
            //Save User data in SP
            verificationCodeViewModel.saveUserDataLocal(verificationCodeResponse.userDto!!)
            verificationCodeViewModel.saveUserTokenLocal(
                verificationCodeResponse.userDto!!.token ?: ""
            )
            verificationCodeViewModel.changeLoginStatus(true)
            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        } else {
            if (verificationCodeResponse.status!!) {
                //Account already active go to Login Page
                SweetAlert.instance.showSuccessAlert(
                    requireActivity(),
                    message = "Your account already active, Go to login"
                )
                findNavController().navigate(VerificationCodeFragmentDirections.actionGlobalSignInFragment())
            } else {
                //OTP Code is error
                SweetAlert.instance.showFailAlert(
                    activity = requireActivity(),
                    message = verificationCodeResponse.msg ?: ""
                )
            }
        }
    }


    private fun handleResetPasswordResponse(checkResetPasswordResponse: CheckResetPasswordResponse) {

        if (checkResetPasswordResponse.status!!) {
            findNavController().navigate(
                VerificationCodeFragmentDirections.actionVerificationCodeFragmentToResetPasswordFragment(
                    phoneNumber = phoneNumber,
                    code = code.toString()
                )
            )
        } else {
            //OTP Code is error
            SweetAlert.instance.showFailAlert(
                activity = requireActivity(),
                message = checkResetPasswordResponse.msg ?: ""
            )
        }
    }

    private fun initToolbar() {
        val toolbar = fragmentVerificationCodeBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.verification_code)

        toolbar?.root?.setNavigationOnClickListener {
            onClickBack()
        }
    }

    companion object {
        private const val TAG = "VerificationCodeFragmen"

        const val VERIFY_TYPE_ACTIVATION_CODE = 1
        const val VERIFY_TYPE_RESET_PASSWORD = 2
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentVerificationCodeBinding?.btnVerifyOTP -> onClickVerifyOTPCode()
            fragmentVerificationCodeBinding?.tvPhoneNumber -> onClickPhoneNumber()
            fragmentVerificationCodeBinding?.tvResendOTPCode -> onClickResendOTPCode()
        }
    }

    private fun onClickBack() {
        //Show Alert dialog
        SweetAlert.instance.showAlertDialog(
            context = requireContext(),
            alertType = SweetAlertType.WARNING_TYPE,
            title = "Are you sure?",
            message = "You cannot login when you exit without active your account.",
            confirmText = "Stay",
            confirmListener = {
                SweetAlert.instance.dismissAlertDialog()
            },
            cancelText = "Exit",
            cancelListener = {
                findNavController().popBackStack()
                Timber.tag(TAG).d("onClickBack : ")
            },
            cancelable = true,
        )
    }

    private fun onClickVerifyOTPCode() {
        val otpCode = fragmentVerificationCodeBinding?.pvVerifyOTP?.text.toString()
        code = otpCode
        if (code.isNotEmpty()) {
            when (verifyType) {
                VERIFY_TYPE_ACTIVATION_CODE -> {
                    verificationCodeViewModel.verifyOTPCode(phoneNumber = phoneNumber, code = code)
                }
                VERIFY_TYPE_RESET_PASSWORD -> {
                    verificationCodeViewModel.checkResetCode(phoneNumber = phoneNumber, code = code)
                }
                else -> {

                }
            }
        } else
            verificationCodeViewModel.changeErrorMessage(Throwable("OTP Code Error"))
    }

    private fun onClickPhoneNumber() {
        findNavController().popBackStack()
    }

    private fun onClickResendOTPCode() {

    }

}