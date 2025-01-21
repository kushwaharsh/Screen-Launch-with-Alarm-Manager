package com.example.alarmmanagerintegration

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.alarmmanagerintegration.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CODE_PERMISSIONS = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Alarm Button
        binding.setAlarmButton.setOnClickListener {
            Log.d(TAG , "Alarm button clicked")
            if (checkPermissions()) {
                setAlarm()
            } else {
                requestPermissions()
            }
        }
    }

    // Check for necessary permissions
    private fun checkPermissions(): Boolean {
        val foregroundServicePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.FOREGROUND_SERVICE
        ) == PackageManager.PERMISSION_GRANTED

        return foregroundServicePermission
    }

    // Request required permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.FOREGROUND_SERVICE),
            REQUEST_CODE_PERMISSIONS
        )
    }

    // Set the alarm using AlarmManager
    private fun setAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Alarm after 5 seconds from now
        val triggerTime = System.currentTimeMillis() + 5000

        val intent = Intent(this, AlarmReceiver::class.java).apply {
            action = "com.example.alarmmanagerintegration.ALARM_TRIGGERED"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // API 31+ specific handling for exact alarms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                showToast("Alarm set for 5 seconds from now.")
            } else {
                requestExactAlarmPermission()
            }
        } else {
            // For devices below API 31
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            showToast("Alarm set for 5 seconds from now.")
        }
    }

    // Prompt the user to grant Exact Alarm permission
    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
            showToast("Please enable Exact Alarm permission in settings.")
        }
    }

    // Handle permissions result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                setAlarm()
            } else {
                showToast("Permissions are required for the app to function.")
            }
        }
    }

    // Helper function to show toasts
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
