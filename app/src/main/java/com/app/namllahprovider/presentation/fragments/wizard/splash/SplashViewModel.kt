package com.app.namllahprovider.presentation.fragments.wizard.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), CoroutineScope {

    val disposable = CompositeDisposable()
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun printInLaunch(){
        Timber.tag(TAG).d("printInLaunch : ")
        launch {
            Timber.tag(TAG).d("printInLaunch : ")
        }
    }

    companion object{
        private const val TAG = "SplashViewModel"
    }

}