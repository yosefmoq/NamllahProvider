package com.app.namllahprovider.presentation.fragments.main.profile.services

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentUserServiceBinding
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserServiceFragment : Fragment(), View.OnClickListener {

    private var fragmentUserServiceBinding: FragmentUserServiceBinding? = null

    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserServiceBinding = FragmentUserServiceBinding.inflate(inflater, container, false)
        return fragmentUserServiceBinding?.apply {
            actionOnCLick = this@UserServiceFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initToolbar()
    }

    private fun initToolbar() {
        val toolbar = fragmentUserServiceBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.my_services)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.service_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_addNewService -> onClickAddNewService()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickAddNewService() {
        findNavController().navigate(UserServiceFragmentDirections.actionUserServiceFragmentToAddNewServiceFragment())
    }

    companion object {
        private const val TAG = "UserServiceFragment"

        @JvmStatic
        fun newInstance() = UserServiceFragment()
    }

    override fun onClick(v: View?) {
        when (v ?: return) {

        }
    }

}