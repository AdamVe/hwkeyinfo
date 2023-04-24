package com.adamve.hwkeyinfo

import android.app.Application
import com.adamve.hwkeyinfo.data.AppContainer
import com.adamve.hwkeyinfo.data.AppDataContainer

class HwKeyInfoApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}