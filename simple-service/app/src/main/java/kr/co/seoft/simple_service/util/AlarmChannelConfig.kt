package kr.co.seoft.simple_service.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build

object AlarmChannelConfig {

    const val CHANNEL_ID = "CHANNEL_ID"
    private const val CHANNEL_NAME = "CHANNEL_NAME"

    const val SOUND_CHANNEL_ID = "SOUND_CHANNEL_ID"
    private const val SOUND_CHANNEL_NAME = "SOUND_CHANNEL_NAME"

    fun registAlarmChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
                    ?: return
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    setSound(null, null)
                }
            )

            notificationManager.createNotificationChannel(
                NotificationChannel(
                    SOUND_CHANNEL_ID,
                    SOUND_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {

                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()

                    setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), audioAttributes
                    )
                }
            )
        }
    }
}