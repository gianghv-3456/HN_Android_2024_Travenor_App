package com.example.travenor.screen.main

import android.util.Log
import android.view.View
import com.example.travenor.data.sharedpreference.SharedPreferencesManager
import com.example.travenor.databinding.ActivityMainBinding
import com.example.travenor.utils.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun getLayoutRoot(): View {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initView() {
        // TODO(IMPLEMENT)
        testSharedPreferencesUserInterests()
    }

    private fun testSharedPreferencesUserInterests() {
        // TODO(REMOVE THIS WHEN DONE
        val sharedPreferencesManager = SharedPreferencesManager.getInstance(this)
        val placeList = sharedPreferencesManager.getUserPlaceInterest()
        val foodList = sharedPreferencesManager.getUserFoodInterest()

        placeList.forEach {
            Log.d(LOG_TAG, it.name)
        }
        foodList.forEach {
            Log.d(LOG_TAG, it.name)
        }
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
    }
}
