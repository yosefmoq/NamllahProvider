package com.app.namllahprovider.presentation.fragments.main.home.work

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.namllahprovider.R
import com.app.namllahprovider.databinding.FragmentWorkBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class WorkFragment : Fragment(), View.OnClickListener {

    private var fragmentWorkBinding: FragmentWorkBinding? = null

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

        initCounter()
    }

    private fun initCounter() {
        val totalMillis = 3600000L
        val countDownTimer = object : CountDownTimer(totalMillis, 1000) {
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
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(
                                    millisUntilFinished
                                )
                            ), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
                Timber.tag(TAG).d("onTick : Going Time $goingTime")
                Timber.tag(TAG).d("onTick : Remaining Time $remainingTime")
                fragmentWorkBinding?.tvWorkTimer?.text = goingTime
            }

            override fun onFinish() {

            }
        }
        countDownTimer.start()
    }

    private fun initToolbar() {
        val toolbar = fragmentWorkBinding?.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar?.root)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        val toolBarTitleView = toolbar?.toolbarTitle
        toolBarTitleView?.text = getString(R.string.check_up)

        toolbar?.root?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val TAG = "WorkFragment"

        @JvmStatic
        fun newInstance() = WorkFragment()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}