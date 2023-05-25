package com.example.proiect_android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class Extensions {
    
    companion object {
        fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            observe(lifecycleOwner, object : Observer<T> {
                override fun onChanged(value: T) {
                    observer.onChanged(value)
                    removeObserver(this)
                }
            })
        }
    }

}