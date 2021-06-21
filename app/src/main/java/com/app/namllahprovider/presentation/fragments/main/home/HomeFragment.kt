package com.app.namllahprovider.presentation.fragments.main.home

import android.os.Bundle
import android.os.CountDownTimer
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
import com.app.namllahprovider.presentation.utils.SweetAlert
import com.app.namllahprovider.presentation.utils.SweetAlertType
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var fragmentHomeBinding: FragmentHomeBinding? = null

    private var toolbar: UserToolbarBinding? = null
    private var toolBarTitleView: TextView? = null
    private var ivProfileImage: CircleImageView? = null
    private var swUserAvailable: SwitchCompat? = null
    lateinit var adapter: ViewPagerAdapter


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
        fragmentHomeBinding?.vpHomeViewPager?.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                Timber.tag(TAG).d("onPageSelected : position $position")
                lastFragmentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        fragmentHomeBinding?.vpHomeViewPager?.setPageTransformer(true, ZoomOutPageTransformer())
        setupViewPager(fragmentHomeBinding?.vpHomeViewPager ?: return)


        val totalMillis = 5000L
        val countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val goingMillis = totalMillis.minus(millisUntilFinished)
                Timber.tag(TAG).d("onTick : Remaining Seconds ${millisUntilFinished.div(1000)}")
                Timber.tag(TAG).d("onTick : Going Seconds ${goingMillis.div(1000)}")
                val s = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(goingMillis),
                    TimeUnit.MILLISECONDS.toMinutes(goingMillis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(goingMillis)), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(goingMillis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(goingMillis))
                )
                Timber.tag(TAG).d("onTick : Going Time $s")

            }

            override fun onFinish() {

            }
        }
        countDownTimer.start()
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

                ivProfileImage?.loadImage(userDto.images.med)

                toolbar?.root?.navigationIcon = null
            }
        }

        homeViewModel.changeAvailableLiveData.observe(viewLifecycleOwner) {
            it?.let {

            }
        }

        FirebaseMessaging.getInstance()
            .subscribeToTopic(getString(R.string.firebase_notification_topic))
            .addOnCompleteListener {
                Timber.tag(TAG).d("observeLiveData : FirebaseMessaging Subscribing to Topic isSuccessful ${it.isSuccessful}")
                if (it.isComplete) {
                    FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                        Timber.tag(TAG).d("observeLiveData : FirebaseMessaging get Token ${task.result.token}")
                        if (task.isComplete && task.isSuccessful) {
                            homeViewModel.updateUserFcmToken(task.result.token)
                        }
                    }
                }
            }
    }


    private fun setupViewPager(viewPager: ViewPager) {
        adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NewOrderFragment.newInstance(), "New")
        adapter.addFragment(InProgressOrderFragment.newInstance(), "In Progress")
        adapter.addFragment(FinishedOrderFragment.newInstance(), "Finish")
        viewPager.adapter = adapter
        viewPager.currentItem = lastFragmentPosition
    }

    companion object {
        private const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()

        var lastFragmentPosition = 0
    }
}