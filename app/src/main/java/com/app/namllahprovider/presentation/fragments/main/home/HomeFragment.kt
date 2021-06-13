package com.app.namllahprovider.presentation.fragments.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentHomeBinding
import com.app.namllahprovider.databinding.UserToolbarBinding
import com.app.namllahprovider.presentation.base.ViewBindingAdapter.loadImage
import com.app.namllahprovider.presentation.base.ViewPagerAdapter
import com.app.namllahprovider.presentation.base.ZoomOutPageTransformer
import com.app.namllahprovider.presentation.fragments.main.home.finished_order.FinishedOrderFragment
import com.app.namllahprovider.presentation.fragments.main.home.in_progress_order.InProgressOrderFragment
import com.app.namllahprovider.presentation.fragments.main.home.new_order.NewOrderFragment
import com.app.namllahprovider.presentation.fragments.wizard.sign_in.SignInFragment
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var fragmentHomeBinding: FragmentHomeBinding? = null

    private var toolbar: UserToolbarBinding? = null
    private var toolBarTitleView: TextView? = null
    private var ivProfileImage: CircleImageView? = null
    private var swUserAvailable: SwitchCompat? = null


    val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        homeViewModel.changeUserAvailable(isChecked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.tag(TAG).d("onCreateView : ")
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding?.apply { }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.tag(TAG).d("onViewCreated : ")
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        observeLiveData()
        homeViewModel.getLoggedUser()

        fragmentHomeBinding?.tlHomeTabLayout?.setupWithViewPager(
            fragmentHomeBinding?.vpHomeViewPager ?: return
        )
        fragmentHomeBinding?.vpHomeViewPager?.setPageTransformer(true, ZoomOutPageTransformer())
        setupViewPager(fragmentHomeBinding?.vpHomeViewPager ?: return)
    }

    private fun initToolbar() {

        toolbar = fragmentHomeBinding?.tbUserToolbar

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        toolBarTitleView = toolbar?.tvToolbarTitle
        swUserAvailable = toolbar?.swUserAvailable
        ivProfileImage = toolbar?.ivProfileImage
    }

    private fun observeLiveData() {
        homeViewModel.loadingLiveData.observe(viewLifecycleOwner, {
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
                    SweetAlert.instance.dismissAlertDialog(true)
                }
            }
        })

        homeViewModel.errorLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).e("observeLiveData : Error Message ${it.message}")
                SweetAlert.instance.showFailAlert(activity = requireActivity(), throwable = it)
                it.printStackTrace()
            }
        }
        homeViewModel.getLoggedUserLiveData.observe(viewLifecycleOwner) {
            it?.let { userDto ->
                Timber.tag(TAG).d("observeLiveData : userDto $userDto")

                val isAvailable = userDto.isAvailable == 1
                toolBarTitleView?.text = if (isAvailable) "You're online" else "You're offline"

                swUserAvailable?.setOnCheckedChangeListener(null)
                swUserAvailable?.isChecked = isAvailable
                swUserAvailable?.setOnCheckedChangeListener(onCheckedChangeListener)

                ivProfileImage?.loadImage(userDto.images ?: getString(R.string.fake_image))

                toolbar?.root?.navigationIcon = null
            }
        }

        homeViewModel.changeAvailableLiveData.observe(viewLifecycleOwner) {
            it?.let {

            }
        }
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NewOrderFragment.newInstance(), "New")
        adapter.addFragment(InProgressOrderFragment.newInstance(), "In Progress")
        adapter.addFragment(FinishedOrderFragment.newInstance(), "Finish")
        viewPager.adapter = adapter
    }

    companion object {
        private const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}