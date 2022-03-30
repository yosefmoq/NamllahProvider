package com.app.namllahprovider.presentation.fragments.wizard.reset_password

import android.content.Intent
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
import com.app.namllahprovider.databinding.FragmentResetPasswordBinding
import com.app.namllahprovider.presentation.MainActivity
import com.app.namllahprovider.presentation.fragments.wizard.forget_password.ForgetPasswordFragment
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(), View.OnClickListener {
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()
    private var fragmentResetPasswordBinding: FragmentResetPasswordBinding? = null

    private var phoneNumber = ""
    private var code = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentResetPasswordBinding =
            FragmentResetPasswordBinding.inflate(inflater, container, false)
        return fragmentResetPasswordBinding?.apply {
            actionOnClick = this@ResetPasswordFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            phoneNumber = ResetPasswordFragmentArgs.fromBundle(it).phoneNumber
            code = ResetPasswordFragmentArgs.fromBundle(it).code
        }

        initViews()
        initToolbar()
        observeLiveData()
    }

    private fun initViews() {
        val onTextChanged = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                fragmentResetPasswordBinding?.tilPassword?.error = null
                fragmentResetPasswordBinding?.tilConfirmPassword?.error = null
            }
        }
        fragmentResetPasswordBinding?.etPassword?.addTextChangedListener(onTextChanged)
        fragmentResetPasswordBinding?.etConfirmPassword?.addTextChangedListener(onTextChanged)
    }

    private fun observeLiveData() {
        resetPasswordViewModel.loadingLiveData.observe(viewLifecycleOwner, {
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

        resetPasswordViewModel.errorLiveData.observe(viewLifecycleOwner, {
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
        resetPasswordViewModel.resetPasswordLiveData.observe(viewLifecycleOwner){
            it?.let {
                if  (it.status!!){
                    resetPasswordViewModel.saveUserTokenLocal(it.userDto!!.token ?: "")
                    resetPasswordViewModel.changeLoginStatus(true)
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                }
                resetPasswordViewModel.resetPasswordLiveData.postValue(null)
            }
        }
    }

    private fun initToolbar() {
        val toolbar = fragmentResetPasswordBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.reset_password)

        toolbar?.root?.setNavigationOnClickListener {
            onClickBack()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentResetPasswordBinding?.btnResetPassword -> onClickResetPassword()
        }
    }

    private fun onClickResetPassword() {
        val password = fragmentResetPasswordBinding?.etPassword?.text?.toString()
        val confirmPassword = fragmentResetPasswordBinding?.etConfirmPassword?.text?.toString()
        when {
            password.isNullOrEmpty() -> {
                fragmentResetPasswordBinding?.etPassword?.error = "Please fill new Password"
            }
            confirmPassword.isNullOrEmpty() -> {
                fragmentResetPasswordBinding?.etConfirmPassword?.error =
                    "Please confirm new Password"
            }
            password != confirmPassword -> {
                fragmentResetPasswordBinding?.etPassword?.error = "Passwords does not match"
                fragmentResetPasswordBinding?.etConfirmPassword?.error = "Passwords does not match"
            }
            else -> {

                resetPasswordViewModel.resetPassword(phoneNumber , password , code)
            }
        }
    }

    private fun onClickBack() {
        //Show Alert Dialog
    }

    companion object {
        private const val TAG = "ResetPasswordFragment"
    }
}