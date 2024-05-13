package com.example.travenor.data.source

import com.example.travenor.constant.Food
import com.example.travenor.constant.Place
import com.example.travenor.core.ResultListener

interface UserInterestSource {
    interface Local {
        fun getFirstAppOpenState(listener: ResultListener<Boolean>)

        fun saveFirstAppOpenState(state: Boolean, listener: ResultListener<Any>)

        fun getUserInterestedPlace(listener: ResultListener<List<Place>>)

        fun getUserLastLocation(): Pair<Double, Double>

        fun getUserInterestedFood(listener: ResultListener<List<Food>>)

        fun saveUserInterestedPlace(placeList: List<Place>, listener: ResultListener<Any>)

        fun saveUserInterestedFood(foodList: List<Food>, listener: ResultListener<Any>)
    }
}
