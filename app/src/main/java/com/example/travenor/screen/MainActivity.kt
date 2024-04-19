package com.example.travenor.screen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.travenor.R

class MainActivity : AppCompatActivity() {
    companion object {
        const val DELAY_TIME = 1000L
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableUIDrawOnSystemBar()

        // TODO("Remove this when need to load something on splash time")
        Handler(Looper.getMainLooper()).postDelayed({
            // Do something after 1000ms
            openOnboardingScreen()
        }, DELAY_TIME)
    }

    /**
     * Start onboarding activity and remove current activity from backstack
     */
    private fun openOnboardingScreen() {
        // TODO("Start OnboardingActivity in later PR")
    }

    private fun enableUIDrawOnSystemBar() {
        val decorView: View = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}
