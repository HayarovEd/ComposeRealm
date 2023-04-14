package com.eedurda77

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm

@HiltAndroidApp
class ComposeRealmApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}