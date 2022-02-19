package kr.co.seoft.simple_service.count

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import kr.co.seoft.simple_service.MainActivity
import kr.co.seoft.simple_service.R
import kr.co.seoft.simple_service.util.AlarmChannelConfig


class CompleteNotificationController(private val context: Context) {
    companion object {
        private const val NOTIFY_ID = 1198
    }

    fun show() {
        val pendingIntent = PendingIntent.getActivity(
            context, NOTIFY_ID, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, AlarmChannelConfig.SOUND_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Complete")
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setAutoCancel(true)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build().apply {
                flags = flags or Notification.FLAG_INSISTENT // 사운드 반복 여부
            }

        val manager =
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
                ?: return
        manager.notify(NOTIFY_ID, notification)
    }

}