package kr.co.seoft.schedule_alarm.example

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class ExampleService : Service() {

    private val exampleStringValue = "STRING"
    private val exampleIntValue = 12345

    private val exampleSyncStringValue = "SYNC_STRING"
    private val exampleSyncIntValue = 67890

    private val compositeDisposable = CompositeDisposable()

    private var iExampleServiceCallback: IExampleServiceCallback? = null

    private val binder = object : IExampleService.Stub() {

        override fun getStringValue(): String {
            return exampleStringValue
        }

        override fun getIntValue(): Int {
            return exampleIntValue
        }

        override fun requestStringAsync() {
            waitAndSendString()
        }

        override fun requestIntAsync() {
            waitAndSendInt()
        }

        override fun addCallback(callback: IExampleServiceCallback?) {
            iExampleServiceCallback = callback
        }

        override fun removeCallback() {
            iExampleServiceCallback = null
        }

    }

    private fun waitAndSendString() {
        compositeDisposable.add(Single.timer(3, TimeUnit.SECONDS).subscribe({
            iExampleServiceCallback?.onStringListener(exampleSyncStringValue)
        }, {}))
    }

    private fun waitAndSendInt() {
        compositeDisposable.add(Single.timer(3, TimeUnit.SECONDS).subscribe({
            iExampleServiceCallback?.onIntListener(exampleSyncIntValue)
        }, {}))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}