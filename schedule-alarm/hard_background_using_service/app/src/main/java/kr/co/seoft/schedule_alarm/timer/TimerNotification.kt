package kr.co.seoft.schedule_alarm.timer

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import kr.co.seoft.schedule_alarm.R
import kr.co.seoft.schedule_alarm.util.App


object TimerNotification {

    private const val NOTIFY_ID = 1197

    var lastIndex = -1
    var lastSecond = -1L

    fun removeNotification(service: TimerService) {
        service.stopForeground(true)
    }

    fun showNotification(service: TimerService, times: IntArray, index: Int, second: Long) {
        if (lastIndex == index && lastSecond == second) return

        lastIndex = index
        lastSecond = second

        val pendingIntent = PendingIntent.getActivity(
            service, NOTIFY_ID, Intent(service, TimerActivity::class.java).apply {
                putExtra(TimerActivity.EXTRA_TIMES, times)
            }, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationView = RemoteViews(service.packageName, R.layout.notification_timer)
        val textViewId = R.id.notificationTimerTextView

        notificationView.setTextViewText(textViewId, "${index}번째 ${second}초")

        val priority = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            NotificationManager.IMPORTANCE_DEFAULT
        } else Notification.PRIORITY_DEFAULT

        val notification = NotificationCompat.Builder(service, App.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setContent(notificationView)
            .setSound(null)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .build()

        service.startForeground(NOTIFY_ID, notification)
    }
}