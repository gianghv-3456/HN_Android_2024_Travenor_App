package com.example.travenor.data.user.repository

import com.example.travenor.constant.Food
import com.example.travenor.constant.Place
import com.example.travenor.core.ResultListener
import com.example.travenor.data.user.source.UserInterestSource

class UserRepository private constructor(
    private val local: UserInterestSource.Local
) : UserInterestSource.Local {

    override fun getFirstAppOpenState(listener: ResultListener<Boolean>) {
        local.getFirstAppOpenState(listener)
    }

    override fun saveFirstAppOpenState(state: Boolean, listener: ResultListener<Any>) {
        local.saveFirstAppOpenState(state, listener)
    }

    override fun saveUserInterestedPlace(placeList: List<Place>, listener: ResultListener<Any>) {
        local.saveUserInterestedPlace(placeList, listener)
    }

    override fun saveUserInterestedFood(foodList: List<Food>, listener: ResultListener<Any>) {
        local.saveUserInterestedFood(foodList, listener)
    }

    override fun getUserInterestedPlace(listener: ResultListener<List<Place>>) {
        local.getUserInterestedPlace(listener)
    }

    override fun getUserInterestedFood(listener: ResultListener<List<Food>>) {
        local.getUserInterestedFood(listener)
    }

    companion object {
        private var instance: UserRepository? = null

        fun getInstance(local: UserInterestSource.Local) = synchronized(this) {
            instance ?: UserRepository(local).also { instance = it }
        }
    }
}
