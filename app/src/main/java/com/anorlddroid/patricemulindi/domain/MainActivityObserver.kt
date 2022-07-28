package com.anorlddroid.patricemulindi.domain

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class MainActivityObserver(private val update: () -> Unit): DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.removeObserver(this)
        update()
    }
}