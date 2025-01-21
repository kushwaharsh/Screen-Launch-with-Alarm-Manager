package com.example.alarmmanagerintegration


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.alarmmanagerintegration.HomeScreen
import com.example.alarmmanagerintegration.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Wake up the device if it is in a deep sleep state
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "AlarmReceiver:WakeLock"
        )
        wakeLock.acquire(10000) // Wake for 3 seconds

        //Create an intent to launch the HomeScreen activity when the notification is tapped
        val homeIntent = Intent(context, HomeScreen::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        // Check if the app has the permission to display over other apps
        if (!Settings.canDrawOverlays(context)) {
            // If not, show a notification asking for permission (or guide the user to settings)
            val permissionIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            permissionIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(permissionIntent)
            Log.d(
                "AlarmReceiver",
                "Permission not granted to draw over other apps. Requesting permission."
            )
            return
        }
        try {
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(homeIntent)
            Log.d("AlarmReceiver", "HomeScreen activity launched.")
        } catch (e: Exception) {
            Log.e("AlarmReceiver", "Error launching activity: ${e.message}")
        }


        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            homeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        // Trigger the activity through the PendingIntent
        try {
            // This will start the activity directly
            pendingIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }

        // Ensure the notification channel exists (for Android O and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Notification Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for alarm triggered notifications"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create and show the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Alarm Triggered")
            .setContentText("Tap to open HomeScreen")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your app's icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

        wakeLock.release()
    }

    companion object {
        const val CHANNEL_ID = "ALARM_NOTIFICATION_CHANNEL"
    }
}
