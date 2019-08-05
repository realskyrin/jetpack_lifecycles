package com.skyrin.jetpack_lifecycles

import android.os.SystemClock
import androidx.lifecycle.*
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timerTask

class TimerViewModel : ViewModel(),LifecycleObserver{
    private var startTime: Long? = null
    private var stopTime: Long = 0
    private var elapsedMilliseconds = 0L


    private val _elapsedTime = MutableLiveData<Long>()
    var elapsedTime: LiveData<Long> = _elapsedTime

    init {
        startTime = SystemClock.elapsedRealtime()

        Timer().scheduleAtFixedRate(timerTask {
            elapsedMilliseconds = SystemClock.elapsedRealtime() - startTime!!
            val newValue = elapsedMilliseconds / 1000
            _elapsedTime.postValue(newValue)
        }, ONE_SECOND, ONE_SECOND)
    }

    companion object {
        const val ONE_SECOND = 1000L
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start(){
        startTime = SystemClock.elapsedRealtime() - stopTime
        // 让 UI 立即更新最新结果
        _elapsedTime.postValue((SystemClock.elapsedRealtime() - startTime!!)/1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop(){
        stopTime = elapsedMilliseconds
    }
}