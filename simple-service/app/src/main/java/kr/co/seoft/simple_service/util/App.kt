package kr.co.seoft.simple_service.util

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        AlarmChannelConfig.registAlarmChannel(this)
    }
}