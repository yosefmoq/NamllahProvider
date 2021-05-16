package com.app.namllahprovider.presentation.fragments.main.profile.services.add_new_service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentAddNewServiceBinding
import com.app.namllahprovider.presentation.fragments.common.radio_list_bottom_sheet.RadioListSelectionItem
import com.app.namllahprovider.presentation.fragments.common.single_list_bottom_sheet.SingleListSelectionItem
import com.app.namllahprovider.presentation.fragments.main.profile.services.UserServiceFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewServiceFragment : Fragment(), View.OnClickListener {

    private var fragmentAddNewServiceBinding: FragmentAddNewServiceBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentAddNewServiceBinding =
            FragmentAddNewServiceBinding.inflate(inflater, container, false)
        return fragmentAddNewServiceBinding?.apply {
            actionOnCLick = this@AddNewServiceFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
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
        }
    }


    private fun onClickServiceName() {
        val servicesList = arrayOf(
            "Service Name 01",
            "Service Name 02",
            "Service Name 03",
            "Service Name 03",
            "Service Name 04",
            "Service Name 05",
            "Service Name 06",
            "Service Name 07",
            "Service Name 08",
            "Service Name 09",
            "Service Name 11",
            "Service Name 12",
            "Service Name 13",
            "Service Name 13",
            "Service Name 14",
            "Service Name 15",
            "Service Name 16",
            "Service Name 17",
            "Service Name 18",
            "Service Name 19",
        )
        findNavController().navigate(
            UserServiceFragmentDirections.actionGlobalSingleListBottomSheetFragment(
                title = "Select Service",
                message = "Select Service You Provide it",
                hint = "",
                currentValue = "",
                textArray = servicesList,
                singleListSelectionItem = SingleListSelectionItem {
                    fragmentAddNewServiceBinding?.tvServiceName?.text = servicesList[it]
                }
            )
        )
    }

    private fun onClickServiceAreas() {
        val areasList = arrayOf(
            "Service Area 01",
            "Service Area 02",
            "Service Area 03",
            "Service Area 04",
            "Service Area 05",
            "Service Area 06",
            "Service Area 07",
            "Service Area 08",
            "Service Area 09",
            "Service Area 10",
            "Service Area 11",
            "Service Area 12",
            "Service Area 13",
            "Service Area 13",
            "Service Area 14",
            "Service Area 15",
            "Service Area 16",
            "Service Area 17",
            "Service Area 18",
            "Service Area 19",
            "Service Area 20",
        )
        var intArray = intArrayOf(1, 2, 5, 10)
        findNavController().navigate(
            UserServiceFragmentDirections.actionGlobalRadioListBottomSheetFragment(
                title = "Select Area",
                message = "Select Area You Cover it",
                hint = "",
                textArray = areasList,
                selectedPositionArray = intArray,
                radioListSelectionItem = RadioListSelectionItem {
                    intArray = it
                    val stringBuilder = StringBuilder()
                    it.forEach { position ->
                        stringBuilder.append("   ${areasList[position]}")
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