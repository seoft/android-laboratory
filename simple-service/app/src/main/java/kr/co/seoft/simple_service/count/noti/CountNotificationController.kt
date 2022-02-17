package kr.co.seoft.simple_service.count.noti

import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import kr.co.seoft.simple_service.R
import kr.co.seoft.simple_service.util.AlarmChannelConfig

class CountNotificationController(private val countNotiService: CountNotiService) {

    companion object {
        private const val NOTIFY_ID = 1197
    }

    fun show() {
        val priority = NotificationManager.IMPORTANCE_DEFAULT
        val notification = NotificationCompat.Builder(countNotiService, AlarmChannelConfig.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setContentText("running")
            .setSound(null)
            .setPriority(priority)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            countNotiService.startForeground(NOTIFY_ID, notification)
        }
    }

    fun hide() {
        countNotiService.stopForeground(true)
    }

}