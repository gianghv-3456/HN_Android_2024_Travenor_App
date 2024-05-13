package com.example.travenor.data.source.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import com.example.travenor.constant.Food
import com.example.travenor.constant.Place
import com.example.travenor.constant.genericValueOf

class SharedPreferencesManager private constructor(context: Context?) {

    private var mSharedPrefs: SharedPreferences? = null

    init {
        mSharedPrefs =
            context?.applicationContext?.getSharedPreferences(PREF_MY_APP, Context.MODE_PRIVATE)
    }

    fun isFirstAppOpen(): Boolean {
        return mSharedPrefs?.getBoolean(PREF_FIRST_APP_OPEN, true) == true
    }

    fun setFirstAppOpen(isFirst: Boolean) {
        val editor = mSharedPrefs?.edit()
        editor?.putBoolean(PREF_FIRST_APP_OPEN, isFirst)
        editor?.apply()
    }

    fun getLocation(): Pair<Double, Double> {
        val lat = mSharedPrefs?.getFloat(PREF_USER_LOCATION_LAT, 0F)?.toDouble() ?: 0.0
        val lng = mSharedPrefs?.getFloat(PREF_USER_LOCATION_LONG, 0F)?.toDouble() ?: 0.0
        return Pair(lat, lng)
    }

    fun saveLocation(lat: Double, lng: Double) {
        val editor = mSharedPrefs?.edit()
        editor?.putFloat(PREF_USER_LOCATION_LAT, lat.toFloat())
        editor?.putFloat(PREF_USER_LOCATION_LONG, lng.toFloat())
        editor?.apply()
    }

    fun getUserPlaceInterest(): List<Place> {
        val str = mSharedPrefs?.getString(PREF_USER_INTEREST_PLACE_TYPE, "")
        if (str.isNullOrEmpty()) return emptyList()

        val list = stringListFromPresenterString(str)
        val placeList = mutableListOf<Place>()
        list.forEach {
            val place = genericValueOf<Place>(value = it)
            placeList.add(place)
        }
        return placeList
    }

    fun saveUserPlaceInterest(list: List<Place>) {
        val editor = mSharedPrefs?.edit()
        val stringList = list.map { it.name }
        val presenterString = stringListToPresenterString(stringList)
        editor?.putString(PREF_USER_INTEREST_PLACE_TYPE, presenterString)
        editor?.apply()
    }

    fun getUserFoodInterest(): List<Food> {
        val str = mSharedPrefs?.getString(PREF_USER_INTEREST_FOOD_TYPE, "")
        if (str.isNullOrEmpty()) return emptyList()

        val list = stringListFromPresenterString(str)
        val foodList = mutableListOf<Food>()
        list.forEach {
            val food = genericValueOf<Food>(value = it)
            foodList.add(food)
        }
        return foodList
    }

    fun saveUserFoodInterest(list: List<Food>) {
        val editor = mSharedPrefs?.edit()
        val stringList = list.map { it.name }
        val presenterString = stringListToPresenterString(stringList)
        editor?.putString(PREF_USER_INTEREST_FOOD_TYPE, presenterString)
        editor?.apply()
    }

    private fun stringListToPresenterString(list: List<String>): String {
        return list.joinToString(",")
    }

    private fun stringListFromPresenterString(presenter: String): List<String> {
        return presenter.split(",")
    }

    companion object {
        private const val PREF_MY_APP = "com.example.travenor.data.shared_preference.my_app_prefs"
        private const val PREF_FIRST_APP_OPEN =
            "com.example.travenor.data.shared_preference.first_app_open"

        private const val PREF_USER_INTEREST_PLACE_TYPE =
            "com.example.travenor.data.shared_preference.interest_place"
        private const val PREF_USER_INTEREST_FOOD_TYPE =
            "com.example.travenor.data.shared_preference.interest_food"
        private const val PREF_USER_LOCATION_LAT =
            "com.example.travenor.data.shared_preference.location_lat"
        private const val PREF_USER_LOCATION_LONG =
            "com.example.travenor.data.shared_preference.location_long"

        private var instance: SharedPreferencesManager? = null

        @Synchronized
        fun getInstance(context: Context?): SharedPreferencesManager {
            if (instance == null) instance = SharedPreferencesManager(context)
            return instance as SharedPreferencesManager
        }
    }
}
