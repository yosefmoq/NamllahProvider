package com.app.namllahprovider.presentation.fragments.main.profile.services.add_new_service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.AreaDto
import com.app.namllahprovider.data.model.ServiceDto
import com.app.namllahprovider.databinding.FragmentAddNewServiceBinding
import com.app.namllahprovider.presentation.fragments.common.radio_list_bottom_sheet.RadioListSelectionItem
import com.app.namllahprovider.presentation.fragments.common.single_list_bottom_sheet.SingleListSelectionItem
import com.app.namllahprovider.presentation.fragments.main.profile.ProfileViewModel
import com.app.namllahprovider.presentation.fragments.main.profile.services.UserServiceFragment
import com.app.namllahprovider.presentation.fragments.main.profile.services.UserServiceFragmentDirections
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddNewServiceFragment : Fragment(), View.OnClickListener {

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var fragmentAddNewServiceBinding: FragmentAddNewServiceBinding? = null

    var servicesList = arrayOf<ServiceDto>()
    var areasList = arrayOf<AreaDto>()

    var selectedServicePosition = -1
    var selectedAreasPositionList = intArrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAddNewServiceBinding =
            FragmentAddNewServiceBinding.inflate(inflater, container, false)
        return fragmentAddNewServiceBinding?.apply {
            actionOnClick = this@AddNewServiceFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        observeLiveData()
        getServicesAndAreas()
    }

    private fun observeLiveData() {
        profileViewModel.loadingLiveData.observe(viewLifecycleOwner, {
            it?.let {
                Timber.tag(TAG).d("observeLiveData : Loading Status $it")
                if (it) {
                    SweetAlert.instance.showAlertDialog(
                        context = requireContext(),
                        alertType = SweetAlertType.PROGRESS_TYPE,
                        title = "Loading",
                        message = "",
                        confirmText = "",
                        confirmListener = {},
                        cancelText = "",
                        cancelListener = {},
                        cancelable = false,
                    )
                } else {
                    Timber.tag(TAG).d("observeLiveData : dismissAlertDialog")
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


    private fun getServicesAndAreas() {
        profileViewModel.getServicesAndAreas()
        profileViewModel.getServicesLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isEmpty()) {

                }
                this.servicesList = it.toTypedArray()
                profileViewModel.getServicesLiveData.postValue(null)
            }
        }
        profileViewModel.getAreasLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isEmpty()) {

                }
                this.areasList = it.toTypedArray()
                profileViewModel.getAreasLiveData.postValue(null)
            }
        }
    }

    private fun initToolbar() {
        val toolbar = fragmentAddNewServiceBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.add_new_services)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onClick(v: View?) {
        when (v ?: return) {
            fragmentAddNewServiceBinding?.clServiceName -> onClickServiceName()
            fragmentAddNewServiceBinding?.clServiceAreas -> onClickServiceAreas()
            fragmentAddNewServiceBinding?.btnAddService -> onClickAddService()
        }
    }

    private fun onClickAddService() {
        profileViewModel.updateServices(servicesList[selectedServicePosition].id)
        profileViewModel.updateProfileLiveData.observe(viewLifecycleOwner){
            it?.let {
                if (it.status){
                    Toast.makeText(requireContext(), "Service Added Successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun onClickServiceName() {
        findNavController().navigate(
            UserServiceFragmentDirections.actionGlobalSingleListBottomSheetFragment(
                title = "Select Service",
                message = "Select Service You Provide it",
                hint = "",
                currentValue = "",
                textArray = servicesList.map { it.title }.toTypedArray(),
                singleListSelectionItem = SingleListSelectionItem {
                    selectedServicePosition = it
                    fragmentAddNewServiceBinding?.tvServiceName?.text = servicesList[it].title
                }
            )
        )
    }

    private fun onClickServiceAreas() {
        findNavController().navigate(
            UserServiceFragmentDirections.actionGlobalRadioListBottomSheetFragment(
                title = "Select Area",
                message = "Select Area You Cover it",
                hint = "",
                textArray = areasList.map { it.title }.toTypedArray(),
                selectedPositionArray = selectedAreasPositionList,
                radioListSelectionItem = RadioListSelectionItem {
                    selectedAreasPositionList = it
                    if (selectedAreasPositionList.isEmpty()) {
                        fragmentAddNewServiceBinding?.tvServiceAreas?.text =
                            getString(R.string.click_here_to_select_areas_from_list)
                        return@RadioListSelectionItem
                    }
                    val stringBuilder = StringBuilder()
                    it.forEach { position ->
                        stringBuilder.append("   ${areasList[position].title}")
                        stringBuilder.append("\n")
                    }
                    fragmentAddNewServiceBinding?.tvServiceAreas?.text = stringBuilder.toString()
                }
            )
        )
    }

    companion object {
        private const val TAG = "AddNewServiceFragment"

        @JvmStatic
        fun newInstance() = AddNewServiceFragment()
    }
}