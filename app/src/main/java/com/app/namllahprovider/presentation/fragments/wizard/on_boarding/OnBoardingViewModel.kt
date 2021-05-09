package com.app.namllahprovider.presentation.fragments.wizard.on_boarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.namllahprovider.domain.repository.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository
) : ViewModel(), CoroutineScope {

    val disposable = CompositeDisposable()
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun changeOnBoardingStatus(status: Boolean) {
        configRepository.setSeenOnBoarding(status)
    }
}