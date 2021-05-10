package com.app.namllahprovider.presentation.fragments.wizard.forget_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentForgetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : Fragment(), View.OnClickListener {

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

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentForgetPasswordBinding?.btnSendOTPCode -> onClickSendOTPCode()
        }
    }

    private fun onClickSendOTPCode() {

    }
}