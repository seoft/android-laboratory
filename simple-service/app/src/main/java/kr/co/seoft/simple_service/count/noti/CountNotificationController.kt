package kr.co.seoft.simple_service.count.noti

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import kr.co.seoft.simple_service.MainActivity
import kr.co.seoft.simple_service.R
import kr.co.seoft.simple_service.count.noti.CountNotiService.Companion.STOP_SERVICE
import kr.co.seoft.simple_service.util.AlarmChannelConfig
import kr.co.seoft.simple_service.util.e

class CountNotificationController(private val service: CountNotiService) {

    companion object {
        private const val NOTIFY_ID = 1197
    }

    fun showWithUpdate(second: Int) {
        "showWithUpdate $second".e()
        val notificationView = RemoteViews(service.packageName, R.layout.notification_count)
        val notificationTvCount = R.id.notificationTvCount
        val notificationIvClose = R.id.notificationIvClose

        val pendingIntent = PendingIntent.getActivity(
            service, NOTIFY_ID, Intent(service, MainActivity::class.java).apply {
            }, PendingIntent.FLAG_IMMUTABLE
        )

        val stopServicePendingIntent = PendingIntent.getService(
            service, NOTIFY_ID, Intent(service, CountNotiService::class.java).apply {
                putExtra(STOP_SERVICE, true)
            }, PendingIntent.FLAG_IMMUTABLE
        )

        notificationView.setTextViewText(notificationTvCount, "counting : $second")

        notificationView.setOnClickPendingIntent(notificationIvClose, stopServicePendingIntent)

        val priority = NotificationManager.IMPORTANCE_DEFAULT
        val notification =
            NotificationCompat.Builder(service, AlarmChannelConfig.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationView)
                .setSound(null)
                .setPriority(priority)
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            service.startForeground(NOTIFY_ID, notification)
        }
    }

    fun hide() {
        service.stopForeground(true)
    }

}