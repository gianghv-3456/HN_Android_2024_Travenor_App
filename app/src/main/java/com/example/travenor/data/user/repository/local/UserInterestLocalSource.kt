package com.example.travenor.data.user.repository.local

import com.example.travenor.constant.Food
import com.example.travenor.constant.Place
import com.example.travenor.core.ResultListener
import com.example.travenor.data.sharedpreference.SharedPreferencesManager
import com.example.travenor.data.user.source.UserInterestSource
import java.io.IOException

class UserInterestLocalSource private constructor(
    private val mSharedPreferencesManager: SharedPreferencesManager
) : UserInterestSource.Local {
    override fun getFirstAppOpenState(listener: ResultListener<Boolean>) {
        try {
            val isFirstOpen = mSharedPreferencesManager.isFirstAppOpen()
            listener.onSuccess(isFirstOpen)
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    override fun saveFirstAppOpenState(state: Boolean, listener: ResultListener<Any>) {
        try {
            mSharedPreferencesManager.setFirstAppOpen(state)
            listener.onSuccess(null)
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    override fun getUserInterestedPlace(listener: ResultListener<List<Place>>) {
        try {
            val result = mSharedPreferencesManager.getUserPlaceInterest()
            listener.onSuccess(result)
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    override fun getUserLastLocation(): Pair<Double, Double> {
        return mSharedPreferencesManager.getLocation()
    }

    override fun getUserInterestedFood(listener: ResultListener<List<Food>>) {
        try {
            val result = mSharedPreferencesManager.getUserFoodInterest()
            listener.onSuccess(result)
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    override fun saveUserInterestedPlace(placeList: List<Place>, listener: ResultListener<Any>) {
        try {
            mSharedPreferencesManager.saveUserPlaceInterest(placeList)
            listener.onSuccess(null)
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    override fun saveUserInterestedFood(foodList: List<Food>, listener: ResultListener<Any>) {
        try {
            mSharedPreferencesManager.saveUserFoodInterest(foodList)
            listener.onSuccess(null)
        } catch (e: IOException) {
            listener.onError(e)
        }
    }

    companion object {
        private var instance: UserInterestLocalSource? = null

        fun getInstance(sharedPrefsManager: SharedPreferencesManager) = synchronized(this) {
            instance ?: UserInterestLocalSource(sharedPrefsManager).also { instance = it }
        }
    }
}
