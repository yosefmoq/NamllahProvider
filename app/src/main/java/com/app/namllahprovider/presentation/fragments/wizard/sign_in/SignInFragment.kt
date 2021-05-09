package com.app.namllahprovider.presentation.fragments.wizard.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentSignInBinding


class SignInFragment : Fragment(), View.OnClickListener {

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
        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = fragmentSignInBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        toolbar?.root?.title = ""

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.login_to_your_account)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onClick(v: View?) {

    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}