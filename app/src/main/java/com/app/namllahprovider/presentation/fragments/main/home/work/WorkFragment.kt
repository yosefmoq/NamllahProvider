package com.app.namllahprovider.presentation.fragments.main.home.work

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
import com.app.namllahprovider.databinding.FragmentWorkBinding
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import com.app.namllahprovider.presentation.fragments.main.home.order_details.OrderDetailsFragmentArgs
import com.app.namllahprovider.presentation.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class WorkFragment : Fragment(), View.OnClickListener {

    private val homeViewModel: HomeViewModel by viewModels()

    private var fragmentWorkBinding: FragmentWorkBinding? = null
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
        fragmentWorkBinding = FragmentWorkBinding.inflate(inflater, container, false)
        return fragmentWorkBinding?.apply {
            actionOnClick = this@WorkFragment
        }?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        getOrderData()
        observeLiveData()
    }

    private fun initToolbar() {
        val toolbar = fragmentWorkBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.work)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getOrderData() {
        if (orderId != -1) {
            homeViewModel.getOrderData(orderId)
            homeViewModel.getOrderDataLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    orderDto = it
                    fragmentWorkBinding?.apply {
                        orderDto = this@WorkFragment.orderDto
                        when (orderDto?.status?.getOrderStatus()) {
                            OrderStat.CHECK -> {
                                btnStartWorkTimer.visibility = View.VISIBLE
                                llBtnsContainer.visibility = View.GONE
                            }
                            OrderStat.WORKING -> {
                                btnStartWorkTimer.visibility = View.GONE
                                llBtnsContainer.visibility = View.VISIBLE
                                if (orderDto?.isWorking == 1) {
                                    resumeWork()
                                } else {
                                    pauseWork()
                                }
                            }
                            else -> {
                                SweetAlert.instance.showFailAlert(
                                    activity = requireActivity(),
                                    message = "Couldn't Update Order Status"
                                )
                                findNavController().popBackStack(R.id.mainFragment, false)
                            }
                        }
                    }


                }
            }
        } else {
            Timber.tag(TAG).d("onViewCreated : Can't find order details")
        }

    }

    private fun resumeWork() {
        val oldDuration = orderDto?.duration!!
        initCounter(oldDuration)
        updateUIs()
    }

    private fun pauseWork() {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        updateUIs()
    }

    private fun updateUIs() {
        Timber.tag(TAG).d("updateUIs : orderDto $orderDto")
        fragmentWorkBinding?.apply {
            tvWorkTimer.text = (this@WorkFragment.orderDto?.duration ?: 0).getTime()

            btnStartWorkTimer.visibility = View.GONE
            llBtnsContainer.visibility = View.VISIBLE
            Timber.tag(TAG).d("updateUIs : orderDto $orderDto")

            if (orderDto?.isWorking == 1) {
                btnPauseResumeTime.text = getString(R.string.pause)
            } else {
                btnPauseResumeTime.text = getString(R.string.resume)
            }
        }
    }

    private fun initCounter(initialTime: Int) {
        val totalMillis = 360000000L
        countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val goingMillis = totalMillis.minus(millisUntilFinished).plus(initialTime)
                val goingTime = goingMillis.toInt().getTime()

                val remainingTime = millisUntilFinished.toInt().getTime()
                Timber.tag(TAG).d("onTick : Going Time $goingTime")
                Timber.tag(TAG).d("onTick : Remaining Time $remainingTime")
                fragmentWorkBinding?.tvWorkTimer?.text = goingTime
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

        homeViewModel.changeOrderStatusLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Timber.tag(TAG).d("observeLiveData ChangeOrderStatus : $it")
                Timber.tag(TAG)
                    .d("observeLiveData : ChangeOrderStatus OrderId:${it.order?.id} to ${it.order?.status?.getOrderStatus()}")
                if (it.status) {
                    orderDto = it.order
                    fragmentWorkBinding?.apply {
                        orderDto = this@WorkFragment.orderDto
                    }
                    when (it.order?.status?.getOrderStatus()) {
                        OrderStat.WORKING -> {
                            //Update Timer Start/Stop
                            if (orderDto?.isWorking == 1) {
                                resumeWork()
                            } else {
                                pauseWork()
                            }
                        }
                        else -> {

                        }
                    }
                } else {
                    homeViewModel.changeErrorMessage(Throwable(it.error))
                }
            }
        }
    }

    companion object {
        private const val TAG = "WorkFragment"
    }

    override fun onClick(v: View?) {
        when (v) {
            fragmentWorkBinding?.btnStartWorkTimer -> onClickStartWork()
            fragmentWorkBinding?.btnPauseResumeTime -> onClickPauseResumeWork()
            fragmentWorkBinding?.btnFinishWork -> onClickFinishWork()
        }
    }

    private fun onClickFinishWork() {
        homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.STOP_WORK)
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            findNavController().navigate(
                WorkFragmentDirections.actionWorkFragmentToBillFragment(
                    orderId = orderId
                )
            )
        }
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    private fun onClickPauseResumeWork() {
        if (orderDto?.isWorking == 1) {
            homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.STOP_WORK)
        } else {
            homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.START_WORK)
        }
    }

    private fun onClickStartWork() {
        homeViewModel.changeOrderStatus(orderId = orderId, OrderStatusRequestType.START_WORK)
    }
}

