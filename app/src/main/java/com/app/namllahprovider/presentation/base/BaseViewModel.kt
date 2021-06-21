package com.app.namllahprovider.presentation.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel constructor(application: Application) : AndroidViewModel(application),
    CoroutineScope {

    val disposable = CompositeDisposable()

    val dialogLiveData = MutableLiveData<DialogData?>()
    val errorLiveData = MutableLiveData<Throwable?>()
    val loadingLiveData = MutableLiveData<Boolean?>()
    val noDataLiveData = MutableLiveData<String?>()

    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val ioScheduler = Schedulers.io()
    val newThreadScheduler = Schedulers.newThread()
    val computationScheduler = Schedulers.computation()


    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable.dispose()
    }

    fun changeLoadingStatus(newStatus: Boolean,loadingMessage:String = "") {
        println("changeLoadingStatus :: New Status $newStatus, Loading Message $loadingMessage")
        loadingLiveData.postValue(newStatus)
    }

    fun changeErrorMessage(throwable: Throwable) {
        errorLiveData.postValue(throwable)
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            errorLiveData.postValue(null)
        }
    }

    fun notifyNoDataComing(){
        noDataLiveData.postValue("")
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            errorLiveData.postValue(null)
        }
    }

    fun changeDialogLiveData(dialogData: DialogData) {
        dialogLiveData.postValue(dialogData)
    }

}
