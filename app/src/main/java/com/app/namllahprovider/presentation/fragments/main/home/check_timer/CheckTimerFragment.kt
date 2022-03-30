package com.app.namllahprovider.presentation.fragments.main.home.check_timer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.databinding.FragmentCheckTimerBinding
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.fragments.main.home.order_details.OrderDetailsFragmentArgs
import com.app.namllahprovider.presentation.utils.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CheckTimerFragment : Fragment(), View.OnClickListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentCheckTimerBinding: FragmentCheckTimerBinding? = null
    private var orderId: Int = -1
    private var orderDto: OrderDto? = null

    lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = OrderDetailsFragmentArgs.fromBundle(it).orderId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCheckTimerBinding = FragmentCheckTimerBinding.inflate(inflater, container, false)
        return fragmentCheckTimerBinding?.apply {
            actionOnClick = this@CheckTimerFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        getOrderData()

        initCounter()

        observeLiveData()
    }

    private fun getOrderData() {
        if (orderId != -1) {
            homeViewModel.getOrderData(orderId)
            homeViewModel.getOrderDataLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    this@CheckTimerFragment.orderDto = it
                }
            }
        } else {
            Timber.tag(TAG).d("onViewCreated : Can't find order details")
        }

    }


    private fun initToolbar() {
        val toolbar = fragmentCheckTimerBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.check_up)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
    }

    private fun initCounter() {
        val totalMillis = 360000000L
        countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val goingMillis = totalMillis.minus(millisUntilFinished)
                val goingTime = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(goingMillis),
                    TimeUnit.MILLISECONDS.toMinutes(goingMillis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(goingMillis)), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(goingMillis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(goingMillis))
                )

                val remainingTime = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                )
                Timber.tag(TAG).d("onTick : Going Time $goingTime")
                Timber.tag(TAG).d("onTick : Remaining Time $remainingTime")
                fragmentCheckTimerBinding?.tvCheckTimer?.text = goingTime
            }

            override fun onFinish() {

            }
        }
        countDownTimer.start()
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

    }

    companion object {
        private const val TAG = "CheckTimerFragment"

        @JvmStatic
        fun newInstance() = CheckTimerFragment()
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentCheckTimerBinding?.btnFinishChecking -> onClickFinishChecking()
        }
    }

    private fun onClickFinishChecking() {
        countDownTimer.cancel()
        findNavController().navigate(
            CheckTimerFragmentDirections.actionCheckTimerFragmentToCheckFragment(
                orderId = orderId
            )
        )
    }
}