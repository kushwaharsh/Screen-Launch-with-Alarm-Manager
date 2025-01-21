package com.example.alarmmanagerintegration

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmmanagerintegration.databinding.ActivityHomeScreenBinding

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding

    companion object {
        private const val TAG = "HomeScreen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ensure the screen turns on and unlocks
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        Log.d(TAG, "HomeScreen launched.")

        // Handle the case when the activity is launched as a result of an alarm
        handleIntentData()
    }


    private fun handleIntentData() {
        // Check if intent contains data or action, log it for debugging
        intent?.let {
            val action = it.action
            val extras = it.extras
            Log.d(TAG, "Intent received with action: $action and extras: $extras")
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the UI or perform any necessary actions
    }

}
