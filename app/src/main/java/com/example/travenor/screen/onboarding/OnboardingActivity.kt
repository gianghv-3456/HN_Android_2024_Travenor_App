package com.example.travenor.screen.onboarding

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.travenor.R
import com.example.travenor.constant.Food
import com.example.travenor.constant.Place
import com.example.travenor.constant.genericValueOf
import com.example.travenor.constant.isNameOfEnum
import com.example.travenor.data.sharedpreference.SharedPreferencesManager
import com.example.travenor.data.user.repository.UserRepository
import com.example.travenor.data.user.repository.local.UserInterestLocalSource
import com.example.travenor.databinding.ActivityOnboardingBinding
import com.example.travenor.screen.main.MainActivity
import com.example.travenor.screen.onboarding.adapter.OnboardingAdapter
import com.example.travenor.screen.onboarding.adapter.OnboardingAdapter.Companion.FIRST_PAGE_INDEX
import com.example.travenor.screen.onboarding.adapter.OnboardingAdapter.Companion.SECOND_PAGE_INDEX
import com.example.travenor.screen.onboarding.adapter.OnboardingAdapter.Companion.SURVEY_PAGE_INDEX
import com.example.travenor.screen.onboarding.adapter.OnboardingAdapter.Companion.THIRD_PAGE_INDEX
import com.example.travenor.utils.base.BaseActivity

class OnboardingActivity :
    BaseActivity(),
    UserInterestSurveyContract.View,
    OnboardingAdapter.OnUserInterestSurveyChangeListener {

    private lateinit var mUserSurveyPresenter: UserInterestSurveyPresenter
    private lateinit var mBinding: ActivityOnboardingBinding

    private val mUserSurveyResult = mutableMapOf<String, Boolean>()
    override fun getLayoutRoot(): View {
        mBinding = ActivityOnboardingBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun initView() {
        val sharedPreferencesManager = SharedPreferencesManager.getInstance(this)

        val userInterestsDataSource = UserInterestLocalSource.getInstance(sharedPreferencesManager)
        val userRepository = UserRepository.getInstance(userInterestsDataSource)
        mUserSurveyPresenter = UserInterestSurveyPresenter(userRepository)
        mUserSurveyPresenter.setView(this)

        showOnboardingPagerView()
        registerOnPageViewPageChange()
        registerSkipButtonClick()
        registerNextPageButtonClick()
    }

    override fun onSaveUserSurveySuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onSaveUserSurveyError(exception: Exception?) {
        val msg = getString(R.string.msg_save_survey_fail)
        Log.d(LOG_TAG, msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showOnboardingPagerView() {
        val adapter = OnboardingAdapter()
        mBinding.viewPager.adapter = adapter
        adapter.setOnUserSurveyChangeListener(this)
    }

    private fun registerOnPageViewPageChange() {
        mBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    FIRST_PAGE_INDEX, SECOND_PAGE_INDEX, THIRD_PAGE_INDEX -> {
                        // display skip button and next button
                        mBinding.buttonNextPage.text = getString(R.string.onboarding_next_btn)
                        mBinding.textSkipAsButton.visibility = View.VISIBLE
                    }

                    SURVEY_PAGE_INDEX -> {
                        // Hide skip button and show "Get started" button
                        mBinding.buttonNextPage.text =
                            getString(R.string.onboarding_get_started_btn)
                        mBinding.textSkipAsButton.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun registerSkipButtonClick() {
        mBinding.textSkipAsButton.setOnClickListener {
            mBinding.viewPager.currentItem = SURVEY_PAGE_INDEX
        }
    }

    private fun registerNextPageButtonClick() {
        mBinding.buttonNextPage.setOnClickListener {
            val currentPosition = mBinding.viewPager.currentItem
            if (currentPosition < SURVEY_PAGE_INDEX) {
                mBinding.viewPager.currentItem += 1
            } else {
                saveUserSurveyResult()
            }
        }
    }

    override fun userSurveyChangeListener(pair: Pair<String, Boolean>) {
        val key = pair.first
        val value = pair.second
        mUserSurveyResult[key] = value
    }

    private fun saveUserSurveyResult() {
        val placeList = mutableListOf<Place>()
        val foodList = mutableListOf<Food>()

        mUserSurveyResult.forEach { (name, isSelected) ->
            if (isNameOfEnum<Place>(name)) {
                if (isSelected) {
                    placeList.add(genericValueOf<Place>(name))
                }
            }

            if (isNameOfEnum<Food>(name)) {
                if (isSelected) {
                    foodList.add(genericValueOf<Food>(name))
                }
            }
        }

        mUserSurveyPresenter.saveUserSelection(placeList, foodList)
    }

    companion object {
        private const val LOG_TAG = "OnboardingActivity"
    }
}
