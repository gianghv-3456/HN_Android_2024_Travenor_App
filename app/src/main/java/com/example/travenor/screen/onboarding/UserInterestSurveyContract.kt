package com.example.travenor.screen.onboarding

import com.example.travenor.constant.Food
import com.example.travenor.constant.Place
import com.example.travenor.utils.base.BasePresenter

open class UserInterestSurveyContract {
    interface View {
        fun onSaveUserSurveySuccess()

        fun onSaveUserSurveyError(exception: Exception?)
    }

    /**
     * Presenter
     */
    interface Presenter : BasePresenter<View> {
        fun saveUserSelection(placeList: List<Place>, foodList: List<Food>)
    }
}
