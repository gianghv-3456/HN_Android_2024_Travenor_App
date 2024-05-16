package com.example.travenor.screen.onboarding

import com.example.travenor.constant.Food
import com.example.travenor.constant.Place
import com.example.travenor.core.ResultListener
import com.example.travenor.data.repository.UserRepository
import java.io.IOException

class UserInterestSurveyPresenter internal constructor(private val userRepository: UserRepository) :
    UserInterestSurveyContract.Presenter {
    private var mView: UserInterestSurveyContract.View? = null

    override fun saveUserSelection(placeList: List<Place>, foodList: List<Food>) {
        var isPlaceSaved = false
        var isFoodSaved = false

        try {
            userRepository.saveUserInterestedPlace(
                placeList,
                object : ResultListener<Any> {
                    override fun onSuccess(data: Any?) {
                        isPlaceSaved = true
                    }

                    override fun onError(exception: Exception?) {
                        mView?.onSaveUserSurveyError(exception)
                    }
                }
            )

            userRepository.saveUserInterestedFood(
                foodList,
                object : ResultListener<Any> {
                    override fun onSuccess(data: Any?) {
                        isFoodSaved = true
                    }

                    override fun onError(exception: Exception?) {
                        mView?.onSaveUserSurveyError(exception)
                    }
                }
            )

            userRepository.saveFirstAppOpenState(
                false,
                object : ResultListener<Any> {
                    override fun onSuccess(data: Any?) {
                        // No Action needed
                    }

                    override fun onError(exception: Exception?) {
                        mView?.onSaveUserSurveyError(exception)
                    }
                }
            )

            if (isFoodSaved && isPlaceSaved) mView?.onSaveUserSurveySuccess()
        } catch (e: IOException) {
            mView?.onSaveUserSurveyError(e)
        }
    }

    override fun setView(view: UserInterestSurveyContract.View?) {
        this.mView = view
    }
}
