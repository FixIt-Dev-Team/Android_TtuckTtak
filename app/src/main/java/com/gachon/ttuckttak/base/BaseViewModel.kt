package com.gachon.ttuckttak.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gachon.ttuckttak.utils.Event

open class BaseViewModel : ViewModel() {

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    fun viewEvent(content: Any) {
        Log.d("BaseViewModel", "ViewEvent called with content: $content")
        _viewEvent.postValue(Event(content))
    }
}