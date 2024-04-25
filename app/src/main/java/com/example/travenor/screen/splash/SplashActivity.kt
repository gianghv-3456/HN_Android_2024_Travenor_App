package com.example.travenor.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.travenor.data.sharedpreference.SharedPreferencesManager
import com.example.travenor.databinding.ActivitySplashBinding
import com.example.travenor.screen.MainActivity
import com.example.travenor.screen.onboarding.OnboardingActivity
import com.example.travenor.utils.base.BaseActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private lateinit var mBinding: ActivitySplashBinding

    override fun getLayoutRoot(): View {
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initView() {
        delayThenOpenNextActivity()
    }

    private fun delayThenOpenNextActivity() {
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
        if (isFirstTimeAppOpen()) {
            startOnBoardingActivity()
        } else {
            startMainActivity()
        }
    }

    private fun startOnBoardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun isFirstTimeAppOpen(): Boolean {
        return SharedPreferencesManager.getInstance(this).isFirstAppOpen()
    }

    companion object {
        const val DELAY_TIME = 1000L
    }
}
