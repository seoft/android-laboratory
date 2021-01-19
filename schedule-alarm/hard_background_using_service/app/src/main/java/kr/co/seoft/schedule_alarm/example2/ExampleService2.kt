package kr.co.seoft.schedule_alarm.example2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class ExampleService2 : Service() {

    private val exampleStringValue = "STRING"
    private val exampleIntValue = 12345

    private val exampleSyncStringValue = "SYNC_STRING"
    private val exampleSyncIntValue = 67890

    private val compositeDisposable = CompositeDisposable()

    private val binder = ExampleService2Binder()

    private var listener: OnExampleService2Listener? = null

    fun getStringValue(): String {
        return exampleStringValue
    }

    fun getIntValue(): Int {
        return exampleIntValue
    }

    fun requestStringAsync() {
        waitAndSendString()
    }

    fun requestIntAsync() {
        waitAndSendInt()
    }

    fun setToListener(listener: OnExampleService2Listener?) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    private fun waitAndSendString() {
        compositeDisposable.add(Single.timer(3, TimeUnit.SECONDS).subscribe({
            listener?.onString(exampleSyncStringValue)
        }, {}))
    }

    private fun waitAndSendInt() {
        compositeDisposable.add(Single.timer(3, TimeUnit.SECONDS).subscribe({
            listener?.onInt(exampleSyncIntValue)
        }, {}))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    inner class ExampleService2Binder : Binder() {
        val service: ExampleService2
            get() = this@ExampleService2  // return current service
    }

}