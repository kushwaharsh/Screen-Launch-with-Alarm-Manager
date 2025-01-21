package com.example.alarmmanagerintegration


import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.alarmmanagerintegration.HomeScreen

class AlarmService : IntentService("AlarmService") {

    override fun onCreate() {
        super.onCreate()
        // Create the notification channel for devices above Android O
        createNotificationChannel()
    }

    override fun onHandleIntent(intent: Intent?) {
        // Acquire a wake lock to keep the CPU awake
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "AlarmService:WakeLock"
        )
        wakeLock.acquire(5000)

        // Create the PendingIntent to launch the HomeScreen activity
        val homeIntent = Intent(this, HomeScreen::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            homeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create and show the notification in the foreground
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarm Triggered")
            .setContentText("Tap to open HomeScreen")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Start the service in the foreground
        startForeground(1, notification)

        // Release wake lock and stop the service after the task is completed
        wakeLock.release()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Service Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for alarm service notifications"
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    companion object {
        const val CHANNEL_ID = "ALARM_NOTIFICATION_CHANNEL"
    }
}
