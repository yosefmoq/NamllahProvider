package com.app.namllahprovider.presentation.fragments.wizard.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment(), View.OnClickListener {

    private var fragmentSignUpBinding: FragmentSignUpBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        return fragmentSignUpBinding?.apply {
            actionOnClick = this@SignUpFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = fragmentSignUpBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        toolbar?.root?.title = ""

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.create_new_account)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "SignUpFragment"
    }

    override fun onClick(view: View?) {
        when (view ?: return) {
            fragmentSignUpBinding?.btnSignUp -> onClickSignUp()
            fragmentSignUpBinding?.ibSignWithGoogle -> onClickSignWithGoogle()
            fragmentSignUpBinding?.tvSignIn -> onClickSignIn()
        }
    }

    private fun onClickSignUp() {

    }

    private fun onClickSignWithGoogle() {

    }

    private fun onClickSignIn() {
        findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
    }
}