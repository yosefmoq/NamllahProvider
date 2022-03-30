package com.app.namllahprovider.presentation.fragments.main.profile.services

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentUserServiceBinding
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserServiceFragment : Fragment(), View.OnClickListener {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var fragmentUserServiceBinding: FragmentUserServiceBinding? = null

    private val userServiceAdapter = UserServiceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentUserServiceBinding = FragmentUserServiceBinding.inflate(inflater, container, false)
        return fragmentUserServiceBinding?.apply {
            actionOnCLick = this@UserServiceFragment
            userServiceAdapter = this@UserServiceFragment.userServiceAdapter
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initToolbar()
        observeLiveData()
        getUserServices()

    }

    private fun getUserServices() {
        profileViewModel.getLoggedUser()
        profileViewModel.getLoggedUserLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("getLoggedProfile : it $it")
                Timber.tag(TAG).d("getLoggedProfile : services size ${it.services.servicesList.size}")
                userServiceAdapter.updateData(it.services.servicesList)
                profileViewModel.getLoggedUserLiveData.postValue(null)
            }
        })
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
            }
        })

        profileViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        }
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