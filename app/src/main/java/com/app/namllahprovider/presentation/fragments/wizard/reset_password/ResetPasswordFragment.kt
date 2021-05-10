package com.app.namllahprovider.presentation.fragments.wizard.reset_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(), View.OnClickListener {


    private var fragmentResetPasswordBinding: FragmentResetPasswordBinding? = null
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
        initToolbar()
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

    }

    private fun onClickBack() {
        //Show Alert Dialog
    }

    companion object {
        private const val TAG = "ResetPasswordFragment"
    }
}