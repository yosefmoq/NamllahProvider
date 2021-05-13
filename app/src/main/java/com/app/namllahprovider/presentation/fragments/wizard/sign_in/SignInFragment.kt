package com.app.namllahprovider.presentation.fragments.wizard.sign_in

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.api.auth.sign_in.SignInResponse
import com.app.namllahprovider.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignInFragment : Fragment(), View.OnClickListener {

    private val signInViewModel: SignInViewModel by viewModels()
    private var fragmentSignInBinding: FragmentSignInBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSignInBinding = FragmentSignInBinding.inflate(inflater, container, false)
        return fragmentSignInBinding?.apply {
            actionOnClick = this@SignInFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                fragmentSignInBinding?.tilPhoneNumber?.error = null
                fragmentSignInBinding?.tilPassword?.error = null
            }
        }
        fragmentSignInBinding?.etPhoneNumber?.addTextChangedListener(onTextChanged)
        fragmentSignInBinding?.etPassword?.addTextChangedListener(onTextChanged)
    }

    private fun initToolbar() {
        val toolbar = fragmentSignInBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.login_to_your_account)

        toolbar?.root?.navigationIcon = null
    }

    private fun observeLiveData() {
        signInViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            Timber.tag(TAG).d("observeLiveData : Loading Status $it")
        })

        signInViewModel.errorLiveData.observe(viewLifecycleOwner) {
            Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
            it.printStackTrace()
        }

        signInViewModel.signInLiveData.observe(viewLifecycleOwner, {
            it?.let {
                handleSignInResponse(it)
                //To Stop Livedata
                signInViewModel.signInLiveData.postValue(null)
            }
        })
    }

    private fun handleSignInResponse(signInResponse: SignInResponse) {
        if (signInResponse.userDto != null) {
            //Success Login
            //Save User data in SP
            signInViewModel.saveUserDataLocal(signInResponse.userDto!!)
            signInViewModel.changeLoginStatus(true)
            findNavController().navigate(R.id.action_signInFragment_to_mainFragment)
        } else {
            val errorMessage = signInResponse.error ?: signInResponse.message
            ?: "Something error, Please try again later"
            signInViewModel.changeErrorMessage(Throwable(errorMessage))
        }
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentSignInBinding?.tvForgetPassword -> onClickForgetPassword()
            fragmentSignInBinding?.btnSignIn -> onClickSignIn()
            fragmentSignInBinding?.ibSignWithGoogle -> onClickSignWithGoogle()
            fragmentSignInBinding?.tvSignUp -> onClickSignUp()
        }
    }

    private fun onClickForgetPassword() {
        findNavController().navigate(R.id.action_signInFragment_to_forgetPasswordFragment)
    }

    private fun onClickSignIn() {
        val errorMessage: String
        val phoneNumber = fragmentSignInBinding?.etPhoneNumber?.text?.toString() ?: ""
        val password = fragmentSignInBinding?.etPassword?.text?.toString() ?: ""

        if (phoneNumber.isEmpty() || phoneNumber.isBlank() || phoneNumber.length < 6) {
            errorMessage = "Invalid Phone number"
            fragmentSignInBinding?.tilPhoneNumber?.error = errorMessage
            return
        }

        if (password.isEmpty() || password.isBlank() || password.length < 6) {
            errorMessage = "Please fill Password"
            fragmentSignInBinding?.tilPassword?.error = errorMessage
            return
        }
        //Show Loading Dialog
        signInViewModel.signInRequest(phoneNumber, password)
    }

    private fun onClickSignWithGoogle() {

    }

    private fun onClickSignUp() {
        findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}